package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class ScenarioDecision(
    val text: String,
    val successChance: Double,
    val successResult: ScenarioDecisionResult? = null,
    val failureResult: ScenarioDecisionResult? = null
)