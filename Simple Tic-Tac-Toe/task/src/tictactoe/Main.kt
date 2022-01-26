package tictactoe

import kotlin.math.abs

const val CROSS = 'X'
const val ZERO = 'O'
const val EMPTY = '_'
const val GAME_IS_NOT_FINISHED = "Game not finished"

fun main() = readGridState()
    .let(::stateToGrid)
    .let(::startGame)
    .let(::println)

fun readGridState() = EMPTY.toString().repeat(9)

fun stateToGrid(state: String) = listOf(
    state.subSequence(0, 3).toMutableList(),
    state.subSequence(3, 6).toMutableList(),
    state.subSequence(6, 9).toMutableList(),
)

fun startGame(grid: List<MutableList<Char>>, moveSymbol: Char = CROSS): String = grid
    .also(::printGrid)
    .also { promptMakeMove(grid, moveSymbol) }
    .let(::getGameState)
    .let {
        if (it == GAME_IS_NOT_FINISHED) {
            startGame(grid, if (moveSymbol == CROSS) ZERO else CROSS)
        } else {
            printGrid(grid)
            it
        }
    }

fun printGrid(grid: List<MutableList<Char>>) {
    val gridBorder = "---------"
    println(gridBorder)
    grid.forEach { println("| ${it.joinToString(" ")} |") }
    println(gridBorder)
}

fun promptMakeMove(grid: List<MutableList<Char>>, moveSymbol: Char) {
    try {
        print("Enter the coordinates: ")
        val (x, y) = readLine()!!.split(" ").map { it.toInt() - 1 }
        if (y in grid.indices && x in grid.indices) {
            when (grid[x][y]) {
                CROSS, ZERO -> {
                    println("This cell is occupied! Choose another one!")
                    promptMakeMove(grid, moveSymbol)
                }
                else -> grid[x][y] = moveSymbol
            }
        } else {
            println("Coordinates should be from 1 to 3!")
            promptMakeMove(grid, moveSymbol)
        }
    } catch (e: NumberFormatException) {
        println("You should enter numbers!")
        promptMakeMove(grid, moveSymbol)
    }
}

fun getGameState(grid: List<List<Char>>) = when {
    isImpossibleState(grid) -> "Impossible"
    isThreeInRowOf(grid, CROSS) -> "$CROSS wins"
    isThreeInRowOf(grid, ZERO) -> "$ZERO wins"
    grid.flatten().none { char -> char == EMPTY } -> "Draw"
    else -> GAME_IS_NOT_FINISHED
}

fun isImpossibleState(grid: List<List<Char>>) = isThreeInRowOf(grid, CROSS)
        && isThreeInRowOf(grid, ZERO)
        || grid.flatten().run { abs(count { it == CROSS } - count { it == ZERO }) >= 2 }

fun isThreeInRowOf(grid: List<List<Char>>, symbol: Char) = isThreeInRow(grid, symbol)
        || isThreeInColumn(grid, symbol)
        || isThreeInDiagonal(grid, symbol)

fun isThreeInRow(grid: List<List<Char>>, symbol: Char) =
    grid.any { it.all { char -> char == symbol } }

fun isThreeInColumn(grid: List<List<Char>>, symbol: Char) =
    grid.indices.any { j -> grid.indices.all { i -> grid[i][j] == symbol } }

fun isThreeInDiagonal(grid: List<List<Char>>, symbol: Char) = grid.indices.all { grid[it][it] == symbol }
        || grid.indices.all { grid[grid.lastIndex - it][it] == symbol }

