package logan.blockpartycompose.ui.screens.levelsMenu

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import javax.inject.Inject


@HiltViewModel
class LevelsMenuViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _state = MutableLiveData<LevelMenuState>()
    val state: LiveData<LevelMenuState> = _state


    private fun getLevels(levelSet: LevelSet, context: Context): List<Level> {
        if (levelSet == LevelSet.CUSTOM) return repo.getCustomLevels(context)
        return repo.getLevels(levelSet, context)
    }

    fun getProgress(levelSet: LevelSet): List<Int> {
        if (levelSet == LevelSet.CUSTOM) return emptyList()
        return repo.getLevelsProgress(levelSet)
    }

    fun deleteCustomLevelTriggered(id: Int, name: String) {
        _state.value = LevelMenuState(_state.value!!.levels, id, name)
    }

    fun deleteCustomLevel(id: Int, context: Context) {
        repo.deleteCustomLevel(id, context)
        setupState(LevelSet.CUSTOM, context)
    }

    fun setupState(levelSet: LevelSet, context: Context) {
        _state.value = LevelMenuState(getLevels(levelSet, context))
    }

    @Immutable
    data class LevelMenuState(
        val levels: List<Level>,
        val deleteId: Int? = null,
        val deleteName: String? = null,
        val forceUpdate: Boolean = false,
    )

}

