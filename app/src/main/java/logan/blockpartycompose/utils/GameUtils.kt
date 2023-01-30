package logan.blockpartycompose.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import logan.blockpartycompose.ui.screens.level.Direction
import logan.blockpartycompose.ui.screens.level.GameState

class GameUtils {

    companion object {
        const val PLAYER_BLOCK = 'p'
        const val ENEMY_BLOCK = 'e'
        const val MOVABLE_BLOCK = 'm'
        const val UNMOVABLE_BLOCK = 'x'
        const val GOAL_BLOCK = 'g'
        const val EMPTY_BLOCK = '.'
        const val MEDIUM_STAR_REQUIREMENT = 25
        const val MEDIUM_MAX_STARS = 15
        const val HARD_STAR_REQUIREMENT = 40
        const val HARD_MAX_STARS = 15
        val FINAL_LEVELS = listOf(9, 14, 19)
        const val HELP_CARD_COUNT_INITIAL = 6
        const val HELP_CARD_COUNT_SECOND = 8
        const val HELP_CARD_MOVE_INITIAL = 6
        const val HELP_CARD_MOVE_SECOND = 7
        private const val PLAYER_SPEED = 350
        private const val PLAYER_DELAY = 100
        private const val ENEMY_SPEED = 250
        private const val ENEMY_DELAY = 100

        fun isTouching(index: Int, index2: Int, x: Int): Boolean {
            // checks for out of bounds indices
            if (index < 0 || index2 < 0) return false
            if (x == 4) {
                if (index >= 24 || index2 >= 24) return false
            } else {
                if (index >= 48 || index2 >= 48) return false
            }

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

        fun getDirection(initial: Int, target: Int, x: Int): Direction? {
            return when {
                isInSameColumn(
                    initial,
                    target,
                    x
                ) -> if (initial < target) Direction.DOWN else Direction.UP

                isInSameRow(
                    initial,
                    target,
                    x
                ) -> if (initial > target) Direction.LEFT else Direction.RIGHT

                else -> return null
            }
        }

        fun isValidEnemyMove(newIndexType: Char): Boolean {
            return when (newIndexType) {
                EMPTY_BLOCK -> true
                PLAYER_BLOCK -> true
                GOAL_BLOCK -> true
                else -> false
            }
        }

        fun shouldEnemyAttemptMove(index: Int, state: GameState): Boolean {
            return isEnemyBlockPresent(index) && state == GameState.IN_PROGRESS
        }

        private fun isEnemyBlockPresent(index: Int) = index != -1

        @OptIn(ExperimentalAnimationApi::class)
        fun levelGridTransitions(
            initialState: Char,
            targetState: Char,
            direction: Direction
        ): ContentTransform {
            return when {
                initialState == EMPTY_BLOCK && targetState == PLAYER_BLOCK -> { // Grey box turning Blue
                    return when (direction) {
                        Direction.LEFT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.End,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.UP -> { //G
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Blue slide in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        ), shrinkTowards = Alignment.Top
                                    ) // Grey shrink out
                        }

                        Direction.RIGHT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.Start,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.DOWN -> {
                            expandVertically(
                                animationSpec = tween(PLAYER_SPEED, delayMillis = 0),
                                expandFrom = Alignment.Bottom
                            ) with // Blue expand in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        )
                                    ) // Grey shrink out
                        }
                    }
                }

                initialState == PLAYER_BLOCK && targetState == EMPTY_BLOCK -> { // Blue box turning Grey
                    return when (direction) {
                        Direction.LEFT -> {
                            expandHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height + height / 2 } with // Grey expand in
                                    slideOutHorizontally(
                                        animationSpec = tween(
                                            PLAYER_SPEED,
                                            delayMillis = 0
                                        )
                                    ) { height -> -height } // Blue slide out
                        }

                        Direction.UP -> {
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Grey slide in
                                    slideOutVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        )
                                    ) { height -> height * 2 } // Blue slide out
                        }

