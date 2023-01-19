package logan.blockpartycompose.data.models

import kotlinx.serialization.Serializable
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils.Companion.EMPTY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.ENEMY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.GOAL_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.MOVABLE_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.PLAYER_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.UNMOVABLE_BLOCK

data class LevelsDTO(
    val levels: List<LevelDTO>
) {

    companion object {
        fun getDTO(levels: List<Level>): LevelsDTO {
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
            initialBlocks.filter { it != ',' && it != '[' && it != ']' && it != ' ' }.toList(),
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
    var playerIndex: Int = initialBlocks.indexOf(PLAYER_BLOCK)
    var enemyIndex: Int = initialBlocks.indexOf(ENEMY_BLOCK)
    var goalIndex: Int = initialBlocks.indexOf(GOAL_BLOCK)

    fun resetLevel() {
        state = GameState.IN_PROGRESS
        blocks = initialBlocks.toMutableList()
        movesUsed = 0
        playerIndex = initialBlocks.indexOf(PLAYER_BLOCK)
        enemyIndex = initialBlocks.indexOf(ENEMY_BLOCK)
    }

}

enum class BlockType(val type: Char) {
    PLAYER(PLAYER_BLOCK),
    ENEMY(ENEMY_BLOCK),
    GOAL(GOAL_BLOCK),
    MOVABLE(MOVABLE_BLOCK),
    UNMOVABLE(UNMOVABLE_BLOCK),
    EMPTY(EMPTY_BLOCK),
}