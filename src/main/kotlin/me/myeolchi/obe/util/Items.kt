package me.myeolchi.obe.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.textOfChildren
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

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

    val pineapplePickaxe = ItemStack(Material.IRON_PICKAXE).apply {
        editMeta {
            it.displayName(Component.text("Pickaxe of Pineapple", NamedTextColor.AQUA))
            it.addEnchant(Enchantment.LUCK, 1, false)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    val bulgogiShovel = ItemStack(Material.DIAMOND_SHOVEL).apply {
        editMeta {
            it.displayName(Component.text("Shovel of Bulgogi", NamedTextColor.AQUA))
            it.addEnchant(Enchantment.LUCK, 1, false)
            it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }
    
    val book = ItemStack(Material.WRITTEN_BOOK).apply { 
        editMeta { 
            it as BookMeta
            it.title = "설명서"
            it.author = "dolphin2410"
            it.title(text("설명서"))
                .author(text("dolphin2410"))
                .pages(
                    textOfChildren(
                        text("1. Axe of Gorgonzola\n\n"),
                        text("영혼과 신체를 분리시킵니다")
                    ),
                    textOfChildren(
                        text("2. Sword of Peperoni\n\n"),
                        text("번쩍번쩍 번개따라 찌리찌리 짜라짜라")
                    ),
                    textOfChildren(
                        text("3. Hoe of Bacon Potato\n\n"),
                        text("부매랑 폭탄")
                    ),
                    textOfChildren(
                        text("4. Shovel of Shrimp\n\n"),
                        text("부비트랩")
                    ),
                    textOfChildren(
                        text("5. Shovel of Bulgogi\n\n"),
                        text("지구 종말 지진 발생 장치")
                    ),
                    textOfChildren(
                        text("6. Pickaxe of Pineapple\n\n"),
                        text("불사의 표창")
                    )
                )
        }
    }
}