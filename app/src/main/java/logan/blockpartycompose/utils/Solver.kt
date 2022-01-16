package logan.blockpartycompose.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LevelSolver @Inject constructor() {
    lateinit var initialState:List<Char>

//    var visitedStates = List<ArrayList<Char>>()
//
//    var badStates = ArrayList<ArrayList<Char>>()
//
//    var winsStates = ArrayList<ArrayList<ArrayList<Char>>>()
//
//    var moveCount = 0

    //TODO: inject me
    private val dispatcher = Dispatchers.Default

//    val solverService = SolverService()

    suspend fun isSolvable(state: ArrayList<Char>): Boolean {
        var moveCount = 0
        initialState = state
        var visitedStates = listOf(initialState)
        var winsStates: List<List<List<Char>>> = emptyList()
        var badStates: List<List<Char>> = emptyList()

        if (!isLevelValid(initialState)) return false

        val staringPoint = findBlueBlock(initialState)
        println("Solving")
        printLayout(initialState)
        println("Level")


        getNextMove(staringPoint, moveCount, visitedStates, winsStates, badStates)

        return if (winsStates.isNullOrEmpty()) {
            printFailedToFindSolution()
            false
        } else {
            if (winsStates[winsStates.size - 1].size <= 6) {
                println("Too easy trying again")
                return false
            }
            printShortestPath(winsStates)
            true
        }
    }

    suspend fun findShortestSolution(state: List<Char>): List<Int>? {
        var moveCount = 0
        initialState = state
        var visitedStates = arrayListOf(initialState)
        var winsStates: List<List<List<Char>>> = emptyList()
        var badStates: List<List<Char>> = emptyList()

        if (!isLevelValid(initialState)) return null

        val staringPoint = findBlueBlock(initialState)
        println("Solving")
        printLayout(initialState)
        println("Level")

        winsStates = getNextMove(
            staringPoint,
            moveCount,
            visitedStates,
            winsStates,
            badStates
        )

        return if (winsStates.isNullOrEmpty()) {
            printFailedToFindSolution()
            null
        } else {
            printShortestPath(winsStates)
            return createSolutionList(winsStates[winsStates.size - 1])
        }
    }

    private fun createSolutionList(moveList: List<List<Char>>): List<Int> {
        return moveList.subList(1, moveList.size).map {
            findBlueBlock(it)
        }
    }

    private fun getMoveDirection(initialPosition: Int, nextPosition: Int): Char {
        return when (nextPosition) {
            initialPosition + 1 -> {
                'r'
            }
            initialPosition - 1 -> {
                'l'
            }
            initialPosition - 6 -> {
                'u'
            }
            initialPosition + 6 -> {
                'd'
            }
            else -> ' '
        }

    }

    suspend fun getNextMove(
        position: Int,
        moveCount: Int,
        visitedStates: List<List<Char>>,
        winsStates: List<List<List<Char>>>,
        badStates: List<List<Char>>
    ): List<List<List<Char>>> {
        var winsState = listOf<List<Char>>()
        var currentState = visitedStates.last()
        var possibleMoves = getPossibleMoves(position, visitedStates)
//        if (isCountHigherThenSolution(winsStates, moveCount)) {
//            handleDeadEnd(currentState, badStates, visitedStates)
//            return winsStates
//        }

        withContext(dispatcher) {
            val movesJobs = mutableListOf<Deferred<Unit>>()
            val newBadStates = if(badStates.isEmpty()) arrayListOf()
                else badStates as ArrayList<List<Char>>
            val upBadState = newBadStates.clone() as ArrayList<List<Char>>
            val downBadState = newBadStates.clone() as ArrayList<List<Char>>
            val rightBadState = newBadStates.clone() as ArrayList<List<Char>>
            val leftBadState = newBadStates.clone() as ArrayList<List<Char>>
            for (moves in possibleMoves) {
                when (moves) {
                    'u' -> {
                        movesJobs.add(async {
                            val newWinsState = makeMove(
                                currentState,
                                position,
                                position - 6,
                                moveCount + 1,
                                visitedStates,
                                winsStates,
                                upBadState
                            )
                            if (newWinsState.isNotEmpty()) {
                                winsState = newWinsState.last()
                                this.cancel()
                            }
                        })

                    }
                    'd' -> {
                        if(position + 6 > 48)throw NullPointerException()
                        movesJobs.add(async {
                            val newWinsState = makeMove(
                                currentState,
                                position,
                                position + 6,
                                moveCount + 1,
                                visitedStates,
                                winsStates,
                                downBadState
                            )
                            if (newWinsState.isNotEmpty()) {
                                winsState = newWinsState.last()
                                this.cancel()
                            }
                        })
                    }
                    'l' -> {
                        movesJobs.add(async {
                            val newWinsState = makeMove(
                                currentState,
                                position,
                                position - 1,
                                moveCount + 1,
                                visitedStates,
                                winsStates,
                                leftBadState
                            )
                            if (newWinsState.isNotEmpty()) {
                                winsState = newWinsState.last()
                                this.cancel()
                            }
                        })
                    }
                    'r' -> {
                        movesJobs.add(async {
                            val newWinsState = makeMove(
                                currentState,
                                position,
                                position + 1,
                                moveCount + 1,
                                visitedStates,
                                winsStates,
                                rightBadState
                            )
                            if (newWinsState.isNotEmpty()) {
                                winsState = newWinsState.last()
                                this.cancel()
                            }

                        })
                    }
                    else -> throw Exception()
                }
            }
            movesJobs.awaitAll()
            if(winsState.isNotEmpty()) return@withContext winsStates
            else this.cancel()
            handleDeadEnd(currentState, newBadStates, visitedStates)
        }

        return listOf(winsState)
    }

    fun isCountHigherThenSolution(
        winsStates: List<List<List<Char>>>,
        moveCount: Int
    ): Boolean {
        if (winsStates.isNullOrEmpty()) return false
        return moveCount >= winsStates.last().size
    }

    private suspend fun makeMove(
        currentState: List<Char>,
        position: Int,
        newPosition: Int,
        moveCount: Int,
        visitedStates: List<List<Char>>,
        winsStates: List<List<List<Char>>>,
        badStates: ArrayList<List<Char>>
    ): List<List<List<Char>>> {
        if (newPosition == findEndingPoint()) {
            return handleWin(visitedStates, winsStates, badStates, moveCount)
        }
        val newLayout =
            getUpdatedLayout(currentState, position, newPosition, visitedStates, winsStates, moveCount)
        if (newLayout != null) {
            val updatedVisitedStates = visitedStates as ArrayList<List<Char>>
            visitedStates.add(newLayout)
            println("Count: ${updatedVisitedStates.size} Move Count: $moveCount")
            if (moveCount > 50) {
                handleDeadEnd(currentState, badStates, updatedVisitedStates)
                return winsStates
            }
            return getNextMove(
                newPosition,
                moveCount,
                updatedVisitedStates,
                winsStates,
                badStates
            )
        }
        return winsStates
    }

    private fun getUpdatedLayout(
        currentState: List<Char>,
        position: Int,
        newPosition: Int,
        visitedStates: List<List<Char>>,
        winsStates: List<List<List<Char>>>,
        moveCount: Int
    ): List<Char>? {
        val newLayout = ArrayList<Char>()
        newLayout.addAll(currentState)
        if(newLayout.size == 48)newLayout.add(moveCount.toChar())
        else newLayout[48] = moveCount.toChar()
        if (!isGreenBlockMoved(newLayout, position, newPosition)) {
            if(newLayout[position] != 'b'){
                return null
            }
            newLayout[position] = '.'
            newLayout[newPosition] = 'b'
        }

        return if (moveRed(newLayout) || moveRed(newLayout)) {
            null
        }
        else if (isAlreadyVisitedState(newLayout,
                visitedStates, winsStates)) {
            null
        }
        else newLayout
    }


    fun moveRed(currentState: ArrayList<Char>): Boolean {
        var possibleMoves = arrayListOf<Char>()
        val blueBlock = findBlueBlock(currentState)
        val redBlock = findRedBlock(currentState)
        val orderedDirections = sortByClosestToGold(redBlock, blueBlock)

        val redMoveFirstDirection = orderedDirections[0]
        val redMoveSecondDirection = orderedDirections[1]

        when (redMoveFirstDirection) {
            'u' -> {
                if (handleRedMove(
                        redBlock,
                        possibleMoves,
                        redMoveFirstDirection,
                        currentState,
                        blueBlock
                    )
                ) return true
            }
            'd' -> {
                if (handleRedMove(
                        redBlock,
                        possibleMoves,
                        redMoveFirstDirection,
                        currentState,
                        blueBlock
                    )
                ) return true
            }
            'l' -> {
                if (handleHorizontalMove(
                        redBlock,
                        possibleMoves,
                        redMoveFirstDirection,
                        currentState,
                        blueBlock,
                        redMoveSecondDirection
                    )
                ) return true
            }
            'r' -> {
                if (handleHorizontalMove(
                        redBlock,
                        possibleMoves,
                        redMoveFirstDirection,
                        currentState,
                        blueBlock,
                        redMoveSecondDirection
                    )
                ) return true
            }
            else -> throw Exception()
        }
        return false
    }

    private fun handleHorizontalMove(
        redBlock: Int,
        possibleMoves: ArrayList<Char>,
        redMoveFirstDirection: Char,
        currentState: ArrayList<Char>,
        blueBlock: Int,
        redMoveSecondDirection: Char
    ): Boolean {
        if (handleRedMove(
                redBlock,
                possibleMoves,
                redMoveFirstDirection,
                currentState,
                blueBlock
            )
        ) return true

        if (!possibleMoves.contains(redMoveFirstDirection) && isMoveCloserToBlue(
                redMoveSecondDirection,
                blueBlock,
                redBlock
            )
        ) {
            if (redMoveSecondDirection == 'u') {
                if (handleRedMove(
                        redBlock,
                        possibleMoves,
                        redMoveSecondDirection,
                        currentState,
                        blueBlock
                    )
                ) return true

            } else {

                if (handleRedMove(
                        redBlock,
                        possibleMoves,
                        redMoveSecondDirection,
                        currentState,
                        blueBlock
                    )
                ) return true
            }
        }
        return false
    }

    fun isMoveCloserToBlue(redMoveSecondDirection: Char, blueBlock: Int, redBlock: Int): Boolean {
        return when (redMoveSecondDirection) {
            'u' -> {
                getRow(blueBlock) != getRow(redBlock)
            }

            'd' -> {
                getRow(blueBlock) != getRow(redBlock)
            }
            else -> throw Exception()
        }
    }

    private fun handleRedMove(
        redBlock: Int,
        possibleMoves: ArrayList<Char>,
        moveDirection: Char,
        currentState: ArrayList<Char>,
        blueBlock: Int
    ): Boolean {

        filterOffGrid(redBlock, possibleMoves, moveDirection)
        filterBlackBlocks(redBlock, possibleMoves, currentState)
        filterGreenBlocksForRed(redBlock, possibleMoves, currentState)
        if (possibleMoves.contains(moveDirection)) {
            val newPosition = getNewPosition(redBlock, moveDirection)
            checkIfOnYellowBlock(redBlock, currentState)
            currentState[newPosition] = 'r'
            println("Handle red move")
            printLayout(currentState)
            println("Handled red move")
            if (newPosition == blueBlock) {
                return true
            }
        }
        println("No Red Move Possible")
        printLayout(currentState)
        println("No Red Move Possible")

        return false
    }

    fun getNewPosition(redBlock: Int, moveDirection: Char): Int {
        return when (moveDirection) {
            'u' -> {
                redBlock - 6
            }
            'd' -> {
                redBlock + 6
            }
            'l' -> {
                redBlock - 1
            }
            'r' -> {
                redBlock + 1
            }
            else -> throw Exception()
        }
    }

    private fun checkIfOnYellowBlock(redBlock: Int, currentState: ArrayList<Char>) {
        if (redBlock == findEndingPoint()) {
            currentState[redBlock] = 'y'
        } else
            currentState[redBlock] = '.'
    }

    fun isOnSameRow(redBlock: Int, blueBlock: Int): Boolean {
        return if (redBlock > blueBlock) {
            blueBlock + (6 - (blueBlock % 6)) > redBlock
        } else {
            redBlock + (6 - (redBlock % 6)) > blueBlock
        }
    }

    fun findBlueBlock(layout: List<Char>): Int {
        return layout.indexOf('b')
    }

    fun findRedBlock(layout: List<Char>): Int {
        return layout.indexOf('r')
    }

    fun findEndingPoint(): Int {
        return initialState.indexOf('y')
    }

    fun getPossibleMoves(position: Int, visitedStates: List<List<Char>>): List<Char> {
        val possibleMoves = arrayListOf<Char>()
        val goldPosition = findEndingPoint()
        val orderedDirections = sortByClosestToGold(position, goldPosition)

        for (direction in orderedDirections) {
            filterOffGrid(position, possibleMoves, direction)
        }
        filterBlackBlocks(position, possibleMoves, visitedStates.last())
        filterGreenBlocks(position, possibleMoves, visitedStates.last())

        return possibleMoves
    }

    //Past method to when(Move) method
    fun filterGreenBlocks(
        position: Int,
        possibleMoves: ArrayList<Char>,
        currentState: List<Char>
    ) {
        for (move in possibleMoves) {
            when (move) {
                'u' -> {
                    handleGreenBlocks(currentState, position, position - 6, possibleMoves, 'u')
                }
                'd' -> {
                    handleGreenBlocks(currentState, position, position + 6, possibleMoves, 'd')
                }
                'l' -> {
                    handleGreenBlocks(currentState, position, position - 1, possibleMoves, 'l')
                }
                'r' -> {
                    handleGreenBlocks(currentState, position, position + 1, possibleMoves, 'r')
                }
                else -> throw Exception()
            }
        }
    }

    private fun handleGreenBlocks(
        currentState: List<Char>,
        position: Int,
        newPosition: Int,
        possibleMoves: ArrayList<Char>,
        move: Char
    ) {
        val validMoves = ArrayList<Char>()
        val greenBlockNewPosition = getNewGreenPosition(position, newPosition)

        if (currentState[newPosition] == 'g') {
            filterOffGrid(newPosition, validMoves, move)
            filterBlackBlocks(newPosition, validMoves, currentState)
//        if((greenBlockNewPosition in 0..47) && currentState[greenBlockNewPosition] == 'g')
//            handleGreenBlocks(currentState, newPosition, greenBlockNewPosition, possibleMoves, move)
            if (validMoves.size != 1 || currentState[greenBlockNewPosition] == 'r') {
                possibleMoves.remove(move)
            }
        }
    }

    fun isGreenBlockMoved(newLayout: ArrayList<Char>, position: Int, newPosition: Int): Boolean {
        return if (newLayout[newPosition] == 'g') {
            val newGreenPosition = getNewGreenPosition(position, newPosition)
            newLayout[newPosition] = newLayout[position]
            newLayout[position] = '.'
            if (newLayout[newGreenPosition] != 'y') {
                if (newLayout[newGreenPosition] == 'g') {
                    newLayout[newGreenPosition] = '.'
                } else
                    newLayout[newGreenPosition] = 'g'
            }
            true
        } else false
    }

    fun getNewGreenPosition(position: Int, newPosition: Int): Int {
        return position - ((position - newPosition) + (position - newPosition))
    }

    fun isAlreadyVisitedState(
        state: List<Char>,
        visitedStates: List<List<Char>>,
        winsStates: List<List<List<Char>>>
    ): Boolean {
        var oldCount = 0
        var newCount = 0
        val newVisitedStates = (visitedStates as ArrayList).clone() as List<List<Char>>
            newVisitedStates.forEach { oldState ->
            val isStateSame = oldState.subList(0, 47) == state.subList(0, 47)

            if (isStateSame) {  // TODO Make more efficient for lots of winsStates
                oldCount = oldState[48].toInt()
                newCount = state[48].toInt()
                if (!winsStates.isNullOrEmpty() && winsStates.last().contains(state)) {
                    if (oldCount <= newCount) {
//                        oldState[48] = oldCount.toChar()
//                        state[48] = newCount.toChar()
                        return true
                    }
                } else {
//                    oldState[48] = oldCount.toChar()
//                    state[48] = newCount.toChar()
                    return true
                }
            }
//            oldState[48] = oldCount.toChar()
//            state[48] = newCount.toChar()
        }
        return false
    }


    fun filterBlackBlocks(
        position: Int,
        possibleMoves: ArrayList<Char>,
        currentState: List<Char>
    ) {
        if (position - 6 >= 0 && (currentState[position - 6] == 'x' || currentState[position - 6] == 'r')) {
            possibleMoves.remove('u')
        }

        if (position + 6 < 48 && (currentState[position + 6] == 'x' || currentState[position + 6] == 'r')) {
            possibleMoves.remove('d')
        }

        if (position - 1 >= 0 && (currentState[position - 1] == 'x' || currentState[position - 1] == 'r')) {
            possibleMoves.remove('l')
        }

        if (position + 1 < 48 && (currentState[position + 1] == 'x' || currentState[position + 1] == 'r')) {
            possibleMoves.remove('r')
        }
    }

    fun filterGreenBlocksForRed(
        position: Int,
        possibleMoves: ArrayList<Char>,
        currentState: List<Char>
    ) {

        if (position - 6 >= 0 && (currentState[position - 6] == 'g')) {
            possibleMoves.remove('u')
        }

        if (position + 6 < 48 && (currentState[position + 6] == 'g')) {
            possibleMoves.remove('d')
        }

        if (position - 1 >= 0 && (currentState[position - 1] == 'g')) {
            possibleMoves.remove('l')
        }

        if (position + 1 < 48 && (currentState[position + 1] == 'g')) {
            possibleMoves.remove('r')
        }
    }

    //Refactor to closest to point
    fun sortByClosestToGold(position: Int, goldPosition: Int): List<Char> {
        val orderedDirections = ArrayList<Char>()

        if (getColoum(position) == getColoum(goldPosition)) {
            if (position < goldPosition) {
                orderedDirections.add('d')
                orderedDirections.add('r')
                orderedDirections.add('l')
                orderedDirections.add('u')
            } else {
                orderedDirections.add('u')
                orderedDirections.add('r')
                orderedDirections.add('l')
                orderedDirections.add('d')
            }
        } else if (getColoum(position) < getColoum(goldPosition)) {
            orderedDirections.add('r')
            if (position < goldPosition) {
                orderedDirections.add('d')
                orderedDirections.add('u')
            } else {
                orderedDirections.add('u')
                orderedDirections.add('d')
            }
            orderedDirections.add('l')
        } else {
            orderedDirections.add('l')
            if (position < goldPosition) {
                orderedDirections.add('d')
                orderedDirections.add('u')
            } else {
                orderedDirections.add('u')
                orderedDirections.add('d')
            }
            orderedDirections.add('r')
        }
        return orderedDirections
    }

    fun getColoum(position: Int): Int {
        return position % 6
    }

    fun getRow(position: Int): Any {
        return position / 6
    }

    fun filterOffGrid(position: Int, possibleMoves: ArrayList<Char>, direction: Char) {
        when (direction) {
            'u' -> {
                if (position - 6 >= 0) {
                    possibleMoves.add('u')
                }
            }
            'd' -> {
                if (position + 6 < 48) {
                    possibleMoves.add('d')
                }
            }
            'l' -> {
                if ((position) % 6 != 0) {
                    possibleMoves.add('l')
                }
            }
            'r' -> {
                if ((position + 1) % 6 != 0) {
                    possibleMoves.add('r')
                }
            }
            else -> throw Exception()
        }

    }

    fun printLayout(layout: List<Char>) {
        var layoutString = ""
        for (x in 0..47) {
            if (x % 6 == 0) layoutString += "\n"
            layoutString += layout[x] + ""
        }
        println("layout = $layoutString\n")

    }

    fun handleDeadEnd(
        currentState: List<Char>,
        badStates: ArrayList<List<Char>>,
        visitedStates: List<List<Char>>,
    ) {
        println("-----------")
        println("Hit Dead End")
        if (currentState != initialState) badStates.add(currentState)
        println("ADDING STATE TO DEAD END STATES")

        println("RETURNING TO: ${visitedStates.size}")
//        printLayout(visitedStates[visitedStates.size - 3])
//        println("-----------")
    }

    fun handleWin(
        visitedStates: List<List<Char>>,
        winsStates: List<List<List<Char>>>,
        badStates: List<List<Char>>,
        moveCount: Int
    ):List<List<List<Char>>> {
        var copy = visitedStates as ArrayList<List<Char>>
        copy.removeAll(badStates.toSet())
//        if (copy.size != moveCount) {
//            println("Win State does not match move count")
//        }
//        winsStates.add(copy)
        println("-----------")
        println("SOLVED in $moveCount moves!")
        println("Looking for shorter solutions")
        println()


        return listOf(copy)

//        println("PRINTING SOLUTION")
//        Thread.sleep(3000)

//        for (layout in copy) {
////            Thread.sleep(500)
//            printLayout(layout)
//        }

        //exitProcess(0)
    }

    fun printFailedToFindSolution() {
        println()
        println("No Solution Found")
        println("--------------------")
    }

    fun printShortestPath(winsStates: List<List<List<Char>>>) {
        println()
        println("SHORTEST PATH IS ${winsStates[winsStates.size - 1].size} MOVES")
//        Thread.sleep(1500)

        for (layout in winsStates[winsStates.size - 1]) {
            printLayout(layout)
//            Thread.sleep(1000)
        }
        println("-----------")
        println("SOLVED in ${winsStates[winsStates.size - 1].size}!")
        println("-----------")
    }

    fun isLevelValid(initialState: List<Char>): Boolean {
        return initialState.count { it == 'b' } == 1 &&
                initialState.count { it == 'r' } == 1 &&
                initialState.count { it == 'y' } == 1
    }


}
