package mineSweeper
import kotlin.random.Random

class MineSweeperMap(private var height: Int, private var width: Int, private var mineCount: Int) {
    private var map = Array(height) { Array(width) {"■"} }
    private var renderMap = Array(height) { Array(width) {" □ "} }
    private var playerYPos: Int = 0
    private var playerXPos: Int = 0
    private var moveErrorMsg = ""

    init {
        placeMines()
        placeNumbers()
        renderMap[playerYPos][playerXPos] = "≬${renderMap[playerYPos][playerXPos].trim()}≬"
        renderMap()
    }

    private fun placeNumbers() {
        for (y in 0 until this.map.size){
            for(x in 0 until this.map[y].size){
                val adjacentMines = checkForMines(y,x)
                if(adjacentMines > 0 && this.map[y][x] != "*"){
                    this.map[y][x] = "$adjacentMines"
                }
            }
        }
    }

    private fun checkForMines(yPos: Int, xPos: Int): Int {
        var adjacentMines = 0
        for (y in yPos-1..yPos+1){
            for(x in xPos-1 .. xPos+1){
                if (y in 0 until height){
                    if (x in 0 until width){
                        if(this.map[y][x] == "*"){
                            adjacentMines++
                        }
                    }
                }
            }
        }
        return adjacentMines
    }

    private fun placeMines(){
        var minesPlaced = 0
        while(minesPlaced != mineCount){
            val minePosition = Random.nextInt(0, (width*height))
            val yPos = minePosition/width
            val xPos = minePosition%width
            this.map[yPos][xPos] = "*"
            minesPlaced++
        }
    }

    fun updatePlayerPos(direction: String, steps: Int){
        print(direction + steps)
        renderMap[playerYPos][playerXPos] = renderMap[playerYPos][playerXPos].replace("≬", " ")
        when (direction) {
            "up" -> if(playerYPos - steps < 0){moveErrorMsg = "Oops that was out of bounds"}else{playerYPos -= steps}
            "down" -> if(playerYPos + steps >= height){moveErrorMsg = "Oops that was out of bounds"}else{playerYPos += steps}
            "left" -> if(playerXPos - steps < 0){moveErrorMsg = "Oops that was out of bounds"}else{playerXPos -= steps}
            "right" -> if(playerXPos + steps >= width){moveErrorMsg = "Oops that was out of bounds"}else{playerXPos += steps}
            "open" -> openCell()
            else -> print("Oops something went wrong. Try again")
        }
        renderMap[playerYPos][playerXPos] = "≬${renderMap[playerYPos][playerXPos].trim()}≬"
    }

    private fun openCell() {
        renderMap[playerYPos][playerXPos] = " ${map[playerYPos][playerXPos]} "
        if(map[playerYPos][playerXPos] == "■"){
            openBlankCells(playerYPos, playerXPos)
        }
    }

    private fun openBlankCells(yPos: Int, xPos: Int) {
        for (y in yPos-1..yPos+1){
            for(x in xPos-1 .. xPos+1){
                if ( y in 0 until height){
                    if (x in 0 until width){
                        if(this.map[y][x] != "*" && (this.renderMap[y][x] == " □ " || this.renderMap[y][x] == "≬□≬")){
                            renderMap[y][x] = " ${this.map[y][x]} "
                            if(this.map[y][x] == "■"){
                                openBlankCells(y, x)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setErrorMsg(newMsg: String){
        moveErrorMsg = newMsg
    }

    fun renderMap(){
        print("\n\n\n\n\n\n\n\n\n\n\n" )

        for (y in 0 until this.renderMap.size){
            for(x in 0 until this.renderMap[y].size){
                print(this.renderMap[y][x])
            }
            print("\n")
        }
        println(moveErrorMsg)
        moveErrorMsg = ""
    }
}