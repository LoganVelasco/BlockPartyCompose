package logan.blockpartycompose.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameData @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.levels), Context.MODE_PRIVATE)


    val easyLevelProgress: MutableList<Int>
        get() {
        return if (!prefs.getString(LevelSet.EASY.name, "").isNullOrEmpty()) {
            prefs.getString(LevelSet.EASY.name, "")!!.map { it.code - 48 } as MutableList<Int>
        } else {
            mutableListOf<Int>().apply {
                while (this.size <= 10) this.add(0)
            }
        }
    }

    val mediumLevelProgress: MutableList<Int>
        get() {
            return if (!prefs.getString(LevelSet.MEDIUM.name, "").isNullOrEmpty()) {
                prefs.getString(LevelSet.MEDIUM.name, "")!!.map { it.code - 48 } as MutableList<Int>
            } else {
                mutableListOf<Int>().apply {
                    while (this.size <= 10) this.add(0)
                }
            }
        }

    val hardLevelProgress: MutableList<Int>
        get() {
            return if (!prefs.getString(LevelSet.HARD.name, "").isNullOrEmpty()) {
                prefs.getString(LevelSet.HARD.name, "")!!.map { it.code - 48 } as MutableList<Int>
            } else {
                mutableListOf<Int>().apply {
                    while (this.size <= 10) this.add(0)
                }
            }
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
                updateLevel(difficulty, currentProgress, level, stars)
            }
            LevelSet.MEDIUM -> {
                updateLevel(difficulty, currentProgress, level - 10, stars)
            }
            LevelSet.HARD -> {
                updateLevel(difficulty, currentProgress, level - 20, stars)
            }
            LevelSet.CUSTOM -> {}
        }
    }

}