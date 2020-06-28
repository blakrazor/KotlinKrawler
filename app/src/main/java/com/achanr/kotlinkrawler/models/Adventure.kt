package com.achanr.kotlinkrawler.models

data class Adventure(
    val player: Player,
    val scenario: Scenario,
    val eventLog: List<AdventureEvent>
)