                        Direction.RIGHT -> {
                            expandHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> (height + height / 2) * -1 } with // Grey expand in
                                    slideOutHorizontally(
                                        animationSpec = tween(
                                            PLAYER_SPEED,
                                            delayMillis = 0
                                        )
                                    ) { height -> height } // Blue slide out
                        }

                        Direction.DOWN -> { //G
                            expandVertically(
                                animationSpec = tween(PLAYER_SPEED, delayMillis = 0),
                                expandFrom = Alignment.Top
                            ) with // Grey expand in
                                    slideOutVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        )
                                    ) { height -> -height } // Blue slide out
                        }
                    }

                }

                initialState == EMPTY_BLOCK && targetState == ENEMY_BLOCK -> { // Grey box turning Red
                    return when (direction) {
                        Direction.LEFT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    ENEMY_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height } + fadeIn() with
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = ENEMY_DELAY
                                        ),
                                        shrinkTowards = Alignment.End,
                                        targetWidth = { 0 }) + fadeOut()
                        }

                        Direction.UP -> { //G
                            slideInVertically(
                                animationSpec = tween(
                                    ENEMY_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height } + fadeIn() with
                                    shrinkVertically(
                                        animationSpec = tween(
                                            ENEMY_SPEED,
                                            delayMillis = ENEMY_DELAY
                                        ), shrinkTowards = Alignment.Bottom
                                    ) + fadeOut()
                        }

                        Direction.RIGHT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    ENEMY_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height } + fadeIn() with
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = ENEMY_DELAY
                                        ),
                                        shrinkTowards = Alignment.Start,
                                        targetWidth = { 0 }) + fadeOut()
                        }

                        Direction.DOWN -> {
                            expandVertically(
                                animationSpec = tween(ENEMY_SPEED, delayMillis = 0),
                                expandFrom = Alignment.Bottom
                            ) + fadeIn() with
                                    shrinkVertically(
                                        animationSpec = tween(
                                            ENEMY_SPEED,
                                            delayMillis = ENEMY_DELAY
                                        ), shrinkTowards = Alignment.Bottom
                                    ) + fadeOut()
                        }
                    }
                }

                initialState == ENEMY_BLOCK && targetState == EMPTY_BLOCK -> { // Red box turning Grey
                    return when (direction) {
                        Direction.LEFT -> {
                            expandHorizontally(
                                animationSpec = tween(
                                    ENEMY_SPEED * 2,
                                    delayMillis = ENEMY_DELAY
                                )
                            ) { height -> height + height / 2 } + fadeIn() with
                                    slideOutHorizontally(
                                        animationSpec = tween(
                                            ENEMY_SPEED * 2,
                                            delayMillis = 0
                                        )
                                    ) { height -> -height } + fadeOut()
                        }

                        Direction.UP -> {
                            expandVertically(
                                animationSpec = tween(1, delayMillis = 0),
                                expandFrom = Alignment.Bottom
                            ) + fadeIn() with
                                    shrinkVertically(
                                        animationSpec = tween(
                                            ENEMY_SPEED * 2,
                                            delayMillis = 0
                                        ), shrinkTowards = Alignment.Top
                                    ) + fadeOut()
                        }

                        Direction.RIGHT -> {
                            expandHorizontally(
                                animationSpec = tween(
                                    ENEMY_SPEED * 2,
                                    delayMillis = 0
                                )
                            ) { height -> (height + height / 2) * -1 } + fadeIn() with
                                    slideOutHorizontally(
                                        animationSpec = tween(
                                            ENEMY_SPEED * 2,
                                            delayMillis = 0
                                        )
                                    ) { height -> height } + fadeOut()
                        }

                        Direction.DOWN -> { //G
                            expandVertically(
                                animationSpec = tween(1, delayMillis = 0),
                                expandFrom = Alignment.Top
                            ) + fadeIn() with
                                    slideOutVertically(
                                        animationSpec = tween(
                                            375,
                                            delayMillis = 0
                                        )
                                    ) { height -> -height } + fadeOut()
                        }
                    }
                }

                initialState == EMPTY_BLOCK && targetState == MOVABLE_BLOCK -> {
                    return when (direction) {
                        Direction.LEFT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.End,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.UP -> { //G
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Blue slide in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        ), shrinkTowards = Alignment.Top
                                    ) // Grey shrink out
                        }

                        Direction.RIGHT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.Start,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.DOWN -> {
                            expandVertically(
                                animationSpec = tween(PLAYER_SPEED, delayMillis = 0),
                                expandFrom = Alignment.Bottom
                            ) with // Blue expand in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        )
                                    ) // Grey shrink out
                        }
                    }
                }

                initialState == MOVABLE_BLOCK && targetState == EMPTY_BLOCK -> {
                    return when (direction) {
                        Direction.LEFT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height * 2 } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.Start,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.UP -> { //G
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height } with // Blue slide in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        ), shrinkTowards = Alignment.Bottom
                                    ) // Grey shrink out
                        }

                        Direction.RIGHT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ), shrinkTowards = Alignment.End, targetWidth = { 0 })
                        }

                        Direction.DOWN -> {
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Blue slide in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        ), shrinkTowards = Alignment.Top
                                    ) // Grey shrink out
                        }
                    }
                }

                initialState == MOVABLE_BLOCK && targetState == PLAYER_BLOCK -> {
                    return when (direction) {
                        Direction.LEFT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.End,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.UP -> { //G
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Blue slide in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        ), shrinkTowards = Alignment.Top
                                    ) // Grey shrink out
                        }

                        Direction.RIGHT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.Start,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.DOWN -> {
                            expandVertically(
                                animationSpec = tween(PLAYER_SPEED, delayMillis = 0),
                                expandFrom = Alignment.Bottom
                            ) with // Blue expand in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        )
                                    ) // Grey shrink out
                        }
                    }
                }

                initialState == GOAL_BLOCK && targetState == ENEMY_BLOCK -> {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()

                }

                initialState == ENEMY_BLOCK && targetState == GOAL_BLOCK -> {
                    scaleIn() + fadeIn() with
                            scaleOut() + fadeOut()

                }

                initialState == PLAYER_BLOCK && targetState == ENEMY_BLOCK -> { // Kill blue animation
                    scaleIn() + fadeIn() with
                            scaleOut() + fadeOut()

                }

                initialState == GOAL_BLOCK && targetState == PLAYER_BLOCK -> { // Win Animation
                    return when (direction) {
                        Direction.LEFT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.End,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.UP -> { //G
                            slideInVertically(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> height * 2 } with // Blue slide in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        ), shrinkTowards = Alignment.Top
                                    ) // Grey shrink out
                        }

                        Direction.RIGHT -> {
                            slideInHorizontally(
                                animationSpec = tween(
                                    PLAYER_SPEED,
                                    delayMillis = 0
                                )
                            ) { height -> -height } with // Blue slide in
                                    shrinkHorizontally(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_DELAY
                                        ),
                                        shrinkTowards = Alignment.Start,
                                        targetWidth = { 0 }) // Grey shrink out
                        }

                        Direction.DOWN -> {
                            expandVertically(
                                animationSpec = tween(PLAYER_SPEED, delayMillis = 0),
                                expandFrom = Alignment.Bottom
                            ) with // Blue expand in
                                    shrinkVertically(
                                        animationSpec = tween(
                                            1,
                                            delayMillis = PLAYER_SPEED
                                        )
                                    ) // Grey shrink out
                        }
                    }
                }

                initialState == PLAYER_BLOCK && targetState == GOAL_BLOCK -> { // Win Animation 2
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