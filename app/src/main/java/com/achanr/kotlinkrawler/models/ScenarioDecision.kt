package com.achanr.kotlinkrawler.models

data class ScenarioDecision(
    val text: String,
    val successChance: Double,
    val successResult: ScenarioDecisionResult?,
    val failureResult: ScenarioDecisionResult?
)