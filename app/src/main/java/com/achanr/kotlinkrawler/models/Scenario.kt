package com.achanr.kotlinkrawler.models

data class Scenario(
    val title: String,
    val difficulty: Difficulty,
    val description: String,
    val decisions: List<ScenarioDecision>
)