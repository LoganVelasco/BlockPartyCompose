package logan.blockpartycompose.utils

import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject

class GeneratorService @Inject constructor(
//    val levelSolver: LevelSolver
) {

    private lateinit var newLevel:MutableList<Char>

    fun generateLevel(): MutableList<Char> {
        val levelSolver: LevelSolver = LevelSolver()
        newLevel = mutableListOf(
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.',
            '.', '.','.', '.','.', '.', 'c'
        )
        addBlock( 'b')
        addBlock( 'r')
        addBlock( 'y')
        addBlocks('g')
        addBlocks('x')
//        levelSolver.solve(newLevel as ArrayList<Char>)

        // recursively call generate until valid level created
        if(!levelSolver.isSolvable(newLevel as ArrayList<Char>)){
            return generateLevel()
        }
        newLevel.removeLast()
        return newLevel
    }

    private fun addBlock(block: Char) {
        var randomPosition = (0..47).random()
        while(newLevel[randomPosition] != '.') {
            randomPosition = (0..47).random()
        }
        newLevel[randomPosition] = block
    }
    private fun addBlocks(block: Char) {
        var amountOfBlocks = ThreadLocalRandom.current().nextInt(0, 6)
        if (amountOfBlocks == 6) amountOfBlocks = (0..12).random()
        if (amountOfBlocks == 12) amountOfBlocks = (0..18).random()
        while (amountOfBlocks-- != 0) {
            addBlock(block)
        }

    }


}
