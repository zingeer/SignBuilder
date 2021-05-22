package com.zingeer.sign.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object Reflection {

    private val version = Bukkit.getServer().javaClass.getPackage().name.replace(".", ",").split(",")[3]

    fun getNMSClass(name: String): Class<*> = Class.forName("net.minecraft.server.$version.$name")

    fun getOBCClass(name: String): Class<*> = Class.forName("org.bukkit.craftbukkit.$version.$name")

    fun getConnectionPlayer(player: Player): Any {
        val getHandle = player.javaClass.getMethod("getHandle")
        val nmsPlayer: Any = getHandle.invoke(player)
        val conField = nmsPlayer.javaClass.getField("playerConnection")
        return conField.get(nmsPlayer)
    }

}