package com.achanr.kotlinkrawler.models

enum class ScenarioType(val value: Int, val filename: String) {
    FirstAge(0, "FirstAgeScenarios.json");

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}