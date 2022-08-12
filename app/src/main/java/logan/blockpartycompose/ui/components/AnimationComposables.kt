package logan.blockpartycompose.ui.components

import androidx.compose.animation.*
import androidx.compose.runtime.Composable

//
//@OptIn(ExperimentalAnimationApi::class)
//fun LevelGridTransitions(initialState: Char, targetState: Char) {
//    when {
//        initialState == '.' && targetState == 'b' -> {
//
//            slideInHorizontally { height -> height } + fadeIn() with
//                    slideOutHorizontally { height -> -height } + fadeOut()
//
//        }
//        initialState == '.' && targetState == 'r' -> {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        initialState == '.' && targetState == 'g' -> {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        initialState == 'b' && targetState == '.' -> {
//            scaleIn() + fadeIn() with
//                    slideOutHorizontally { height -> -height } + fadeOut()
//
//        }
//        initialState == 'r' && targetState == '.' -> {
//            scaleIn() + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        initialState == 'g' && targetState == '.' -> {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        initialState == 'r' && targetState == 'b' -> {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        initialState == 'b' && targetState == 'r' -> {
//            scaleIn() + fadeIn() with
//                    scaleOut() + fadeOut()
//
//        }
//        initialState == 'y' && targetState == 'r' -> {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        initialState == 'r' && targetState == 'y' -> {
//            scaleIn() + fadeIn() with
//                    scaleOut() + fadeOut()
//
//        }
//        initialState == 'y' && targetState == 'b' -> {
//            slideInVertically { height -> height } + fadeIn() with
//                    slideOutVertically { height -> -height } + fadeOut()
//
//        }
//        else -> {
//            slideInVertically { height -> -height } + fadeIn() with
//                    slideOutVertically { height -> height } + fadeOut()
//        }
//    }
//}