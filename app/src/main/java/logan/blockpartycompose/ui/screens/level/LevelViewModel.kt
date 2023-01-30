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
import logan.blockpartycompose.utils.GameUtils.Companion.EMPTY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.ENEMY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.FINAL_LEVELS
import logan.blockpartycompose.utils.GameUtils.Companion.GOAL_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.HELP_CARD_COUNT_INITIAL
import logan.blockpartycompose.utils.GameUtils.Companion.HELP_CARD_COUNT_SECOND
import logan.blockpartycompose.utils.GameUtils.Companion.HELP_CARD_MOVE_INITIAL
import logan.blockpartycompose.utils.GameUtils.Companion.HELP_CARD_MOVE_SECOND
import logan.blockpartycompose.utils.GameUtils.Companion.MOVABLE_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.PLAYER_BLOCK
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
class LevelViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    private val history = mutableListOf<LevelState>()

    lateinit var level: Level

    private var _infoState = MutableLiveData(-1)
    val infoState: LiveData<Int> = _infoState

    fun setupLevel(levelSet: LevelSet, id: Int) {
        level = getLevel(levelSet, id)
        level.resetLevel()


        val newState = LevelState(
            blocks = level.initialBlocks,
            movesUsed = 0,
            gameState = GameState.IN_PROGRESS
        )
        _state.postValue(
            newState
        )
        history.clear()
        history.add(newState)

        if (levelSet == LevelSet.MEDIUM) {
            val cardToDisplay = cardToDisplay()
            if (cardToDisplay != 0)
                _infoState.value = cardToDisplay
            else if (_infoState.value != -1)
                _infoState.value = -1
        }

    }

    fun setupLevel(newLevel: Level) {
        level = newLevel
        val newState = LevelState(
            blocks = newLevel.blocks.toList(),
            movesUsed = 0,
            gameState = GameState.IN_PROGRESS
        )
        _state.postValue(
            newState
        )
        history.clear()
        history.add(newState)
    }

    private fun getLevel(levelSet: LevelSet, id: Int): Level {
        return repo.levelsSets[levelSet.name]!!.first { it.id == id }
    }

    private fun updateLevel(difficulty: LevelSet, id: Int, stars: Int) {
        if (difficulty == LevelSet.MEDIUM && id == 10) repo.updateTutorialStage(5)
        else if (difficulty == LevelSet.MEDIUM && id == 11) repo.updateTutorialStage(6)

        repo.updateLevelProgress(difficulty, id, stars)
    }

    fun blockClicked(block: Char, index: Int) {
        if (!GameUtils.isTouching(
                index,
                level.playerIndex,
                level.x
            )
        ) {
            return // Invalid block clicked
        }
        if (level.blocks.indexOf(PLAYER_BLOCK) == -1)
            return // Already dead
        val newState: LevelState?
        when (block) {
            ENEMY_BLOCK -> {
                return
            }

            MOVABLE_BLOCK -> {
                if (handleMovableBlockMove(index)) {
                    val direction = movePlayerBlock(index)
                    newState = LevelState(
                        blocks = level.blocks.toList(),
                        movesUsed = ++level.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )
                } else {
                    return
                }
            }

            GOAL_BLOCK -> {
                val direction = movePlayerBlock(index)
                _state.value =
                    LevelState(
                        blocks = level.blocks,
                        movesUsed = ++level.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )

                viewModelScope.launch {
                    delay(200)
                    level.blocks[index] = GOAL_BLOCK
                    _state.value = LevelState(
                        blocks = level.blocks,
                        movesUsed = level.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )

                    delay(400)
                    if (level.state != GameState.FAILED) {
                        level.state = GameState.SUCCESS
                        updateLevel(level.levelSet, level.id, getStars(level.movesUsed))
                    }
                    history.clear()
                    _state.value = LevelState(
                        blocks = level.blocks,
                        movesUsed = level.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )
                }
                return
            }

            EMPTY_BLOCK -> {
                val direction = movePlayerBlock(index)
                newState = LevelState(
                    blocks = level.blocks.toList(),
                    movesUsed = ++level.movesUsed,
                    gameState = level.state,
                    direction = direction
                )

            }

            else -> {
                return
            }
        }

        if (shouldEnemyAttemptMove(level.enemyIndex, level.state)) {
            val redStates = handleEnemyTurn() // gets list of red moves to display
            if (redStates.isEmpty()) { // red block trapped
                viewModelScope.launch {
                    delay(100)
                    newState.also { _state.value = it }
                    history.add(newState)
                }
                return
            }
            viewModelScope.launch {
                newState.also { _state.value = it }

                redStates.forEachIndexed { index, levelState ->
                    when (index) {
                        0 -> delay(350)
                        else -> delay(300)
                    }
                    if (levelState.gameState == GameState.FAILED) {
                        _state.value = levelState
                    } else if ((redStates.size == 1 && redStates[0].blocks.indexOf(ENEMY_BLOCK) == level.enemyIndex) ||    // TODO: refactor this
                        (redStates.size >= 2 && (redStates[0].blocks.indexOf(ENEMY_BLOCK) == level.enemyIndex ||
                                redStates[1].blocks.indexOf(ENEMY_BLOCK) == level.enemyIndex))
                    ) { // if red move is stale (new red move occurred) don't post it
                        if (level.goalIndex != -1 && (level.blocks.indexOf(PLAYER_BLOCK) == level.goalIndex)) {   // don't post red move if valid win happens
                            return@launch
                        }
                        if (history.size < 2 || history.last().blocks.indexOf(ENEMY_BLOCK) != level.enemyIndex) // don't post red move if red stuck
                            _state.value = levelState
                    }
                }
                history.add(redStates.last())
            }
        } else {
            newState.also { _state.value = it }
            history.add(newState)
        }
    }

    private fun handleEnemyTurn(): List<LevelState> {
        val states = mutableListOf<LevelState>()
        var moveDirection = moveEnemyBlock()
        if (moveDirection != null) {

            states.add(
                LevelState(
                    blocks = level.blocks.toMutableList(),
                    movesUsed = level.movesUsed,
                    gameState = level.state,
                    direction = moveDirection
                )
            )

            if (level.blocks.indexOf(PLAYER_BLOCK) == -1) {
                states.add(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = level.movesUsed,
                        gameState = GameState.FAILED,
                        direction = moveDirection
                    )
                )
                return states
            }

            moveDirection = moveEnemyBlock() // attempt second move, return null if no valid move

            if (moveDirection != null) {
                states.add(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = level.movesUsed,
                        gameState = level.state,
                        direction = moveDirection
                    )
                )

                if (level.blocks.indexOf(PLAYER_BLOCK) == -1) {
                    states.add(
                        LevelState(
                            blocks = level.blocks.toMutableList(),
                            movesUsed = level.movesUsed,
                            gameState = GameState.FAILED,
                            direction = moveDirection
                        )
                    )
                }
            }
            return states
        }
        return emptyList()
    }

    private fun movePlayerBlock(index: Int): Direction? {
        val oldIndex = level.playerIndex
        level.blocks[index] = PLAYER_BLOCK
        level.blocks[oldIndex] = EMPTY_BLOCK
        level.playerIndex = index
        return getDirection(oldIndex, index, level.x)
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
            EMPTY_BLOCK -> {
                level.blocks[newIndex] = MOVABLE_BLOCK
                true
            }

            MOVABLE_BLOCK -> {
                level.blocks[newIndex] = EMPTY_BLOCK
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

        if (level.enemyIndex == level.goalIndex && isValidEnemyMove(level.blocks[newIndex])) { // Red moves off of Yellow
            moveEnemyOffGoal(newIndex)
            return true
        }

        if (level.playerIndex == newIndex) {
            level.state = GameState.FAILED
        }

        if (isValidEnemyMove(level.blocks[newIndex])) {
            moveEnemyToNewIndex(newIndex)

            return true
        }
        return false
    }

    private fun moveEnemyOffGoal(newIndex: Int) {
        level.blocks[level.enemyIndex] = GOAL_BLOCK
        level.blocks[newIndex] = ENEMY_BLOCK
        level.enemyIndex = newIndex
    }

    private fun moveEnemyToNewIndex(newIndex: Int) {
        level.blocks[level.enemyIndex] = EMPTY_BLOCK
        level.blocks[newIndex] = ENEMY_BLOCK
        level.enemyIndex = newIndex
    }

    fun undoClicked() {
        if (history.size >= 2) {
            history.sortBy { it.movesUsed }
            level.blocks = history[history.size - 2].blocks.toMutableList() // Code Smell
            val direction = getUndoDirection(level.playerIndex, level.blocks.indexOf(PLAYER_BLOCK))
            level.playerIndex = level.blocks.indexOf(PLAYER_BLOCK)
            level.enemyIndex = level.blocks.indexOf(ENEMY_BLOCK)
            _state.value = history[history.size - 2].copy(direction = direction)
            history.removeLast()
            level.movesUsed--
        }
    }

    private fun getUndoDirection(currentIndex: Int, newIndex: Int): Direction {
        return when {
            currentIndex == newIndex + 1 -> Direction.LEFT
            currentIndex == newIndex - 1 -> Direction.RIGHT
            currentIndex > newIndex -> Direction.UP
            currentIndex < newIndex -> Direction.DOWN
            else -> Direction.UP
        }
    }

    fun tryAgain() {
        level.resetLevel()
        history.clear()
        val newState = LevelState(
            blocks = level.initialBlocks,
            movesUsed = 0,
            gameState = GameState.IN_PROGRESS
        )
        history.add(newState)
        _state.postValue(
            newState
        )
    }

    private fun getMinMoves(): Int {
        return level.minMoves
    }

    fun getStars(movesUsed: Int): Int {
        return if (movesUsed <= getMinMoves()) 3 else if (movesUsed - 2 <= getMinMoves()) 2 else 1
    }

    fun isFinalLevel(): Boolean = FINAL_LEVELS.contains(level.id)

    fun infoClicked() {
        if (_infoState.value != -1) {
            _infoState.value = -1
            return
        }
        var cardShown = 0
        if (level.levelSet == LevelSet.MEDIUM) cardShown = HELP_CARD_MOVE_INITIAL
        _infoState.value = cardShown
    }

    fun getInfoProgress(): Int {
        return if (repo.getDifficultyProgress()[0] >= 15) HELP_CARD_COUNT_SECOND else HELP_CARD_COUNT_INITIAL
    }

    private fun cardToDisplay(): Int {
        return when (repo.getTutorialStage()) {
            4 -> HELP_CARD_MOVE_INITIAL
            5 -> HELP_CARD_MOVE_SECOND
            else -> -1
        }
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