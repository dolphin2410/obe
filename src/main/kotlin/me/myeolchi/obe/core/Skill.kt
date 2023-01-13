package me.myeolchi.obe.core

import me.myeolchi.obe.ObePlugin
import org.bukkit.inventory.ItemStack

interface Skill {
    fun skillItem(): ItemStack

    fun apply() {
        ObePlugin.skillsManager.register(this)
    }
}