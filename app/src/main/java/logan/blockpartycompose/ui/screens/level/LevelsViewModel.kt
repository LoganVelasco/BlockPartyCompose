package logan.blockpartycompose.ui.screens.level

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils
import logan.blockpartycompose.utils.GameUtils.Companion.getDirection
import logan.blockpartycompose.utils.GameUtils.Companion.isAbove
import logan.blockpartycompose.utils.GameUtils.Companion.isInSameColumn
import logan.blockpartycompose.utils.GameUtils.Companion.isInSameRow
import logan.blockpartycompose.utils.GameUtils.Companion.isRightOf
import logan.blockpartycompose.utils.GameUtils.Companion.isValidEnemyMove
import logan.blockpartycompose.utils.GameUtils.Companion.shouldEnemyAttemptMove
import javax.inject.Inject
import kotlin.math.absoluteValue


@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    lateinit var level: Level
    var isTutorialMode = false

    private val playerBlock = 'p'
    private val enemyBlock = 'e'
    private val movableBlock = 'm'
    private val goalBlock = 'g'
    private val emptyBlock = '.'

    fun setupLevel(levelSet: LevelSet, id: Int) {
        level = getLevel(levelSet, id)
        level.resetLevel()
        _state.postValue(
            LevelState(
                blocks = level.initialBlocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

    fun setupLevel(newLevel: Level) {
        level = newLevel
        _state.postValue(
            LevelState(
                blocks = newLevel.blocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

    private fun getLevel(levelSet: LevelSet, id: Int): Level {
        return repo.levelsSets[levelSet.name]!!.first { it.id == id }
    }

    fun updateLevel(difficulty: LevelSet, name: Int, stars: Int) {
        repo.updateLevelProgress(difficulty, name, stars)
    }

    fun blockClicked(block: Char, index: Int) {
        if (!GameUtils.isTouchingPlayer(
                index,
                level.playerIndex,
                level.x
            )
        ) {
            return
        }
        when (block) {
            enemyBlock -> {
                level.state = GameState.FAILED
                _state.postValue(
                    LevelState(
                        blocks = level.blocks,
                        movesUsed = _state.value!!.movesUsed + 1,
                        gameState = level.state,
                    )
                )
            }
            movableBlock -> {
                if (handleMovableBlockMove(index)) {
                    val direction = movePlayerBlock(index)

                    _state.postValue(
                        LevelState(
                            blocks = level.blocks,
                            movesUsed = _state.value!!.movesUsed + 1,
                            gameState = level.state,
                            direction = direction
                        )
                    )
                } else {
                    return
                }
            }
            goalBlock -> {
                val direction = movePlayerBlock(index)
                _state.postValue(
                    LevelState(
                        blocks = level.blocks,
                        movesUsed = _state.value!!.movesUsed + 1,
                        gameState = level.state,
                        direction = direction
                    )
                )
                viewModelScope.launch {
                    delay(200)
                    level.blocks[index] = goalBlock
                    _state.postValue(
                        LevelState(
                            blocks = level.blocks,
                            movesUsed = _state.value!!.movesUsed,
                            gameState = level.state,
                            direction = direction
                        )
                    )
                    delay(400)
                    if (level.state != GameState.FAILED)
                        level.state = GameState.SUCCESS
                    _state.postValue(
                        LevelState(
                            blocks = level.blocks,
                            movesUsed = _state.value!!.movesUsed,
                            gameState = level.state,
                            direction = direction
                        )
                    )

                }
                return
            }
            emptyBlock -> {
                val direction = movePlayerBlock(index)!!
                _state.postValue(
                    LevelState(
                        blocks = level.blocks,
                        movesUsed = _state.value!!.movesUsed + 1,
                        gameState = level.state,
                        direction = direction
                    )
                )
            }
            else -> {
                return
            }
        }


        if (shouldEnemyAttemptMove(level.enemyIndex, level.state)) {
            handleEnemyTurn()
        }
    }

    private fun handleEnemyTurn() {
        var moveDirection = moveEnemyBlock()
        if (moveDirection != null && level.state == GameState.IN_PROGRESS) {
            viewModelScope.launch {
                delay(100)
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state,
                        direction = moveDirection
                    )
                )

                if (level.state != GameState.IN_PROGRESS) return@launch

                moveDirection = moveEnemyBlock()
                if (moveDirection != null) {
                    delay(175)
                    _state.postValue(
                        LevelState(
                            blocks = level.blocks.toMutableList(),
                            movesUsed = _state.value!!.movesUsed,
                            gameState = level.state,
                            direction = moveDirection
                        )
                    )
                }
            }
        }
    }

    private fun movePlayerBlock(index: Int): Direction? {
        level.blocks[index] = playerBlock
        level.blocks[level.playerIndex] = emptyBlock
        val direction = getDirection(level.playerIndex, index, level.x)
        level.playerIndex = index
        return direction
    }

    private fun handleMovableBlockMove(index: Int): Boolean {
        val difference = (index - level.playerIndex)
        val newIndex = index + difference

        if (newIndex < 0 || newIndex > level.blocks.size - 1) return false

        if (GameUtils.isEdge(index, level.x) && GameUtils.isEdge(
                newIndex,
                level.x
            ) && difference.absoluteValue == 1
        ) return false

        return when (level.blocks[newIndex]) {
            emptyBlock -> {
                level.blocks[newIndex] = movableBlock
                true
            }
            movableBlock -> {
                level.blocks[newIndex] = emptyBlock
                true
            }
            else -> {
                false
            }
        }
    }

    private fun moveEnemyBlock(): Direction? {
        if (isInSameColumn(level.playerIndex, level.enemyIndex, level.x)) {
            return if (isAbove(level.enemyIndex, level.playerIndex)) {
                if (!moveEnemyBlock(Direction.UP)) {
                    null // Unable to move up return null to signify end of enemy turn
                } else Direction.UP
            } else {
                if (!moveEnemyBlock(Direction.DOWN)) {
                    null // Unable to move down return null to signify end of enemy turn
                } else Direction.DOWN
            }
        } else if (isRightOf(level.enemyIndex, level.playerIndex, level.x)) {
            if (!moveEnemyBlock(Direction.LEFT)) {
                if (isInSameRow(level.playerIndex, level.enemyIndex, level.x)) {
                    return null // Unable to left move return null to signify end of enemy turn
                }
                return if (isAbove(level.enemyIndex, level.playerIndex)) {
                    if (!moveEnemyBlock(Direction.UP)) {
                        null // Unable to move up return null to signify end of enemy turn
                    } else Direction.UP
                } else {
                    if (!moveEnemyBlock(Direction.DOWN)) {
                        null // Unable to move down return null to signify end of enemy turn
                    } else Direction.DOWN
                }
            } else return Direction.LEFT
        } else {
            return if (!moveEnemyBlock(Direction.RIGHT)) {
                if (isInSameRow(level.playerIndex, level.enemyIndex, level.x)) {
                    null // Unable to move right return null to signify end of enemy turn
                } else
                    if (isAbove(level.enemyIndex, level.playerIndex)) {
                        if (!moveEnemyBlock(Direction.UP)) {
                            null // Unable to move up return null to signify end of enemy turn
                        } else Direction.UP
                    } else {
                        if (!moveEnemyBlock(Direction.DOWN)) {
                            null // Unable to move down return null to signify end of enemy turn
                        } else Direction.DOWN
                    }
            } else Direction.RIGHT
        }

    }


    private fun moveEnemyBlock(direction: Direction): Boolean {
        val newIndex = when (direction) {
            Direction.UP -> {
                level.enemyIndex - level.x
            }
            Direction.DOWN -> {
                level.enemyIndex + level.x
            }
            Direction.LEFT -> {
                level.enemyIndex - 1
            }
            Direction.RIGHT -> {
                level.enemyIndex + 1
            }
        }


        if (newIndex == level.playerIndex) { // Red reached Blue
            viewModelScope.launch {
                moveEnemyToNewIndex(newIndex)
                delay(250)
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )
                )
                delay(250)
                level.state = GameState.FAILED
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = _state.value!!.movesUsed,
                        gameState = level.state
                    )
                )
            }
            return false
        }


        if (level.enemyIndex == level.goalIndex) { // Red moves off of Yellow
            moveEnemyOffGoal(newIndex)
            return true
        }

        if (isValidEnemyMove(level.blocks[newIndex])) {
            moveEnemyToNewIndex(newIndex)
            return true
        }
        return false
    }

    private fun moveEnemyOffGoal(newIndex: Int) {
        level.blocks[level.enemyIndex] = goalBlock
        level.blocks[newIndex] = enemyBlock
        level.enemyIndex = newIndex
    }

    private fun moveEnemyToNewIndex(newIndex: Int) {
        level.blocks[level.enemyIndex] = emptyBlock
        level.blocks[newIndex] = enemyBlock
        level.enemyIndex = newIndex
    }


    fun tryAgain() {
        level.resetLevel()
        _state.postValue(
            LevelState(
                blocks = level.blocks,
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS
            )
        )
    }

    private fun getMinMoves(): Int {
        return level.minMoves
    }

    fun getStars(movesUsed: Int): Int {
        return if (movesUsed <= getMinMoves()) 3 else if (movesUsed - 2 <= getMinMoves()) 2 else 1
    }
}

@Immutable
data class LevelState(
    val blocks: List<Char> = emptyList(),
    var movesUsed: Int = 0,
    var gameState: GameState,
    var direction: Direction? = null
)

enum class GameState {
    FAILED,
    SUCCESS,
    IN_PROGRESS
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}