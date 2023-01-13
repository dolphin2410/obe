package me.myeolchi.obe.util

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

object EventValidator {
    fun validateItem(player: Player, item: ItemStack): Boolean {
        return player.inventory.itemInMainHand.clone().apply { editMeta { (it as Damageable).damage = 0 } } == item
    }

    fun damageItem(player: Player, damage: Int) {
        val item = player.inventory.itemInMainHand
        item.editMeta {
            (it as Damageable).damage += damage
        }

        if (item.type.maxDurability - (item.itemMeta as Damageable).damage <= 0) {
            item.amount -= 1
        }
    }

    fun removeItem(player: Player) {
        val item = player.inventory.itemInMainHand
        item.amount -= 1
    }
}