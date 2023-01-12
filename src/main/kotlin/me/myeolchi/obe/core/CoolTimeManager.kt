package me.myeolchi.obe.core

import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.kommand.KommandContext
import io.github.monun.kommand.KommandSuggestion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import kotlin.math.roundToLong

object CoolTimeManager {
    private val cooltime = HashMap<String, HashMap<Player, Long>>()

    fun suggestions(): KommandSuggestion.(KommandContext) -> Unit = {
        suggest(cooltime.keys)
    }

    fun reset(p: Player, id: String?) {
        if (id != null) {
            cooltime[id]!![p] = 0
        } else {
            cooltime.forEach { (_, data) ->
                data[p] = 0
            }

        }
    }

    fun useItem(id: String, player: Player, interval: Long): Boolean {
        val map = cooltime.computeIfAbsent(id) { HashMap() }
        return if (map.containsKey(player)) {
            val lastTime = map[player]!!
            val current = System.currentTimeMillis()
            if (current - lastTime > interval) {
                map[player] = current
                true
            } else {
                HeartbeatScope().launch {
                    player.sendActionBar(
                        text(
                            "${
                                ((interval - current + map[player]!!) / 1000.0).roundToLong()
                            } seconds left to reuse", NamedTextColor.RED
                        )
                    )

                    delay(3 * 1000)
                    player.sendActionBar(text())
                }
                false
            }
        } else {
            map[player] = System.currentTimeMillis()
            true
        }
    }
}