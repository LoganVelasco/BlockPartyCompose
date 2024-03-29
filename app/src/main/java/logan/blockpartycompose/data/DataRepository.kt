package logan.blockpartycompose.data

import android.content.Context
import com.google.gson.Gson
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.data.models.LevelsDTO
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils.Companion.EMPTY_BLOCK
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val gameData: GameData) {

    private val gson = Gson()
    val levelsSets = mutableMapOf<String, List<Level>>()
    private val customFileName = "custom.json"

    fun getNewLevel(x: Int, y: Int): Level {
        return Level(
            id = -1,
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
            EMPTY_BLOCK
        }
    }

    fun getLevels(difficulty: LevelSet, context: Context): List<Level> {
        if (levelsSets[difficulty.name] != null) return levelsSets[difficulty.name]!!
        val json =
            context.assets.open("${difficulty.name.lowercase()}.json").bufferedReader().readText()
        val levels = gson.fromJson(json, LevelsDTO::class.java).convertToLevels()
        levelsSets[difficulty.name] = levels

        return levels
    }

    fun getCustomLevels(context: Context): List<Level> {
        val fileInputStream: FileInputStream?
        return try {
            fileInputStream = context.openFileInput(customFileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val json = BufferedReader(inputStreamReader).readText()
            if (json.isEmpty()) return emptyList()
            val levels = gson.fromJson(json, LevelsDTO::class.java).convertToLevels()
            levelsSets[LevelSet.CUSTOM.name] = levels
            levels
        } catch (e: FileNotFoundException) {
            File("${context.filesDir.path}/$customFileName").createNewFile()
            emptyList()
        }
    }

    fun addCustomLevel(level: Level, context: Context): Boolean {
        val levels = getCustomLevels(context)
        val newLevels = mutableListOf<Level>()
        newLevels.addAll(levels)
        newLevels.removeIf { it.id == level.id }
        newLevels.add(level)

        val data = gson.toJson(LevelsDTO.getDTO(newLevels))
        val fileOutputStream: FileOutputStream
        return try {
            fileOutputStream = context.openFileOutput(customFileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun deleteCustomLevel(id: Int, context: Context) {
        val newLevels = getCustomLevels(context).filter { it.id != id }
        val data = gson.toJson(LevelsDTO.getDTO(newLevels))
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(customFileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            levelsSets[LevelSet.CUSTOM.name] = newLevels
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

            LevelSet.CUSTOM -> {
                emptyList()
            }
        }
    }

    fun getTutorialStage(): Int {
        return gameData.tutorialStage
    }

    fun updateTutorialStage(stage: Int) {
        gameData.updateTutorialStage(stage)
    }

    fun updateLevelProgress(difficulty: LevelSet, level: Int, stars: Int) {
        gameData.updateProgress(difficulty, level, stars)
    }


    fun generateId(context: Context): Int {
        val highestCurrentID = getCustomLevels(context).maxByOrNull { it.id }?.id ?: 0

        return highestCurrentID + 1
    }

    fun getEmptyLayout(x: Int = 6, y: Int = 8): List<Char> {
        return getBlankLayout(x, y)
    }

    fun getColorScheme(): Int {
        return gameData.getColorScheme()
    }

    fun updateColorScheme(colorScheme: Int) {
        gameData.updateColorScheme(colorScheme)
    }

    fun getHintPreference(): Boolean {
        return gameData.getHintPreference()
    }

    fun updateHintPreference(enabled: Boolean) {
        gameData.updateHintPreference(enabled)
    }
}
