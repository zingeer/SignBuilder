package com.zingeer.sign.utils

import org.bukkit.Server
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

val World.handle get() = javaClass.getMethod("getHandle").invoke(this)
val Server.handle get() = javaClass.getMethod("getHandle").invoke(this)
val Entity.handle get() = javaClass.getMethod("getHandle").invoke(this)
val Player.handle get() = javaClass.getMethod("getHandle").invoke(this)

fun Player.sendPacket(packet: Any) {
    val playerConnection = Reflection.getConnectionPlayer(this)
    playerConnection.javaClass.getMethod("sendPacket", Reflection.getNMSClass("Packet"))
        .invoke(playerConnection, packet)
}
