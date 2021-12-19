package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.utils.UserProgressService
import javax.inject.Inject

@HiltViewModel
class LevelsMenuViewModel @Inject constructor(
    private val repo: DataRepository,
    private val userProgressService: UserProgressService,
) : ViewModel() {

    private var _levels = MutableLiveData<List<Level>>()
    val levels: LiveData<List<Level>> = _levels


    fun getLevels(levelSet: LevelSet) {
        _levels.postValue(repo.getLevels(levelSet))
    }
}

