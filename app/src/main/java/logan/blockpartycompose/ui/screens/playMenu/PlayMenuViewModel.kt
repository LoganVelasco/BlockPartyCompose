package logan.blockpartycompose.ui.screens.playMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.ui.screens.level.LevelState
import javax.inject.Inject

@HiltViewModel
class PlayMenuViewModel @Inject constructor(
    private val repo: DataRepository
) : ViewModel() {

    private var _flags = MutableLiveData<List<Boolean>>()
    val flags: LiveData<List<Boolean>> = _flags

    fun getProgress(): List<Int> {
        return repo.getDifficultyProgress()
    }

}