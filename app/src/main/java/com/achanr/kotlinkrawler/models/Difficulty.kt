package com.achanr.kotlinkrawler.models

enum class Difficulty(val value: Int) {
    VeryEasy(0),
    Easy(1),
    Medium(2),
    Hard(3),
    VeryHard(4);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}