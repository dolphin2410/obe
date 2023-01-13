package me.myeolchi.obe.listeners

import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.core.Skill
import me.myeolchi.obe.util.EventValidator
import me.myeolchi.obe.util.Items
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 지진
 */
class Bulgogi: Listener, Skill {
    override fun skillItem() = Items.bulgogiShovel

    @EventHandler
    fun bulgogi(e: PlayerInteractEvent) {
        if (!EventValidator.validateItem(e.player, Items.bulgogiShovel)) return
        if (!CoolTimeManager.useItem("bulgogi", e.player, 1 * 60 * 1000)) return

        EventValidator.removeItem(e.player)

        val safeRadius = 3
        val unsafeRadius = 7

        vanillaBanana(safeRadius, unsafeRadius, e.player)
    }

    private fun vanillaBanana(safeRadius: Int, unsafeRadius: Int, player: Player) {
        val unsafeZone = ArrayList<Vector>()

        val unsafeEntities = player.getNearbyEntities(unsafeRadius.toDouble(), -2.0, unsafeRadius.toDouble())
        val safeEntities = player.getNearbyEntities(safeRadius.toDouble(), -2.0, safeRadius.toDouble())

        unsafeEntities.forEach {
            if (!safeEntities.contains(it)) {
                it.velocity = player.location.clone().add(0.0, 1.0, 0.0).toVector().subtract(it.location.toVector())
            }
        }

        for (x in -unsafeRadius..unsafeRadius) {
            for (y in -2..10) {
                for (z in -unsafeRadius..unsafeRadius) {
                    if (sqrt(x.toDouble().pow(2) + z.toDouble().pow(2)) <= safeRadius) {
                        continue
                    }

                    if (sqrt(x.toDouble().pow(2) + z.toDouble().pow(2)) <= unsafeRadius) {
                        unsafeZone.add(Vector(x.toDouble(), y.toDouble(), z.toDouble()))
                    }
                }
            }
        }

        unsafeZone.forEach { relPos ->
            val block = player.world.getBlockAt(player.location.add(relPos))
            if (block.type == Material.AIR || block.type == Material.WATER) return@forEach

            val bd = block.blockData.clone()
            block.blockData = Material.AIR.createBlockData()
            val fallingBlock = player.world.spawnFallingBlock(block.location.clone().add(0.0, 1.0, 0.0), bd)
            fallingBlock.velocity = Vector(relPos.x / 3, 2.0, relPos.z / 3)
        }
    }
}