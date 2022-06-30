package logan.blockpartycompose.data.models

import kotlinx.serialization.Serializable
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet


data class LevelInfo(
    val name: String
)

@Serializable
data class Level(
    val name: Int,
    val levelSet: LevelSet,
    val x: Int,
    val y: Int,
    val initialBlocks: List<Char>,
    val minMoves: Int
) {
    var blueIndex: Int = initialBlocks.indexOf('b')
    var redIndex: Int = initialBlocks.indexOf('r')
    var state: GameState = GameState.IN_PROGRESS
    var blocks = initialBlocks.toMutableList()

    fun resetLevel(){
        blueIndex = initialBlocks.indexOf('b')
        redIndex = initialBlocks.indexOf('r')
        blocks = initialBlocks.toMutableList()
        state = GameState.IN_PROGRESS
    }

}

@Serializable
data class Block(
    var color: BlockColor,
    val index: Int,
)

enum class BlockColor(val color: Char) {
    BLUE('b'),
    RED('r'),
    YELLOW('y'),
    GREEN('g'),
    BLACK('x'),
    GRAY('.'),
}