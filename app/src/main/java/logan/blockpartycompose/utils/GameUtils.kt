package logan.blockpartycompose.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.level.Direction
import logan.blockpartycompose.ui.screens.level.GameState

class GameUtils {

    companion object {
        fun isTouchingBlue(index: Int, blueIndex: Int, x: Int): Boolean {
            if (isEdge(blueIndex, x))
                return isValidEdgeMove(index, blueIndex, x)

            if (blueIndex + 1 == index || blueIndex - 1 == index || blueIndex + x == index || blueIndex - x == index)
                return true

            return false
        }

        fun isEdge(index: Int, x: Int): Boolean {
            return index % x == 0 || index % x == x - 1
        }

        private fun isValidEdgeMove(index: Int, blueIndex: Int, x: Int): Boolean {
            return when {
                blueIndex % x == 0 -> {
                    blueIndex + 1 == index || blueIndex + x == index || blueIndex - x == index
                }
                blueIndex % x == x - 1 -> {
                    blueIndex - 1 == index || blueIndex + x == index || blueIndex - x == index
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

        fun isValidRedMove(newIndexType: Char): Boolean {
            return when (newIndexType) {
                '.' -> true
                'b' -> true
                'y' -> true
                else -> false
            }
        }

        fun shouldRedAttemptMove(index: Int, state: GameState): Boolean {
            return isRedBlockPresent(index) && state == GameState.IN_PROGRESS
        }

        private fun isRedBlockPresent(index: Int) = index != -1

        @OptIn(ExperimentalAnimationApi::class)
        fun levelGridTransitions(initialState: Char, targetState: Char, direction: Direction): ContentTransform {
            return when {
//                initialState == '.' && targetState == 'b' || initialState == '.' && targetState == 'r' -> {
                initialState == '.' && targetState != 'r'-> {
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(500, delayMillis = 0)) { height -> height }  with
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = 200), shrinkTowards = Alignment.End, targetWidth = { 0 })
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(500, delayMillis = 0)) { height -> height }  with
                                    shrinkVertically(animationSpec = tween(500, delayMillis = 200), shrinkTowards = Alignment.Bottom)
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(500, delayMillis = 0)) { height -> -height }  with
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = 200), shrinkTowards = Alignment.Start, targetWidth = { 0 })
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(500, delayMillis = 0), expandFrom = Alignment.Bottom)  with
                                    shrinkVertically(animationSpec = tween(500, delayMillis = 200), shrinkTowards = Alignment.Bottom)
                        }
                    }
                }
                initialState != '.' && targetState != 'r' -> {
                    return when(direction){
                        Direction.LEFT -> {
                            expandHorizontally(animationSpec = tween(500, delayMillis = 200 )) { height -> height + height/2} with
                                    slideOutHorizontally(animationSpec = tween(500, delayMillis = 0)) { height -> -height }
                        }
                        Direction.UP -> {
                            expandVertically(animationSpec = tween(1, delayMillis = 0), expandFrom = Alignment.Bottom)  with
                                    shrinkVertically(animationSpec = tween(500, delayMillis = 0), shrinkTowards = Alignment.Top)
                        }
                        Direction.RIGHT -> {
                            expandHorizontally(animationSpec = tween(500, delayMillis = 0 )) { height -> (height + height/2)*-1} with
                                    slideOutHorizontally(animationSpec = tween(500, delayMillis = 0)) { height -> height }
                        }
                        Direction.DOWN -> { //G
                            expandVertically(animationSpec = tween(1, delayMillis = 0), expandFrom = Alignment.Top)  with
                                    slideOutVertically(animationSpec = tween(750, delayMillis = 0)) { height -> -height }
                        }
                    }

                }
                initialState == '.' && targetState == 'r' -> {
                    return when(direction){
                        Direction.LEFT -> {
                            slideInHorizontally(animationSpec = tween(500, delayMillis = 0)) { height -> height }  + fadeIn() with
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = 200), shrinkTowards = Alignment.End, targetWidth = { 0 }) + fadeOut()
                        }
                        Direction.UP -> { //G
                            slideInVertically(animationSpec = tween(500, delayMillis = 0)) { height -> height }  + fadeIn() with
                                    shrinkVertically(animationSpec = tween(500, delayMillis = 200), shrinkTowards = Alignment.Bottom) + fadeOut()
                        }
                        Direction.RIGHT -> {
                            slideInHorizontally(animationSpec = tween(500, delayMillis = 0)) { height -> -height }  + fadeIn() with
                                    shrinkHorizontally(animationSpec = tween(1, delayMillis = 200), shrinkTowards = Alignment.Start, targetWidth = { 0 }) + fadeOut()
                        }
                        Direction.DOWN -> {
                            expandVertically(animationSpec = tween(500, delayMillis = 0), expandFrom = Alignment.Bottom)  + fadeIn() with
                                    shrinkVertically(animationSpec = tween(500, delayMillis = 200), shrinkTowards = Alignment.Bottom) + fadeOut()
                        }
                    }
                }
                initialState == 'r' && targetState == '.' -> {
                    return when(direction){
                        Direction.LEFT -> {
                            expandHorizontally(animationSpec = tween(1000, delayMillis = 200 )) { height -> height + height/2} + fadeIn() with
                                    slideOutHorizontally(animationSpec = tween(1000, delayMillis = 0)) { height -> -height } + fadeOut()
                        }
                        Direction.UP -> {
                            expandVertically(animationSpec = tween(1, delayMillis = 0), expandFrom = Alignment.Bottom)  + fadeIn() with
                                    shrinkVertically(animationSpec = tween(1000, delayMillis = 0), shrinkTowards = Alignment.Top) + fadeOut()
                        }
                        Direction.RIGHT -> {
                            expandHorizontally(animationSpec = tween(1000, delayMillis = 0 )) { height -> (height + height/2)*-1} + fadeIn() with
                                    slideOutHorizontally(animationSpec = tween(1000, delayMillis = 0)) { height -> height } + fadeOut()
                        }
                        Direction.DOWN -> { //G
                            expandVertically(animationSpec = tween(1, delayMillis = 0), expandFrom = Alignment.Top)  + fadeIn() with
                                    slideOutVertically(animationSpec = tween(750, delayMillis = 0)) { height -> -height } + fadeOut()
                        }
                    }

                }
//                initialState == '.' && targetState == 'r' -> {
//                    slideInVertically { height -> height } + fadeIn() with
//                            slideOutVertically { height -> -height } + fadeOut()
//
//                }
//                initialState == 'r' && targetState == '.' -> {
//                    scaleIn() + fadeIn() with
//                            slideOutVertically { height -> -height } + fadeOut()
//
//                }
                initialState == '.' && targetState == 'g' -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }
                initialState == 'g' && targetState == '.' -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }
                initialState == 'r' && targetState == 'b' -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }
                initialState == 'b' && targetState == 'r' -> {
                    scaleIn() + fadeIn() with
                            scaleOut() + fadeOut()

                }
                initialState == 'y' && targetState == 'r' -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }
                initialState == 'r' && targetState == 'y' -> {
                    scaleIn() + fadeIn() with
                            scaleOut() + fadeOut()

                }
                initialState == 'y' && targetState == 'b' -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }
                else -> {
                    slideInVertically { height -> -height } + fadeIn() with
                            slideOutVertically { height -> height } + fadeOut()
                }
            }
        }
    }
}