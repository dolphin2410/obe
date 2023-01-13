package me.myeolchi.obe

import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import io.github.monun.tap.fake.FakeEntityServer
import me.myeolchi.obe.core.CoolTimeManager
import me.myeolchi.obe.core.SkillManager
import me.myeolchi.obe.listeners.*
import me.myeolchi.obe.util.Items
import me.myeolchi.obe.util.Sangsu
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class ObePlugin: JavaPlugin(), Listener {
    companion object {
        lateinit var instance: ObePlugin
            private set

        lateinit var skillsManager: SkillManager
    }

    lateinit var fakeServer: FakeEntityServer

    override fun onEnable() {
        fakeServer = FakeEntityServer.create(this)
        server.scheduler.runTaskTimer(this, fakeServer::update, 0, 1)
        instance = this

        skillsManager = SkillManager()

        Bukkit.getOnlinePlayers().forEach {
            fakeServer.addPlayer(it)
        }

        BaconPotato().apply()
        Gorgonzola().apply()
        Peperoni().apply()
        Shrimp().apply()
        Pineapple().apply()
        Bulgogi().apply()

        kommand {
            register("sangsu") {
                then("set") {
                    then("sangsu" to string()) {
                        executes {
                            val sangsu: String by it
                        }
                    }
                }
            }

            register("obe") {
                then("reset") {
                    then("id" to string().apply { suggests(CoolTimeManager.suggestions()) }) {
                        executes {
                            val id: String by it
                            CoolTimeManager.reset(player, id)
                        }
                    }

                    executes {
                        CoolTimeManager.reset(player, null)
                    }
                }
            }
        }

        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        fakeServer.addPlayer(e.player)
        if (!e.player.inventory.contains(Items.book)) {
            e.player.inventory.addItem(Items.book)
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        fakeServer.removePlayer(e.player)
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        CoolTimeManager.reset(e.player, null)
    }
}