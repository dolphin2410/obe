package me.myeolchi.obe.listeners

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.tap.fake.FakeEntity
import io.papermc.paper.math.Rotations
import kotlinx.coroutines.*
import me.myeolchi.obe.ObePlugin
import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.core.Skill
import me.myeolchi.obe.util.EventValidator
import me.myeolchi.obe.util.Items
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * 표창
 */
class Pineapple: Listener, Skill {
    override fun skillItem() = Items.pineapplePickaxe

    private val pineapples = HashMap<Player, FakeEntity<ArmorStand>>()

    @EventHandler
    fun pineappleInvoke(e: PlayerInteractEvent) {
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
        HeartbeatScope().launch {
            if (pineapples.containsKey(e.player)) {

                val stand = pineapples[e.player] ?: return@launch // Shoot direction
                stand.updateMetadata {
                    headRotations = Rotations.ofDegrees(90.0, 0.0, 0.0)
                }
                while(stand.valid) {
                    stand.moveTo(stand.location.clone().add(e.player.eyeLocation.direction.clone().normalize()))
                    stand.bukkitEntity.getNearbyEntities(1.0, 1.0, 1.0).forEach {
                        if (it is LivingEntity && it != e.player) {
                            it.damage(5.0)
                        }
                    }
                    delay(50)
                }
            }
        }
    }

    @EventHandler
    fun pineapple(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!EventValidator.validateItem(e.player, Items.pineapplePickaxe)) return
        if (!CoolTimeManager.useItem("pineapple", e.player, 1 * 10 * 1000)) return

        HeartbeatScope().launch {
            if (!pineapples.containsKey(e.player)) {
                val asSpawnLoc = e.player.location.clone().subtract(0.0, 2.5, 0.0).add(e.player.eyeLocation.direction.clone().normalize())
                val stand = ObePlugin.instance.fakeServer.spawnEntity(asSpawnLoc, ArmorStand::class.java)
                stand.updateMetadata {
                    isInvisible = true
                    isInvulnerable = true
                    equipment.helmet = ItemStack(Material.TOTEM_OF_UNDYING)
                }

                repeat(10) {
                    stand.moveTo(stand.location.clone().add(0.0, 0.2, 0.0))
                    delay(100)
                }

                pineapples[e.player] = stand

                Thread {
                    Thread.sleep(5000)
                    stand.remove()
                    pineapples.remove(e.player)
                }.start()

                // drag from ground
            }
        }
    }
}