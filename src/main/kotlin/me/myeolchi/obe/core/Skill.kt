package me.myeolchi.obe.core

import me.myeolchi.obe.ObePlugin

interface Skill {
    fun apply() {
        ObePlugin.skillsManager.register(this)
    }
}