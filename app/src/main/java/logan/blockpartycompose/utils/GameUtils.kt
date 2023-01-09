package logan.blockpartycompose.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import logan.blockpartycompose.ui.screens.level.Direction
import logan.blockpartycompose.ui.screens.level.GameState

class GameUtils {

    companion object {
        private const val playerBlock = 'p'
        private const val enemyBlock = 'e'
        private const val movableBlock = 'm'
        private const val goalBlock = 'g'
        private const val emptyBlock = '.'
        private const val playerSpeed = 350
        private const val playerDelay = 100
        private const val enemySpeed = 250
        private const val enemyDelay = 100

        fun isTouching(index: Int, index2: Int, x: Int): Boolean {
            if (isEdge(index2, x))
                return isValidEdgeMove(index, index2, x)

            if (index2 + 1 == index || index2 - 1 == index || index2 + x == index || index2 - x == index)
                return true

            return false
        }

        fun isEdge(index: Int, x: Int): Boolean {
            return index % x == 0 || index % x == x - 1
        }

        private fun isValidEdgeMove(index: Int, playerIndex: Int, x: Int): Boolean {
            return when {
                playerIndex % x == 0 -> {
                    playerIndex + 1 == index || playerIndex + x == index || playerIndex - x == index
                }
                playerIndex % x == x - 1 -> {
                    playerIndex - 1 == index || playerIndex + x == index || playerIndex - x == index
                }
                else -> false
            }
        }

        fun isRightOf(index1: Int, index2: Int, x: Int) = index1 % x > index2 % x

        fun isAbove(index1: Int, index2: Int) = index1 > index2

        fun isInSameRow(index1: Int, index2: Int, x: Int) = (index1 / x) == (index2 / x)

        fun isInSameColumn(index1: Int, index2: Int, x: Int) = index1 % x == index2 % x

        fun getDirection(initial: Int, target: Int, x: Int): Direction?{
            return when{
                isInSameColumn(initial, target, x) -> if(initial < target) Direction.DOWN else Direction.UP
                isInSameRow(initial, target, x) -> if(initial > target) Direction.LEFT else Direction.RIGHT
                else -> return null
            }
        }

        fun isValidEnemyMove(newIndexType: Char): Boolean {
            return when (newIndexType) {
                emptyBlock -> true
                playerBlock -> true
                goalBlock -> true
                else -> false
            }
        }

        fun shouldEnemyAttemptMove(index: Int, state: GameState): Boolean {
            return isEnemyBlockPresent(index) && state == GameState.IN_PROGRESS
        }

        private fun isEnemyBlockPresent(index: Int) = index != -1

        @OptIn(ExperimentalAnimationApi::class)
        fun levelGridTransitions(initialState: Char, targetState: Char, direction: Direction): ContentTransform {
            return when {
                initialState == emptyBlock && targetState == playerBlock-> { // Grey box turning Blue
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.End, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> height*2 } with // Blue slide in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed), shrinkTowards = Alignment.Top) // Grey shrink out
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> -height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.Start, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(playerSpeed, delayMillis = 0), expandFrom = Alignment.Bottom)  with // Blue expand in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed)) // Grey shrink out
                        }
                    }
                }
                initialState == playerBlock && targetState == emptyBlock -> { // Blue box turning Grey
                    return when(direction){
                        Direction.LEFT -> {
                            expandHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0 )) { height -> height + height/2} with // Grey expand in
                                    slideOutHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> -height } // Blue slide out
                        }
                        Direction.UP -> {
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> height*2 }  with // Grey slide in
                                    slideOutVertically(animationSpec = tween(1, delayMillis = playerSpeed)) { height -> height*2 } // Blue slide out
                        }
                        Direction.RIGHT -> {
                            expandHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0 )) { height -> (height + height/2)*-1} with // Grey expand in
                                    slideOutHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> height } // Blue slide out
                        }
                        Direction.DOWN -> { //G
                            expandVertically(animationSpec = tween(playerSpeed, delayMillis = 0), expandFrom = Alignment.Top)  with // Grey expand in
                                    slideOutVertically(animationSpec = tween(1, delayMillis = playerSpeed)) { height -> -height } // Blue slide out
                        }
                    }

                }
                initialState == emptyBlock && targetState == enemyBlock -> { // Grey box turning Red
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(enemySpeed, delayMillis = 0)) { height -> height }  + fadeIn() with
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = enemyDelay), shrinkTowards = Alignment.End, targetWidth = { 0 }) + fadeOut()
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(enemySpeed, delayMillis = 0)) { height -> height }  + fadeIn() with
                                    shrinkVertically(animationSpec = tween(enemySpeed, delayMillis = enemyDelay), shrinkTowards = Alignment.Bottom) + fadeOut()
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(enemySpeed, delayMillis = 0)) { height -> -height }  + fadeIn() with
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = enemyDelay), shrinkTowards = Alignment.Start, targetWidth = { 0 }) + fadeOut()
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(enemySpeed, delayMillis = 0), expandFrom = Alignment.Bottom)  + fadeIn() with
                                    shrinkVertically(animationSpec = tween(enemySpeed, delayMillis = enemyDelay), shrinkTowards = Alignment.Bottom) + fadeOut()
                        }
                    }
                }
                initialState == enemyBlock && targetState == emptyBlock -> { // Red box turning Grey
                    return when(direction){
                        Direction.LEFT -> {
                            expandHorizontally(animationSpec = tween(enemySpeed*2, delayMillis = enemyDelay )) { height -> height + height/2} + fadeIn() with
                                    slideOutHorizontally(animationSpec = tween(enemySpeed*2, delayMillis = 0)) { height -> -height } + fadeOut()
                        }
                        Direction.UP -> {
                            expandVertically(animationSpec = tween(1, delayMillis = 0), expandFrom = Alignment.Bottom)  + fadeIn() with
                                    shrinkVertically(animationSpec = tween(enemySpeed*2, delayMillis = 0), shrinkTowards = Alignment.Top) + fadeOut()
                        }
                        Direction.RIGHT -> {
                            expandHorizontally(animationSpec = tween(enemySpeed*2, delayMillis = 0 )) { height -> (height + height/2)*-1} + fadeIn() with
                                    slideOutHorizontally(animationSpec = tween(enemySpeed*2, delayMillis = 0)) { height -> height } + fadeOut()
                        }
                        Direction.DOWN -> { //G
                            expandVertically(animationSpec = tween(1, delayMillis = 0), expandFrom = Alignment.Top)  + fadeIn() with
                                    slideOutVertically(animationSpec = tween(375, delayMillis = 0)) { height -> -height } + fadeOut()
                        }
                    }
                }
                initialState == emptyBlock && targetState == movableBlock -> {
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.End, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> height*2 } with // Blue slide in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed), shrinkTowards = Alignment.Top) // Grey shrink out
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> -height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.Start, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(playerSpeed, delayMillis = 0), expandFrom = Alignment.Bottom)  with // Blue expand in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed)) // Grey shrink out
                        }
                    }
                }
                initialState == movableBlock && targetState == emptyBlock -> {
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> -height*2 }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.Start, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> -height } with // Blue slide in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed), shrinkTowards = Alignment.Bottom) // Grey shrink out
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> height*2 }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.End, targetWidth = { 0 })
                        }
                        Direction.DOWN -> {
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> height*2 } with // Blue slide in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed), shrinkTowards = Alignment.Top) // Grey shrink out
                        }
                    }
                }
                initialState == movableBlock && targetState == playerBlock -> {
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.End, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> height*2 } with // Blue slide in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed), shrinkTowards = Alignment.Top) // Grey shrink out
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> -height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.Start, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(playerSpeed, delayMillis = 0), expandFrom = Alignment.Bottom)  with // Blue expand in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed)) // Grey shrink out
                        }
                    }
                }
                initialState == goalBlock && targetState == enemyBlock -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }
                initialState == enemyBlock && targetState == goalBlock -> {
                    scaleIn() + fadeIn() with
                            scaleOut() + fadeOut()

                }
                initialState == playerBlock && targetState == enemyBlock -> { // Kill blue animation
                    scaleIn() + fadeIn() with
                            scaleOut() + fadeOut()

                }
                initialState == goalBlock && targetState == playerBlock -> { // Win Animation
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.End, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(playerSpeed, delayMillis = 0)){ height -> height*2 } with // Blue slide in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed), shrinkTowards = Alignment.Top) // Grey shrink out
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(playerSpeed, delayMillis = 0)) { height -> -height }  with // Blue slide in
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = playerDelay), shrinkTowards = Alignment.Start, targetWidth = { 0 }) // Grey shrink out
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(playerSpeed, delayMillis = 0), expandFrom = Alignment.Bottom)  with // Blue expand in
                                    shrinkVertically(animationSpec = tween(1, delayMillis = playerSpeed)) // Grey shrink out
                        }
                    }
                }
                initialState == playerBlock && targetState == goalBlock -> { // Win Animation 2
                    scaleIn() with
                            scaleOut()
                }
                    else -> {
                     fadeIn() with fadeOut()
                }
            }
        }
    }
}