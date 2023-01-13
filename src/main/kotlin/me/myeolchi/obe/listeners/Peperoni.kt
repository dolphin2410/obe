package me.myeolchi.obe.listeners

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.myeolchi.obe.ObePlugin
import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.util.Items.peperoniSword
import me.myeolchi.obe.core.Skill
import me.myeolchi.obe.util.EventValidator
import org.bukkit.*
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * 요술 지팡이
 */
class Peperoni: Listener, Skill {
    override fun skillItem() = peperoniSword

    @EventHandler
    fun retrievePeperoni(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        if (e.player.inventory.itemInMainHand != ItemStack(Material.IRON_SWORD)) return
        if (!CoolTimeManager.useItem("peperoni", e.player, 1 * 60 * 1000)) return
        val item = e.player.inventory.itemInMainHand

        val playerDirection = e.player.eyeLocation.direction
        val initTheta = atan(playerDirection.z / playerDirection.x)
        val period = 2500
        val interval = 125
        val cycle = 1
        val radius = 2.7

        HeartbeatScope().launch {
            val playerLoc = e.player.location.clone()
            e.player.setPlayerWeather(WeatherType.DOWNFALL)
            repeat(period / interval * cycle * 3 / 4) { t ->
                val theta = 2.0 * PI * t * interval / period + initTheta // theta = wt = 2 * pi * t / T
                val x = cos(theta) * radius
                val z = sin(theta) * radius

                val thetaPrime = theta + PI
                val xPrime = cos(thetaPrime) * radius
                val zPrime = sin(thetaPrime) * radius

                val particleLocation = Location(e.player.world, playerLoc.x + x, playerLoc.y + 3.7, playerLoc.z + z)
                val particleLocationPrime = Location(e.player.world, playerLoc.x + xPrime, playerLoc.y + 3.7, playerLoc.z + zPrime)

                e.player.world.spawnParticle(
                    Particle.REDSTONE, particleLocation, 2,
                    Particle.DustOptions(Color.AQUA, 10f)
                )
                e.player.world.spawnParticle(
                    Particle.REDSTONE, particleLocationPrime, 2,
                    Particle.DustOptions(Color.AQUA, 10f)
                )

                delay(interval.toLong())
            }

            e.player.world.strikeLightningEffect(e.player.location)

            delay(100)

            e.player.resetPlayerWeather()
            if (item != e.player.inventory.itemInMainHand) return@launch
            e.player.inventory.setItemInMainHand(peperoniSword)
        }
    }

    @EventHandler
    fun peperoni(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        if (!EventValidator.validateItem(e.player, peperoniSword)) return
        e.player.setCooldown(Material.IRON_SWORD, 10)

        val result = e.player.world.rayTraceEntities(e.player.eyeLocation, e.player.eyeLocation.direction, 40.0) { entity -> (entity is LivingEntity || entity is Item) && entity != e.player }

        result?.hitEntity?.let { target ->
            EventValidator.damageItem(e.player, 30)

            if (target is Item) {
                ObePlugin.skillsManager.skills().forEach {
                    if (it.skillItem().type == target.itemStack.type && target.itemStack.amount == 1) {
                        val toGive = it.skillItem().clone()
                        if(toGive.itemMeta is Damageable) {
                            val dam = (target.itemStack.itemMeta as Damageable).damage
                            toGive.apply { editMeta { d -> (d as Damageable).damage = dam } }
                        }
                        e.player.world.dropItemNaturally(target.location, toGive)
                        e.player.world.strikeLightningEffect(target.location)
                        target.remove()
                    }
                }
                return
            }

            e.player.world.strikeLightning(target.location)
            (target as LivingEntity).damage(8.0)
        }
    }
}