package me.artofluxis.game.game.save

import kotlinx.serialization.Serializable

@Serializable
class SaveData(
    val selectedJARMod: String,
    val selectedDataMod: String,
    val selectedResourceMod: String
)
