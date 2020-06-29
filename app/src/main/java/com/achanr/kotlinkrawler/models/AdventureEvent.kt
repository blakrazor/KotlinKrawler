package com.achanr.kotlinkrawler.models

data class AdventureEvent(val mainText: String, val type: Type = Type.Neutral) {
    enum class Type(val hexColor: String) {
        Good("#FF4CAF50"),
        Bad("#FFE91E63"),
        Gold("#FFE65100"),
        Neutral("#8A000000")
    }
}