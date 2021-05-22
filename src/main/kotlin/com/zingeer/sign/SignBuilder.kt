package com.zingeer.sign

import com.zingeer.sign.SignManager.BUILDERS_MAP
import com.zingeer.sign.utils.Reflection
import com.zingeer.sign.utils.handle
import com.zingeer.sign.utils.sendPacket
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class SignBuilder(
    val lines: Array<String>,
    val texture: SignTexture,
    val builder: SignPacketCompleteEvent.() -> Unit
) {
    fun open(player: Player): SignBuilder {
        BUILDERS_MAP[player.uniqueId] = this

        val packetOpenSignEditorClass = Reflection.getNMSClass("PacketPlayOutOpenSignEditor")

        val blockPositionClass = Reflection.getNMSClass("BlockPosition")
        val blockPosition = blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE)
            .newInstance(player.location.blockX, 1, player.location.blockZ)

        val packetOpenSignEditor =
            packetOpenSignEditorClass.getConstructor(blockPositionClass).newInstance(blockPosition)

        val sign = Reflection.getNMSClass("TileEntitySign").getConstructor().newInstance()

        sign.javaClass.getMethod("setPosition", blockPositionClass).invoke(sign, blockPosition)
        val craftSign = Reflection.getOBCClass("block.CraftSign")

        val components = craftSign.getMethod("sanitizeLines", Array<String>::class.java)
            .invoke(craftSign, lines)

        val signLines = sign.javaClass.getField("lines").get(sign)
        System.arraycopy(components, 0, signLines, 0, (signLines as Array<*>).size)

        val packetBlockChangeClass = Reflection.getNMSClass("PacketPlayOutBlockChange")
        val worldServerClass = Reflection.getNMSClass("IBlockAccess")

        val worldHandler = player.world.handle

        val craftMagicNumber = Reflection.getOBCClass("util.CraftMagicNumbers")

        val packetBlockChange = packetBlockChangeClass.getConstructor(worldServerClass, blockPositionClass)
            .newInstance(worldHandler, blockPosition).apply {
                javaClass.getField("block").set(
                    this, craftMagicNumber.getMethod("getBlock", Material::class.java, Byte::class.java)
                        .invoke(this, texture.material, 0.toByte())
                )
            }

        player.sendPacket(packetBlockChange)
        player.sendPacket(sign.javaClass.getMethod("getUpdatePacket").invoke(sign))
        player.sendPacket(packetOpenSignEditor)

        return this
    }
}