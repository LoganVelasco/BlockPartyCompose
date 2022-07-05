package logan.blockpartycompose.data.models

import androidx.compose.ui.text.toUpperCase
import kotlinx.serialization.Serializable
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import java.util.*


data class LevelInfo(
    val name: String
)

data class LevelsDTO(
    val levels: List<LevelDTO>
){
    fun convertToLevels(): List<Level>{
        return levels.map { it.convertToLevel() }
    }
}

data class LevelDTO(
    val id: Int,
    val name: String,
    val levelSet: String,
    val x: Int,
    val y: Int,
    val blocks: String,
    val minMoves: Int
) {
    fun convertToLevel(): Level {
        return Level(
            id,
            name,
            LevelSet.valueOf(levelSet.uppercase()),
            x,
            y,
            blocks.toList(),
            minMoves
        )
    }

}

@Serializable
data class Level(
    val id: Int,
    val name: String,
    val levelSet: LevelSet,
    val x: Int,
    val y: Int,
    val initialBlocks: List<Char>,
    val minMoves: Int
) {
    var blueIndex: Int = initialBlocks.indexOf('b')
    var redIndex: Int = initialBlocks.indexOf('r')
    var goalIndex: Int = initialBlocks.indexOf('y')
    var state: GameState = GameState.IN_PROGRESS
    var blocks = initialBlocks.toMutableList()

    fun resetLevel() {
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