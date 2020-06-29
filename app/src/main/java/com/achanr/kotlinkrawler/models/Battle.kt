package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class Battle(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val attackChance: Double,
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