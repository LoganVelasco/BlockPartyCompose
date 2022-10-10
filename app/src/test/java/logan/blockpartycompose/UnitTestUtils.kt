package logan.blockpartycompose

import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

    fun createLevel(
        id: Int = 1,
        name: String = "Level 1",
        levelSet: LevelSet = LevelSet.EASY,
        x: Int = 4,
        y: Int = 6,
        initialBlocks: List<Char> = createBlocks(),
        minMoves: Int = 1
    ): Level {
        return Level(
            id,
            name,
            levelSet,
            x,
            y,
            initialBlocks,
            minMoves
        )
    }

    fun createBlocks(level : Int = 1):List<Char>{
        return when(level){
            1 -> {
                ".........p........g.....".toCharArray().toList()
            }
            2 -> {
                "e............p.....g....".toCharArray().toList()
            }
            3 -> {
                ".e...x...p.............g".toCharArray().toList()
            }
            4 -> {
                "............exp.........................g.......".toCharArray().toList()
            }
            5 -> {
                ".....g......e......x.x....x...p.................".toCharArray().toList()
            }
            else -> "........................".toCharArray().toList()
        }

    }
