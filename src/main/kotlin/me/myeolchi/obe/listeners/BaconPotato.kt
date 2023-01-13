package me.myeolchi.obe.listeners

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.papermc.paper.math.Rotations
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.util.Items.baconPotatoHoe
import me.myeolchi.obe.ObePlugin
import me.myeolchi.obe.core.Skill
import me.myeolchi.obe.util.EventValidator
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class BaconPotato: Listener, Skill {
    override fun skillItem() = baconPotatoHoe

    private fun selectPoints(numPoints: Int, init: Location, target: Location): List<Pair<Double, Double>> {
        val center = init.clone().add(target).multiply(0.5)
        val unitTheta = PI / numPoints

        val relInit = init.clone().subtract(center)

        val initX = relInit.x
        val initZ = relInit.z

        return (1..numPoints).map { n ->
            val theta = unitTheta * n
            val xPrime = initX * cos(theta) - initZ * sin(theta)
            val zPrime = initX * sin(theta) + initZ * cos(theta)
            xPrime + center.x to zPrime + center.z
        }
    }

    /**
     * 부매랑 폭탄
     */
    @EventHandler
    fun baconPotato(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!EventValidator.validateItem(e.player, baconPotatoHoe)) return
        if (!CoolTimeManager.useItem("baconPotato", e.player, 60 * 1000)) return

        val res = e.player.world.rayTraceBlocks(e.player.eyeLocation, e.player.eyeLocation.direction, 50.0)
        res?.hitBlock?.let { target ->
            val initPos = e.player.eyeLocation.clone()
            val targetPos = target.location.clone()
            val numPoints = 20
            val points = selectPoints(numPoints, initPos, targetPos)
            val unitY = (targetPos.y - initPos.y) / numPoints
            val stand = ObePlugin.instance.fakeServer.spawnEntity(initPos, ArmorStand::class.java)

            stand.updateMetadata {
                isInvisible = true
                equipment.helmet = baconPotatoHoe
                headRotations = Rotations.ofDegrees(90.0, 0.0, 0.0)
            }

            HeartbeatScope().launch {
                points.forEachIndexed { index, point ->
                    val pos = Vector(point.first, unitY * (index + 1) + initPos.y - 0.8, point.second).add(e.player.eyeLocation.direction.clone().normalize())
                    stand.moveTo(pos.toLocation(e.player.world))
                    delay(130)
                }

                targetPos.createExplosion(4f)
                stand.remove()
            }
        }
    }
}