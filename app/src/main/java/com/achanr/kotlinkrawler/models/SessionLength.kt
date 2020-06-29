package com.achanr.kotlinkrawler.models

enum class SessionLength(val value: Int) {
    Short(10),
    Medium(20),
    Long(30);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}