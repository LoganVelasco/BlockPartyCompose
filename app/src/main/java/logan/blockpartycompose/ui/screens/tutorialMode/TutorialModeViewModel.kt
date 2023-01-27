package logan.blockpartycompose.ui.screens.tutorialMode

import android.content.Context
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
import logan.blockpartycompose.ui.screens.level.Direction
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils
import logan.blockpartycompose.utils.GameUtils.Companion.EMPTY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.ENEMY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.GOAL_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.PLAYER_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.UNMOVABLE_BLOCK
import javax.inject.Inject

@HiltViewModel
class TutorialModeViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _tutorialState = MutableLiveData<Pair<Int, Int>>() // stage, progress
    val tutorialState: LiveData<Pair<Int, Int>> = _tutorialState

    private var _state = MutableLiveData<GamePlayState>()
    val state: LiveData<GamePlayState> = _state

    private val history = mutableListOf<GamePlayState>()

    private lateinit var tutorialLevels: List<Level>

    lateinit var level: Level

    private var _isInfoClicked = MutableLiveData<Boolean>()
    val isInfoClicked: LiveData<Boolean> = _isInfoClicked

    private fun updateTutorialProgress(stage: Int) {
        repo.updateTutorialStage(stage)
    }

    fun startTutorial(context: Context) {
        getLevels(context)
        setupTutorialStage(repo.getTutorialStage())
    }

    private fun setupTutorialStage(stage: Int) {
        when (stage) {
            0 -> {
                setupLevel(0)
                _tutorialState.value = 0 to 0
            }

            1 -> {
                setupLevel(1)
                _tutorialState.value = 1 to 0
            }

            2 -> {
                setupLevel(2)
                _tutorialState.value = 2 to 0
            }

            3 -> {
                setupLevel(3)
                _tutorialState.value = 3 to 0
            }
        }
    }

    fun nextLevelOnClick(navigateToMenu: (() -> Unit)) {
        val newProgress = _tutorialState.value!!.first + 1
        _tutorialState.value = newProgress to 0
        updateTutorialProgress(newProgress)
        if (newProgress == 4) navigateToMenu()
        else setupTutorialStage(level.id + 1)
    }

    private fun setupLevel(id: Int) {
        level = getLevel(id)
        level.resetLevel()
//        val blocks = if (id != 1 && id != 2) getSurroundingBlocks() else emptyList()
        val blocks = getSurroundingBlocks()
        val newState = GamePlayState(
            blocks = level.initialBlocks,
            glowingBlocks = blocks,
            movesUsed = 0,
            gameState = GameState.IN_PROGRESS
        )
        _state.postValue(
            newState
        )
        history.clear()
        history.add(newState)
    }


    private fun getSurroundingBlocks(): List<Int> {
        val list = mutableListOf<Int>()
        if (GameUtils.isTouching(level.playerIndex - 1, level.playerIndex, level.x)) list.add(
            level.playerIndex - 1
        )
        if (GameUtils.isTouching(level.playerIndex + 1, level.playerIndex, level.x)) list.add(
            level.playerIndex + 1
        )
        if (GameUtils.isTouching(
                level.playerIndex - level.x,
                level.playerIndex,
                level.x
            )
        ) list.add(level.playerIndex - level.x)
        if (GameUtils.isTouching(
                level.playerIndex + level.x,
                level.playerIndex,
                level.x
            )
        ) list.add(level.playerIndex + level.x)
        return list.filter {
            level.blocks[it] != ENEMY_BLOCK &&
                    level.blocks[it] != UNMOVABLE_BLOCK
        }
    }

    private fun getLevels(context: Context) {
        tutorialLevels = repo.getLevels(LevelSet.EASY, context).subList(0, 4)
    }

    private fun getLevel(id: Int): Level {
        return tutorialLevels.find { it.id == id } ?: tutorialLevels[0]
    }

    private fun updateLevel(difficulty: LevelSet, name: Int, stars: Int) {
        repo.updateLevelProgress(difficulty, name, stars)
    }

    private fun glowBlocksOnClick(block: Char, index: Int) {
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
        if (block == EMPTY_BLOCK) {
            val direction = movePlayerBlock(index)
            var glow = getSurroundingBlocks()
            if (GameUtils.isTouching(
                    index,
                    level.goalIndex,
                    level.x
                )
            ) {
                glow = listOf(level.goalIndex)
            }
            val newState = GamePlayState(
                blocks = level.blocks.toList(),
                glowingBlocks = glow,
                movesUsed = ++level.movesUsed,
                gameState = level.state,
                direction = direction
            )
            if (level.enemyIndex != -1) redMove(newState)
            else
                _state.value = newState
        } else if (block == GOAL_BLOCK) {
            val direction = movePlayerBlock(index)
            _state.value =
                GamePlayState(
                    blocks = level.blocks,
                    movesUsed = ++level.movesUsed,
                    gameState = level.state,
                    direction = direction
                )

            viewModelScope.launch {
                delay(200)
                level.blocks[index] = GOAL_BLOCK
                _state.value = GamePlayState(
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
                _state.value = GamePlayState(
                    blocks = level.blocks,
                    movesUsed = level.movesUsed,
                    gameState = level.state,
                    direction = direction
                )
            }
            return
        }
    }

    fun blockClicked(block: Char, index: Int) {
        when (tutorialState.value!!.first) {
            0 -> glowBlocksOnClick(block, index)
            1 -> glowBlocksOnClick(block, index)
            2 -> glowBlocksOnClick(block, index)
            3 -> glowBlocksOnClick(block, index)
        }
    }

    private fun redMove(newState: GamePlayState) {
        if (GameUtils.shouldEnemyAttemptMove(level.enemyIndex, level.state)) {
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
                        if (history.size < 2 || history.last().blocks.indexOf(ENEMY_BLOCK) != level.enemyIndex) { // don't post red move if red stuck
                            if (newState.glowingBlocks.isNotEmpty()) {
                                levelState.glowingBlocks =
                                    if (newState.glowingBlocks.contains(level.goalIndex)) newState.glowingBlocks else getSurroundingBlocks()
                            }
                            _state.value = levelState
                        }
                    }
                }
                history.add(redStates.last())
            }
        } else {
            newState.also { _state.value = it }
            history.add(newState)
        }
    }

    private fun handleEnemyTurn(): List<GamePlayState> {
        val states = mutableListOf<GamePlayState>()
        var moveDirection = moveEnemyBlock()
        if (moveDirection != null) {

            states.add(
                GamePlayState(
                    blocks = level.blocks.toMutableList(),
                    movesUsed = level.movesUsed,
                    gameState = level.state,
                    direction = moveDirection
                )
            )

            if (level.blocks.indexOf(PLAYER_BLOCK) == -1) {
                states.add(
                    GamePlayState(
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
                    GamePlayState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = level.movesUsed,
                        gameState = level.state,
                        direction = moveDirection
                    )
                )

                if (level.blocks.indexOf(PLAYER_BLOCK) == -1) {
                    states.add(
                        GamePlayState(
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
        return GameUtils.getDirection(oldIndex, index, level.x)
    }

    private fun moveEnemyBlock(): Direction? {
        if (GameUtils.isInSameColumn(level.playerIndex, level.enemyIndex, level.x)) {
            return if (GameUtils.isAbove(level.enemyIndex, level.playerIndex)) {
                if (!moveEnemyBlock(Direction.UP)) {
                    null // Unable to move up return null to signify end of enemy turn
                } else Direction.UP
            } else {
                if (!moveEnemyBlock(Direction.DOWN)) {
                    null // Unable to move down return null to signify end of enemy turn
                } else Direction.DOWN
            }
        } else if (GameUtils.isRightOf(level.enemyIndex, level.playerIndex, level.x)) {
            if (!moveEnemyBlock(Direction.LEFT)) {
                if (GameUtils.isInSameRow(level.playerIndex, level.enemyIndex, level.x)) {
                    return null // Unable to left move return null to signify end of enemy turn
                }
                return if (GameUtils.isAbove(level.enemyIndex, level.playerIndex)) {
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
                if (GameUtils.isInSameRow(level.playerIndex, level.enemyIndex, level.x)) {
                    null // Unable to move right return null to signify end of enemy turn
                } else
                    if (GameUtils.isAbove(level.enemyIndex, level.playerIndex)) {
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

        if (level.enemyIndex == level.goalIndex && GameUtils.isValidEnemyMove(level.blocks[newIndex])) { // Red moves off of Yellow
            moveEnemyOffGoal(newIndex)
            return true
        }

        if (level.playerIndex == newIndex) {
            level.state = GameState.FAILED
        }

        if (GameUtils.isValidEnemyMove(level.blocks[newIndex])) {
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
        if ((tutorialState.value?.second ?: 0) == 0) progressForward()
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
        if ((tutorialState.value?.first ?: 0) == 3 && (tutorialState.value?.second
                ?: 0) == 1
        ) progressForward()
        level.resetLevel()
        history.clear()
        val newState = GamePlayState(
            blocks = level.initialBlocks,
            glowingBlocks = getSurroundingBlocks(),
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

    private fun getStars(movesUsed: Int): Int {
        return if (movesUsed <= getMinMoves()) 3 else if (movesUsed - 2 <= getMinMoves()) 2 else 1
    }

    fun infoClicked() {
        if ((tutorialState.value?.second ?: 0) == 2) progressForward()
        val currentState = isInfoClicked.value ?: false
        _isInfoClicked.value = !currentState
    }

    fun progressForward() {
        if (tutorialState.value!!.second == 0 && ((tutorialState.value!!.first == 1) || (tutorialState.value!!.first == 2))) {
            _state.value = GamePlayState(
                blocks = level.blocks,
                glowingBlocks = getSurroundingBlocks(),
                movesUsed = 0,
                gameState = GameState.IN_PROGRESS,
            )
        }
        _tutorialState.value = tutorialState.value!!.first to tutorialState.value!!.second + 1
    }

    @Immutable
    data class GamePlayState(
        val blocks: List<Char> = emptyList(),
        var glowingBlocks: List<Int> = emptyList(),
        var movesUsed: Int = 0,
        var gameState: GameState,
        var direction: Direction? = null
    )
}