package com.zingeer.sign

import org.bukkit.Material

enum class SignTexture(
    val material: Material
) {
    OAK(Material.OAK_SIGN),
    BIRCH(Material.BIRCH_SIGN),
    ACACIA(Material.ACACIA_SIGN),
    CRIMSON(Material.CRIMSON_SIGN),
    DARK_OAK(Material.DARK_OAK_SIGN),
    JUNGLE(Material.JUNGLE_SIGN),
    SPRUCE(Material.SPRUCE_SIGN),
    WARPED(Material.WARPED_SIGN),
}