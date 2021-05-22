package com.zingeer.sign

import com.comphenix.tinyprotocol.TinyProtocol
import com.zingeer.sign.utils.Reflection
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.collections.HashMap

object SignManager {

    val BUILDERS_MAP = HashMap<UUID, SignBuilder>()
    lateinit var protocol: TinyProtocol

    fun initialization(plugin: Plugin) {
        val packetUpdateSign = Reflection.getNMSClass("PacketPlayInUpdateSign")

        protocol = object : TinyProtocol(plugin) {
            override fun onPacketInAsync(sender: Player?, packet: Any): Any {
                if (sender != null) {
                    if (packet.javaClass == packetUpdateSign) {
                        val blockPosition = packetUpdateSign.getMethod("b").invoke(packet)
                        val position = Location(
                            sender.world,
                            (blockPosition.javaClass.getMethod("getX").invoke(blockPosition) as Int).toDouble(),
                            (blockPosition.javaClass.getMethod("getY").invoke(blockPosition) as Int).toDouble(),
                            (blockPosition.javaClass.getMethod("getZ").invoke(blockPosition) as Int).toDouble()
                        )

                        BUILDERS_MAP[sender.uniqueId].apply {
                            this ?: return@apply
                            builder(
                                SignPacketCompleteEvent(
                                    sender,
                                    packetUpdateSign.getMethod("c").invoke(packet) as Array<String>,
                                    position
                                )
                            )
                            sender.sendBlockChange(position, position.block.blockData)
                            BUILDERS_MAP.remove(sender.uniqueId)
                        }
                    }
                }
                return packet
            }
        }
    }
}