package com.achanr.kotlinkrawler.models

class ScenarioDecision(
    val text: String,
    val successChance: Double,
    val successResult: ScenarioDecisionResult,
    val failureResult: ScenarioDecisionResult
)