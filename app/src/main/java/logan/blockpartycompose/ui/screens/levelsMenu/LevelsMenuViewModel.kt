package logan.blockpartycompose.ui.screens.levelsMenu

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import javax.inject.Inject


@HiltViewModel
class LevelsMenuViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    fun getLevels(levelSet: LevelSet, context: Context): List<Level> {
        return repo.getLevels(levelSet, context)
    }

    fun getProgress(levelSet: LevelSet): List<Int> {
        return repo.getLevelsProgress(levelSet)
    }
}

