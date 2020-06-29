package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class ScenarioDecisionResult(
    val text: String,
    val changeInGold: Int,
    val isBattle: Boolean = false
)