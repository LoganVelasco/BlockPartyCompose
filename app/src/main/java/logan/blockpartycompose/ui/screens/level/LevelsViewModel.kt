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
import logan.blockpartycompose.utils.GameUtils.Companion.isValidRedMove
import logan.blockpartycompose.utils.GameUtils.Companion.shouldRedAttemptMove
import javax.inject.Inject
import kotlin.math.absoluteValue


@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    lateinit var level: Level

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

//    private fun getLevels(levelSet: LevelSet, context:Context): List<Level> {
//        return repo.getLevels(levelSet, context)
//    }

    private fun getLevel(levelSet: LevelSet, id: Int): Level {
        return repo.levelsSets[levelSet.name]!!.first { it.id == id }
    }

    fun updateLevel(difficulty: LevelSet, name: Int, stars: Int) {
        repo.updateLevelProgress(difficulty, name, stars)
    }

    fun blockClicked(block: Char, index: Int) {
        if (!GameUtils.isTouchingBlue(
                index,
                level.blueIndex,
                level.x
            )
        ) {
            return
        }
        when (block) {
            'r' -> {
                level.state = GameState.FAILED
                _state.postValue(
                    LevelState(
                        blocks = level.blocks,
                        movesUsed = _state.value!!.movesUsed + 1,
                        gameState = level.state,
                    )
                )
            }
            'g' -> {
                if (handleGreenMove(index)) {
                    val direction = moveBlue(index)

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
            'y' -> {
                viewModelScope.launch {
                    delay(450)
                    if (level.state != GameState.FAILED)
                        moveBlue(index)
                        level.state = GameState.SUCCESS

                    _state.postValue(
                        LevelState(
                            blocks = level.blocks,
                            movesUsed = _state.value!!.movesUsed + 1,
                            gameState = level.state,
                        )
                    )
                }
                return
            }
            '.' -> {
                val direction = moveBlue(index)!!
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


        if (shouldRedAttemptMove(level.redIndex, level.state)) {
            handleRedTurn()
        }
    }

    private fun handleRedTurn() {
        var moveDirection = moveRed()
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

                moveDirection = moveRed()
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

    private fun moveBlue(index: Int): Direction? {
        level.blocks[index] = 'b'
        level.blocks[level.blueIndex] = '.'
        val direction = getDirection(level.blueIndex, index, level.x)
        level.blueIndex = index
        return direction
    }

    private fun handleGreenMove(index: Int): Boolean {
        val difference = (index - level.blueIndex)
        val newIndex = index + difference

        if (newIndex < 0 || newIndex > level.blocks.size - 1) return false

        if (GameUtils.isEdge(index, level.x) && GameUtils.isEdge(
                newIndex,
                level.x
            ) && difference.absoluteValue == 1
        ) return false

        return when (level.blocks[newIndex]) {
            '.' -> {
                level.blocks[newIndex] = 'g'
                true
            }
            'g' -> {
                level.blocks[newIndex] = '.'
                true
            }
            else -> {
                false
            }
        }
    }

    private fun moveRed(): Direction? {
        if (isInSameColumn(level.blueIndex, level.redIndex, level.x)) {
            return if (isAbove(level.redIndex, level.blueIndex)) {
                if (!moveRed(Direction.UP)) {
                    null // Unable to move up return null to signify end of red turn
                } else Direction.UP
            } else {
                if (!moveRed(Direction.DOWN)) {
                    null // Unable to move down return null to signify end of red turn
                } else Direction.DOWN
            }
        } else if (isRightOf(level.redIndex, level.blueIndex, level.x)) {
            if (!moveRed(Direction.LEFT)) {
                if (isInSameRow(level.blueIndex, level.redIndex, level.x)) {
                    return null // Unable to left move return null to signify end of red turn
                }
                return if (isAbove(level.redIndex, level.blueIndex)) {
                    if (!moveRed(Direction.UP)) {
                        null // Unable to move up return null to signify end of red turn
                    } else Direction.UP
                } else {
                    if (!moveRed(Direction.DOWN)) {
                        null // Unable to move down return null to signify end of red turn
                    } else Direction.DOWN
                }
            } else return Direction.LEFT
        } else {
            return if (!moveRed(Direction.RIGHT)) {
                if (isInSameRow(level.blueIndex, level.redIndex, level.x)) {
                    null // Unable to move right return null to signify end of red turn
                } else
                    if (isAbove(level.redIndex, level.blueIndex)) {
                        if (!moveRed(Direction.UP)) {
                            null // Unable to move up return null to signify end of red turn
                        } else Direction.UP
                    } else {
                        if (!moveRed(Direction.DOWN)) {
                            null // Unable to move down return null to signify end of red turn
                        } else Direction.DOWN
                    }
            } else Direction.RIGHT
        }

    }


    private fun moveRed(direction: Direction): Boolean {
        val newIndex = when (direction) {
            Direction.UP -> {
                level.redIndex - level.x
            }
            Direction.DOWN -> {
                level.redIndex + level.x
            }
            Direction.LEFT -> {
                level.redIndex - 1
            }
            Direction.RIGHT -> {
                level.redIndex + 1
            }
        }


        if (newIndex == level.blueIndex) { // Red reached Blue
            viewModelScope.launch {
                moveRedToNewIndex(newIndex)
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


        if (level.redIndex == level.goalIndex) { // Red moves off of Yellow
            moveRedOffYellow(newIndex)
            return true
        }

        if (isValidRedMove(level.blocks[newIndex])) {
            moveRedToNewIndex(newIndex)
            return true
        }
        return false
    }

    private fun moveRedOffYellow(newIndex: Int) {
        level.blocks[level.redIndex] = 'y'
        level.blocks[newIndex] = 'r'
        level.redIndex = newIndex
    }

    private fun moveRedToNewIndex(newIndex: Int) {
        level.blocks[level.redIndex] = '.'
        level.blocks[newIndex] = 'r'
//        level.lastDirection = getDirection(level.redIndex, newIndex, level.x)
        level.redIndex = newIndex
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