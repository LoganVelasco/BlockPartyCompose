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
import logan.blockpartycompose.utils.LevelSolver
import logan.blockpartycompose.utils.UserProgressService
import javax.inject.Inject
import kotlin.math.absoluteValue


@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val repo: DataRepository,
    private val userProgressService: UserProgressService,
) : ViewModel() {


    private var _state = MutableLiveData<GameState>()
    val state: LiveData<GameState> = _state
    private var currentState = state.value as InProgress

    private var _moves = MutableLiveData<GameState>()
    val moves: LiveData<GameState> = _moves

    val onClicks = LevelOnClicks(
        blockClicked = this::blockClicked,
        solveClicked = this::solveLevel,
        resetClicked = this::tryAgain,
    )

    private lateinit var level: Level


    fun setupLevel(levelSet: LevelSet, name: String) {
        level = getLevel(levelSet, name)

        _state.postValue(
            InProgress(
                x = level.x,
                blocks = level.blocks,
                movesUsed = 0,
            )
        )
    }

    fun setupLevel(newLevel: Level) {
        level = newLevel
        _state.postValue(
            InProgress(
                x = level.x,
                blocks = level.blocks,
                movesUsed = 0,
            )
        )
    }


    fun getLevels(levelSet: LevelSet): List<Level> {
        return repo.getLevels(levelSet)
    }

    fun getLevel(levelSet: LevelSet, name: String): Level {
        return getLevels(levelSet).first { it.name == name }
    }

    fun blockClicked(block: Char, newIndex: Int) {
        if (!GameUtils.isTouchingBlue(
                newIndex,
                level.blueIndex,
                level.x
            )
        ) {
            return
        }
        var gameState = GameStates.IN_PROGRESS
        when (block) {
            'r' -> {
                gameState = GameStates.FAILED
            }
            'g' -> {
                if (!handleGreenMove(newIndex)) {
                    return
                } else {
                    moveBlue(newIndex)
                    Thread.sleep(100)
                }
            }
            'y' -> {
                gameState = GameStates.SUCCESS
            }
            '.' -> {
                moveBlue(newIndex)
            }
            else -> {
                return
            }
        }

        updateState(
            blocks = level.blocks,
            x = currentState.x,
            movesUsed = currentState.movesUsed + 1,
            gameState = gameState,
            name = level.name,
        )

        if (shouldRedAttemptMove()) {
            handleRedTurn()
        }
    }

    private fun handleRedTurn() {
        if (moveRed() && level.state == GameStates.IN_PROGRESS) {

            viewModelScope.launch {

                delay(100)
                updateState(
                    blocks = level.blocks.toMutableList(),
                    x = level.x,
                    movesUsed = currentState.movesUsed,
                    gameState = level.state,
                    level.name,
                )


                if (level.state == GameStates.IN_PROGRESS && moveRed()) {
                    delay(250)

                    updateState(
                        blocks = level.blocks.toMutableList(),
                        x = level.x,
                        movesUsed = currentState.movesUsed,
                        gameState = level.state,
                        level.name,
                    )

                }
            }
        }
    }

    private fun moveBlue(index: Int) {
        level.blocks[index] = 'b'
        level.blocks[level.blueIndex] = '.'
        level.blueIndex = index
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
            'y' -> {
                level.blocks[newIndex] = 'y'
                true
            }
            else -> {
                false
            }
        }
    }

    private fun moveRed(): Boolean {
        if (level.redIndex % level.x == level.blueIndex % level.x) {
            if (level.redIndex > level.blueIndex) {
                if (!moveRed(Direction.UP)) return false
            } else {
                if (!moveRed(Direction.DOWN)) return false
            }
        } else if (level.redIndex % level.x > level.blueIndex % level.x) {
            if (!moveRed(Direction.LEFT)) {
                if ((level.redIndex / level.x) == (level.blueIndex / level.x)) return false
                if (level.redIndex > level.blueIndex) {
                    if (!moveRed(Direction.UP)) return false
                } else {
                    if (!moveRed(Direction.DOWN)) return false
                }
            }
        } else {
            if (!moveRed(Direction.RIGHT)) {
                if ((level.redIndex / level.x) == (level.blueIndex / level.x)) return false
                if (level.redIndex > level.blueIndex) {
                    if (!moveRed(Direction.UP)) return false
                } else {
                    if (!moveRed(Direction.DOWN)) return false
                }
            }
        }

        return true
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

        if (newIndex == level.blueIndex) {
            level.state = GameStates.FAILED

            return true
        }

        if (isValidRedMove(newIndex)) {
            if (level.blocks.contains('y')) {
                level.blocks[level.redIndex] = '.'
            } else {
                level.blocks[level.redIndex] = 'y'
            }
            level.blocks[newIndex] = 'r'
            level.redIndex = newIndex

            return true
        }
        return false
    }

    private fun isValidRedMove(newIndex: Int): Boolean {
        return when (currentState.blocks[newIndex]) {
            '.' -> true
            'b' -> true
            'y' -> true
            else -> false
        }
    }

    private fun shouldRedAttemptMove(): Boolean {
        return level.redIndex != -1 && level.state == GameStates.IN_PROGRESS
    }

    fun tryAgain() {
        level.resetLevel()

        updateState(
            blocks = level.blocks,
            x = level.x,
            movesUsed = 0,
            gameState = GameStates.IN_PROGRESS,
            name = level.name,
        )

    }

    private fun updateState(
        blocks: MutableList<Char>,
        x: Int,
        movesUsed: Int,
        gameState: GameStates,
        name: String
    ) {
        when (gameState) {
            GameStates.IN_PROGRESS -> {
                _state.postValue(
                    InProgress(
                        blocks = blocks,
                        x = x,
                        movesUsed = movesUsed,
                    )
                )
            }
            GameStates.SUCCESS -> {
                val nextLevel = name.toInt() + 1
                val levelSet = if (nextLevel >= 11) LevelSet.MEDIUM
                else LevelSet.EASY
                _state.postValue(
                    Success(
                        movesUsed = movesUsed,
                        name = name,
                        nextLevel = nextLevel.toString(),
                        newLevelSet = levelSet
                    )
                )
            }
            GameStates.FAILED -> {
                _state.postValue(Failure())
            }
        }
    }

    fun solveLevel() {
        viewModelScope.launch {
            val levelSolver = LevelSolver()
            tryAgain()
            val solution = levelSolver.findShortestSolution(level.blocks as ArrayList<Char>)
            solution?.forEach {
                blockClicked(block = level.blocks[it], it)
                delay(750)
            }
            blockClicked(block = 'y', 0)
        }

    }

    fun isHighScoreUpdated(): Boolean {
        println("TESTLOG: ${level.name} ${currentState.movesUsed ?: 0}")
        return userProgressService.isHighScoreUpdated(level.name, currentState.movesUsed ?: 0)
    }


}


interface GameState

@Immutable
class InProgress(
    val blocks: List<Char>,
    val x: Int,
    var movesUsed: Int,
) : GameState

@Immutable
class Success(
    var movesUsed: Int,
    val name: String,
    val nextLevel: String,
    val newLevelSet: LevelSet,
) : GameState

@Immutable
class Failure : GameState

enum class GameStates {
    FAILED,
    SUCCESS,
    IN_PROGRESS
}

class LevelOnClicks(
    val blockClicked: (Char, Int) -> Unit,
    val solveClicked: () -> Unit,
    val resetClicked: () -> Unit
)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

