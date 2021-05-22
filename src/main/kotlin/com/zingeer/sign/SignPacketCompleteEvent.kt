package com.zingeer.sign

import org.bukkit.Location
import org.bukkit.entity.Player

class SignPacketCompleteEvent(
    val player: Player,
    val lines: Array<String>,
    val position: Location
)