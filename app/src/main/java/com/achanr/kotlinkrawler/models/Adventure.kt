package com.achanr.kotlinkrawler.models

data class Adventure(
    val seed: Int,
    val player: Player,
    val scenario: Scenario,
    val battle: Battle? = null,
    val decisions: List<ScenarioDecision>,
    val eventLog: List<AdventureEvent>
)