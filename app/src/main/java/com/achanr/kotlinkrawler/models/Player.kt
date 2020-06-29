package com.achanr.kotlinkrawler.models

data class Player(
    val maxHealth: Int,
    val attack: Int,
    val defense: Int,
    var goldCount: Int,
    var health: Int = maxHealth
) {
    companion object {
        fun newPlayer(difficulty: Difficulty): Player {
            return when (difficulty) {
                Difficulty.VeryEasy -> Player(150, 10, 10, 20)
                Difficulty.Easy -> Player(125, 7, 7, 15)
                Difficulty.Medium -> Player(100, 5, 5, 10)
                Difficulty.Hard -> Player(75, 3, 3, 5)
                Difficulty.VeryHard -> Player(50, 1, 1, 0)
            }
        }
    }
}