package logan.blockpartycompose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.level.LevelViewModel
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LevelsMenuViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var mockRepo: DataRepository
    private lateinit var viewModel: LevelViewModel

    @Before
    fun setup() {
        mockRepo = mockk()
        viewModel = LevelViewModel(mockRepo)
    }

    @Test
    fun setupLevel_setsUpCorrectLevel() {
        val blocks = createBlocks(1)
        val level1 = createLevel(id = 1, initialBlocks = blocks)
        every { mockRepo.levelsSets[LevelSet.EASY.name] } returns listOf(
            level1,
            createLevel(id = 2, initialBlocks = createBlocks(2)),
            createLevel(id = 3, initialBlocks = createBlocks(3)),
        )

        viewModel.setupLevel(LevelSet.EASY, 1)

        assert(viewModel.level == level1)
        assert(viewModel.state.value!!.blocks == blocks)
        assert(viewModel.state.value!!.gameState == GameState.IN_PROGRESS)
        assert(viewModel.state.value!!.movesUsed == 0)
    }

    @Test
    fun setupLevel_resetsPlayedLevel() {
        val blocks = createBlocks(2)
        val level1 = createLevel(id = 1, initialBlocks = blocks)
        level1.blocks[0] = '.'
        level1.blocks[13] = '.'
        level1.blocks[4] = 'e'
        level1.state = GameState.FAILED
        level1.playerIndex = 4
        level1.enemyIndex = 4

        every { mockRepo.levelsSets[LevelSet.EASY.name] } returns listOf(
            level1
        )

        viewModel.setupLevel(LevelSet.EASY, 1)

        assert(viewModel.level.blocks == blocks)
        assert(viewModel.level.state == GameState.IN_PROGRESS)
        assert(viewModel.level.playerIndex == 13)
        assert(viewModel.level.enemyIndex == 0)
    }


    @Test
    fun nonTouchingBlockClickIgnored(){
        viewModel.level = createLevel()
        viewModel.state.observeForever { assert(false) }

        viewModel.blockClicked('e', 0)

    }

    @Test
    fun clickingEnemyPostFailure(){
        val blocks = "peg.....................".toCharArray().toList()

        viewModel.level = createLevel(initialBlocks = blocks)
        viewModel.state.observeForever { assert(it.gameState == GameState.FAILED) }

        viewModel.blockClicked('e', 1)

    }

    // TODO: fix to test coroutines
    @Test
    fun clickingGoalPostSuccess(){
        val blocks = "pg......................".toCharArray().toList()

        viewModel.level = createLevel(initialBlocks = blocks)

//        viewModel.blockClicked('g', 1)

    }

}