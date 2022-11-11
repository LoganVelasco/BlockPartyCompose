package logan.blockpartycompose.data.models

import kotlinx.serialization.Serializable
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet


data class LevelInfo(
    val name: String
)

data class LevelsDTO(
    val levels: List<LevelDTO>
) {

    companion object {
        fun getDTO(levels: List<Level>):LevelsDTO{
            return LevelsDTO(levels.map { LevelDTO.getDTO(it) })
        }
    }

    fun convertToLevels(): List<Level> {
        return levels.map { it.convertToLevel() }
    }
}

data class LevelDTO(
    val id: Int,
    val name: String,
    val levelSet: String,
    val x: Int,
    val y: Int,
    val initialBlocks: String,
    val minMoves: Int
) {

    companion object {
        fun getDTO(level: Level): LevelDTO {
            return LevelDTO(
                level.id,
                level.name,
                level.levelSet.name,
                level.x,
                level.y,
                level.initialBlocks.toString(),
                level.minMoves,
            )
        }
    }


    fun convertToLevel(): Level {
        return Level(
            id,
            name,
            LevelSet.valueOf(levelSet.uppercase()),
            x,
            y,
            initialBlocks.filter { it != ','  && it != '[' && it != ']' && it != ' ' }.toList(),
            minMoves
        )
    }

}

@Serializable
data class Level(
    var id: Int,
    var name: String,
    val levelSet: LevelSet,
    val x: Int,
    val y: Int,
    val initialBlocks: List<Char>,
    val minMoves: Int
) {
    var state: GameState = GameState.IN_PROGRESS
    var blocks = initialBlocks.toMutableList()
    var movesUsed = 0
    var playerIndex: Int = initialBlocks.indexOf('p')
//        get() { return blocks.indexOf('p') } <- doesn't work, this needs to be manually updated to avoid new moves effecting ongoing ones
var enemyIndex: Int = initialBlocks.indexOf('e')
//        get() { return blocks.indexOf('e') }
    var goalIndex: Int = initialBlocks.indexOf('g')

    fun resetLevel() {
        state = GameState.IN_PROGRESS
        blocks = initialBlocks.toMutableList()
        movesUsed = 0
        playerIndex = initialBlocks.indexOf('p')
        enemyIndex = initialBlocks.indexOf('e')
    }

}

@Serializable
data class Block(
    var color: BlockColor,
    val index: Int,
)

enum class BlockColor(val color: Char) {
    BLUE('p'),
    RED('e'),
    YELLOW('g'),
    GREEN('m'),
    BLACK('x'),
    GRAY('.'),
}