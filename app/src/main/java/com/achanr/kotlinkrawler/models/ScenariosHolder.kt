package com.achanr.kotlinkrawler.models

import kotlinx.serialization.Serializable

@Serializable
data class ScenariosHolder(val type: String, val scenarios: List<Scenario>, val battles: List<Battle>)