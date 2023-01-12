package me.myeolchi.obe.core

import me.myeolchi.obe.ObePlugin
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class SkillManager {
    private val skills = ArrayList<Skill>()

    fun register(skill: Skill) {
        Bukkit.getServer().pluginManager.registerEvents(skill as Listener, ObePlugin.instance)
        skills.add(skill)
    }
}