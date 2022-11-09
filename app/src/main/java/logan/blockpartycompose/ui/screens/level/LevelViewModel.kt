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
class LevelViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelState>()
    val state: LiveData<LevelState> = _state

    private val history = mutableListOf<LevelState>()

    lateinit var level: Level

    private val playerBlock = 'p'
    private val enemyBlock = 'e'
    private val movableBlock = 'm'
    private val goalBlock = 'g'
    private val emptyBlock = '.'

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
    }

    fun setupLevel(newLevel: Level) {
        level = newLevel
        val newState = LevelState(
            blocks = newLevel.blocks,
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
            return // Invalid block clicked
        }
        var newState: LevelState? = null
        when (block) {
            enemyBlock -> {
                level.state = GameState.FAILED
                newState = LevelState(
                    blocks = level.blocks,
                    movesUsed = ++level.movesUsed,
                    gameState = level.state,
                )
                history.clear()
            }
            movableBlock -> {
                if (handleMovableBlockMove(index)) {
                    val direction = movePlayerBlock(index)
                    newState = LevelState(
                        blocks = level.blocks,
                        movesUsed = ++level.movesUsed,
                        gameState = level.state,
                        direction = direction
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
                        movesUsed = ++level.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )
                )
                history.clear()
                viewModelScope.launch {
                    delay(200)
                    level.blocks[index] = goalBlock
                    _state.postValue(
                        LevelState(
                            blocks = level.blocks,
                            movesUsed = level.movesUsed,
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
                            movesUsed = level.movesUsed,
                            gameState = level.state,
                            direction = direction
                        )
                    )

                }
                return
            }
            emptyBlock -> {
                val direction = movePlayerBlock(index)!!
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
            val redStates = handleEnemyTurn()
            if(redStates.isEmpty()){
                _state.value = newState!!
                history.add(newState)
                return
            }
            viewModelScope.launch {
                _state.value = newState!!
                delay(150)

                _state.value = redStates[0]


                if (redStates.size >1)
                {
                    delay(200)
                    _state.value = redStates[1]
                    history.add(redStates[1])
                }else
                    history.add(redStates[0])
            }
        }else{
            _state.value = newState!!
            history.add(newState)
        }
    }

    private fun handleEnemyTurn():List<LevelState> {
        val states = mutableListOf<LevelState>()
        var moveDirection = moveEnemyBlock()
        if (moveDirection != null && level.state == GameState.IN_PROGRESS) {


            states.add( LevelState(
                blocks = level.blocks.toMutableList(),
                movesUsed = level.movesUsed,
                gameState = level.state,
                direction = moveDirection
            ))


            if (level.state != GameState.IN_PROGRESS) return emptyList()

            moveDirection = moveEnemyBlock()
            if (moveDirection != null) {
                states.add(LevelState(
                    blocks = level.blocks.toMutableList(),
                    movesUsed = level.movesUsed,
                    gameState = level.state,
                    direction = moveDirection
                ))

            }
            return states
        }
        return emptyList()
    }

    private fun movePlayerBlock(index: Int): Direction? {
        val oldIndex = level.playerIndex
        level.blocks[index] = playerBlock
        level.blocks[oldIndex] = emptyBlock
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
                        movesUsed = level.movesUsed,
                        gameState = level.state,
                        direction = direction
                    )
                )
                delay(250)
                level.state = GameState.FAILED
                _state.postValue(
                    LevelState(
                        blocks = level.blocks.toMutableList(),
                        movesUsed = level.movesUsed,
                        gameState = level.state
                    )
                )
            }
            return false
        }


        if (level.enemyIndex == level.goalIndex && isValidEnemyMove(level.blocks[newIndex])) { // Red moves off of Yellow
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

    fun undoClicked(){
       if(history.size >= 2){
           history.sortBy { it.movesUsed }
           level.blocks = history[history.size-2].blocks.toMutableList() // Code Smell
           val direction = getUndoDirection(level.playerIndex, level.blocks.indexOf('p'))
           level.playerIndex = level.blocks.indexOf('p')
           level.enemyIndex = level.blocks.indexOf('e')
           _state.value = history[history.size-2].copy(direction = direction)
           history.removeLast()
           level.movesUsed--
       }
    }

    private fun getUndoDirection(currentIndex: Int, newIndex:  Int): Direction {
        return when{
            currentIndex == newIndex+1 -> Direction.LEFT
            currentIndex == newIndex-1 -> Direction.RIGHT
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