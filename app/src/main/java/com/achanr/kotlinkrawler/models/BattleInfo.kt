package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class BattleInfo(
    val name: String,
    val attack: Int,
    val defense: Int,
    val health: Int,
    val reward: Int,
    val successMessage: String,
    val failureMessage: String,
    val strongAttackMessages: List<String>,
    val weakAttackMessages: List<String>,
    val strongDefenseMessages: List<String>,
    val weakDefenseMessages: List<String>
)