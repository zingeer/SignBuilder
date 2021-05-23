package com.zingeer.sign

import com.zingeer.sign.SignManager.BUILDERS_MAP
import com.zingeer.sign.utils.Reflection
import com.zingeer.sign.utils.sendPacket
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class SignBuilder(
    val lines: Array<String>,
    val texture: SignTexture = SignTexture.OAK,
    val builder: SignPacketCompleteEvent.() -> Unit = {}
) {
    fun open(player: Player): SignBuilder {
        BUILDERS_MAP[player.uniqueId] = this

        val blockPositionClass = Reflection.getNMSClass("BlockPosition")
        val blockPosition = blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE)
            .newInstance(player.location.blockX, 1, player.location.blockZ)

        player.sendBlockChange(player.location.apply { y = 1.0 }, Bukkit.createBlockData(texture.material))

        val sign = Reflection.getNMSClass("TileEntitySign").getConstructor().newInstance()

        sign.javaClass.getMethod("setPosition", blockPositionClass).invoke(sign, blockPosition)
        val craftSign = Reflection.getOBCClass("block.CraftSign")

        val components = craftSign.getMethod("sanitizeLines", Array<String>::class.java)
            .invoke(craftSign, lines)

        val signLines = sign.javaClass.getField("lines").get(sign)
        System.arraycopy(components, 0, signLines, 0, (signLines as Array<*>).size)

        val packetOpenSignEditorClass = Reflection.getNMSClass("PacketPlayOutOpenSignEditor")
        val packetOpenSignEditor =
            packetOpenSignEditorClass.getConstructor(blockPositionClass).newInstance(blockPosition)

        player.sendPacket(sign.javaClass.getMethod("getUpdatePacket").invoke(sign))
        player.sendPacket(packetOpenSignEditor)

        return this
    }
}