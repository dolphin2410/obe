package me.myeolchi.obe.listeners

import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.util.Items.shrimpShovel
import me.myeolchi.obe.core.Skill
import me.myeolchi.obe.util.EventValidator
import me.myeolchi.obe.util.Items
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * 부비트랩
 */
class Shrimp: Listener, Skill {
    override fun skillItem() = shrimpShovel

    private val shrimps = HashMap<Player, Block>()

    private fun shrimpInvoke(player: Player, block: Block): Boolean {
        return if (shrimps.containsKey(player)) {
            val boobyTrap = shrimps[player]!!
            shrimps.remove(player)
            boobyTrap.world.createExplosion(boobyTrap.location, 4f)
            true
        } else {
            shrimps[player] = block
            false
        }
    }

    @EventHandler
    fun shrimp(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!EventValidator.validateItem(e.player, shrimpShovel)) return
        if (!CoolTimeManager.useItem("shrimp", e.player, 1 * 60 * 1000)) return

        val invokeRes = shrimpInvoke(e.player, e.player.world.getBlockAt(e.player.location))

        if (invokeRes) {
            e.player.inventory.setItemInMainHand(null)
        }
    }
}