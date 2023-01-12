package me.myeolchi.obe.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Items {
    val gorgonzolaAxe = ItemStack(Material.STONE_AXE).apply {
        editMeta {
            it.displayName(Component.text("Axe of Gorgonzola", NamedTextColor.AQUA))
            it.addEnchant(Enchantment.LUCK, 1, false)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    val peperoniSword = ItemStack(Material.IRON_SWORD).apply {
        editMeta {
            it.displayName(Component.text("Sword of Peperoni", NamedTextColor.AQUA))
            it.addEnchant(Enchantment.LUCK, 1, false)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    val baconPotatoHoe = ItemStack(Material.GOLDEN_HOE).apply {
        editMeta {
            it.displayName(Component.text("Hoe of Bacon Potato", NamedTextColor.AQUA))
            it.addEnchant(Enchantment.LUCK, 1, false)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    val shrimpShovel = ItemStack(Material.IRON_SHOVEL).apply {
        editMeta {
            it.displayName(Component.text("Shovel of Shrimp", NamedTextColor.AQUA))
            it.addEnchant(Enchantment.LUCK, 1, false)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }
}