package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class ScenarioDecision(
    val text: String,
    val successChance: Double = 1.0,
    val successResult: ScenarioDecisionResult? = null,
    val failureResult: ScenarioDecisionResult? = null
)