package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class Scenario(
    val title: String,
    val difficulty: Difficulty,
    val description: String,
    val decisions: List<ScenarioDecision>,
    val battleInfo: BattleInfo? = null
)