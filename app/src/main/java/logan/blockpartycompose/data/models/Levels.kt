package logan.blockpartycompose.data.models

import kotlinx.serialization.Serializable
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.level.GameStates


@Serializable
data class Level(
    val name: String,
    val x: Int,
    val y: Int,
    val initialBlocks: List<Char>,
) {
    var blueIndex: Int = initialBlocks.indexOf('b')
    var redIndex: Int = initialBlocks.indexOf('r')
    var state: GameStates = GameStates.IN_PROGRESS
    var blocks = initialBlocks.toMutableList()

    fun resetLevel(){
        blueIndex = initialBlocks.indexOf('b')
        redIndex = initialBlocks.indexOf('r')
        blocks = initialBlocks.toMutableList()
        state = GameStates.IN_PROGRESS
    }

}

@Serializable
data class Block(
    var color: BlockColor,
    val index: Int,
)

enum class BlockColor(val color: Char) {
    BLUE('b'),
    RED('r'),
    YELLOW('y'),
    GREEN('g'),
    BLACK('x'),
    GRAY('.'),
}