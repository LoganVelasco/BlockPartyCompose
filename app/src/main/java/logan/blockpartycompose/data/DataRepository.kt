package logan.blockpartycompose.data

import android.content.Context
import com.google.gson.Gson
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.data.models.LevelsDTO
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val gameData: GameData) {

    private val gson = Gson()
    val levelsSets = mutableMapOf <String, List<Level>>()

    fun getNewLevel(x: Int, y: Int): Level {
        return Level(
            id = 0,
            name = "",
            levelSet = LevelSet.CUSTOM,
            x = x,
            y = y,
            initialBlocks = getBlankLayout(x, y),
            minMoves = 0
        )
    }

    private fun getBlankLayout(x: Int, y: Int): List<Char> {
        return MutableList(x * y) {
            '.'
        }
    }

    fun getLevels(difficulty: LevelSet, context: Context): List<Level> {
        if(levelsSets[difficulty.name] != null) return levelsSets[difficulty.name]!!
        val json = context.assets.open("${difficulty.name.lowercase()}.json").bufferedReader().readText()
        val levels = gson.fromJson(json, LevelsDTO::class.java).convertToLevels()
        levelsSets[difficulty.name] = levels
        return levels
    }

    fun getDifficultyProgress(): List<Int> {
        return listOf(
            gameData.easyLevelProgress.sum(),
            gameData.mediumLevelProgress.sum(),
            gameData.hardLevelProgress.sum()
        )
    }

    fun getLevelsProgress(difficulty: LevelSet): List<Int> {
        return when (difficulty) {
            LevelSet.EASY -> {
                gameData.easyLevelProgress
            }
            LevelSet.MEDIUM -> {
                gameData.mediumLevelProgress
            }
            LevelSet.HARD -> {
                gameData.hardLevelProgress
            }
            LevelSet.CUSTOM -> TODO()
        }
    }

    fun updateLevelProgress(difficulty: LevelSet, level: Int, stars: Int) {
        gameData.updateProgress(difficulty, level, stars)
    }

}
