package me.myeolchi.obe.listeners

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.tap.mojangapi.MojangAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.util.Items.gorgonzolaAxe
import me.myeolchi.obe.ObePlugin
import me.myeolchi.obe.core.Skill
import me.myeolchi.obe.util.EventValidator
import me.myeolchi.obe.util.Items
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.ceil

/**
 * 유체이탈
 */
class Gorgonzola: Listener, Skill {
    override fun skillItem() = gorgonzolaAxe

    val trapped = ArrayList<Player>()

    @EventHandler
    fun gorgonzola(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!EventValidator.validateItem(e.player, gorgonzolaAxe)) return
        if (!CoolTimeManager.useItem("gorgonzola", e.player, 30 * 1000)) return

        val result = e.player.world.rayTraceEntities(e.player.eyeLocation, e.player.eyeLocation.direction, 20.0) { entity -> entity is Player && entity != e.player }
        result?.hitEntity?.let { target ->
            val skinProfile = MojangAPI.fetchSkinProfile(MojangAPI.fetchProfile(target.name)!!.uuid())!!
            val initTargetPos = target.location.clone()
            val displacement = target.location.clone().subtract(e.player.eyeLocation).toVector()
            val loop = ceil(displacement.length()).toInt()
            val delta = displacement.multiply(1.0 / loop)

            EventValidator.damageItem(e.player, 30)

            HeartbeatScope().launch {
                val current = e.player.eyeLocation.clone()
                repeat(loop) {
                    e.player.world.spawnParticle(Particle.SONIC_BOOM, current.add(delta), 1,0.0, 0.0, 0.0, 0.1)
                    delay(250)
                }

                if (initTargetPos.getNearbyEntities(2.0, 2.0, 2.0).contains(target)) {
                    target.world.spawnParticle(Particle.EXPLOSION_HUGE, initTargetPos, 20, 0.0, 0.0, 0.0)

                    trapped.add(target as Player)
                    target.isInvisible = true
                    val backup = target.inventory.contents
                    target.inventory.clear()

                    val fakePlayer = ObePlugin.instance.fakeServer.spawnPlayer(initTargetPos, target.name, skinProfile.profileProperties().toSet()) // spawn npc of target

                    delay(1000)
                    target.velocity = delta

                    delay(500)
                    target.allowFlight = true
                    target.isFlying = true

                    delay(3500)
                    if (target.gameMode == GameMode.SURVIVAL || target.gameMode == GameMode.SPECTATOR) {
                        target.allowFlight = false
                    }
                    target.teleport(initTargetPos)
                    trapped.remove(target)
                    target.isInvisible = false
                    target.inventory.contents = backup
                    fakePlayer.remove()
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(e: PlayerInteractEvent) {
        if (trapped.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if (trapped.contains(e.entity)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBreakBlock(e: BlockBreakEvent) {
        if (trapped.contains(e.player)) {
            e.isCancelled = true
        }
    }
}