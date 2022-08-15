package logan.blockpartycompose.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameData @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("levels", Context.MODE_PRIVATE)


    var easyLevelProgress = mutableListOf<Int>()
    var mediumLevelProgress = mutableListOf<Int>()
    var hardLevelProgress = mutableListOf<Int>()

    var showMediumAnimation = false
    var showHardAnimation = false

    init {
        prefs.getString(LevelSet.EASY.name, "").also { easyProgress ->
            if (!easyProgress.isNullOrEmpty()) {
                easyLevelProgress = easyProgress.map { it.code - 48 } as MutableList<Int>
            }
            while (easyLevelProgress.size <= 10) easyLevelProgress.add(0)
        }
        prefs.getString(LevelSet.MEDIUM.name, "").also { mediumProgress ->
            if (!mediumProgress.isNullOrEmpty()) {
                mediumLevelProgress = mediumProgress.map { it.code - 48 } as MutableList<Int>
            }
            while (mediumLevelProgress.size <= 10) mediumLevelProgress.add(0)
        }
        prefs.getString(LevelSet.HARD.name, "").also { hardProgress ->
            if (!hardProgress.isNullOrEmpty()) {
                hardLevelProgress = hardProgress.map { it.code - 48 } as MutableList<Int>
            }
            while (hardLevelProgress.size <= 10) hardLevelProgress.add(0)
        }
        showMediumAnimation = prefs.getBoolean(LevelSet.MEDIUM.name, false)
        showHardAnimation = prefs.getBoolean(LevelSet.HARD.name, false)
    }

    private fun updateLevel(
        difficulty: LevelSet,
        currentProgress: String,
        level: Int,
        stars: Int
    ): Boolean {
        if (currentProgress[level - 1].code - 48 < stars) {
            val newProgress =
                StringBuilder(currentProgress).also {
                    it.setCharAt(
                        level - 1,
                        Character.forDigit(stars, 10)
                    )
                }.toString()
            prefs.edit().putString(difficulty.name, newProgress)
                .apply()
            return true
        }
        return false
    }

    fun updateProgress(difficulty: LevelSet, level: Int, stars: Int) {
        var currentProgress = prefs.getString(difficulty.name, "")
        if (currentProgress.isNullOrEmpty()) { // TODO update to scale
            currentProgress = "0000000000"
        }
        when (difficulty) {
            LevelSet.EASY -> {
                if (updateLevel(difficulty, currentProgress, level, stars))
                    easyLevelProgress[level - 1] = stars
            }
            LevelSet.MEDIUM -> {
                if (updateLevel(difficulty, currentProgress, level, stars))
                    mediumLevelProgress[level - 1] = stars
            }
            LevelSet.HARD -> {
                if (updateLevel(difficulty, currentProgress, level, stars))
                    hardLevelProgress[level - 1] = stars
            }
            LevelSet.CUSTOM -> {}
        }
    }

    fun enableDifficultyAnimation(difficulty: LevelSet) {
        prefs.edit().putBoolean(difficulty.name, true)
        when(difficulty){
            LevelSet.MEDIUM -> showMediumAnimation = true
            LevelSet.HARD -> showHardAnimation = true
            else -> {}
        }
    }
    fun disableAnimations(){
        prefs.edit().putBoolean(LevelSet.MEDIUM.name, false)
        prefs.edit().putBoolean(LevelSet.HARD.name, false)
        showMediumAnimation = false
        showHardAnimation = false
    }
}