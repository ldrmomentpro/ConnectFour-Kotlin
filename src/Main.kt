package connectfour

class ConnectFour(
    private val firstPlayer: String,
    private val secondPlayer: String,
    private val rows: Int,
    private val columns: Int
) {

    private var list = MutableList(rows) { MutableList(columns) { " " } }
    private var state = TURN.FIRST
    private var winFP = false
    private var winSP = false
    private var draw = false
    private var cellValue = ""
    private var scoreFP = 0
    private var scoreSP = 0

    enum class TURN { FIRST, SECOND }
    enum class OUTPUT { END, INCORRECT, CORRECT }

    fun run() {
        var input = ""

        while (true) {
            println("Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:")
            input = readLine()!!
            when (checkSingleOrMulti(input)) {
                OUTPUT.CORRECT -> {
                    if (input.isEmpty()) input = "1"
                    break
                }
                OUTPUT.END -> exit()
                OUTPUT.INCORRECT -> {}
            }
        }
        println("$firstPlayer VS $secondPlayer")
        println("$rows X $columns board")
        if (input != "1") println("Total $input games")
        for (i in 1..input.toInt()) {
            list = MutableList(rows) { MutableList(columns) { " " } }
            if (input.toInt() > 1) println("Game #$i") else println("Single game")
            boxDrawing()

            val firstStr = "$firstPlayer's turn:"
            val secondStr = "$secondPlayer's turn:"

            while (!checkWin()) {
                println(if (state == TURN.FIRST) firstStr else secondStr)
                when (checkInput(readLine()!!)) {
                    OUTPUT.CORRECT -> boxDrawing()
                    OUTPUT.END -> return
                    OUTPUT.INCORRECT -> {}
                }
            }
            if (input.toInt() > 1) {
                println("Score\n" +
                        "$firstPlayer: $scoreFP $secondPlayer: $scoreSP")
            }
        }
        exit()
    }

    private fun checkWin(): Boolean {
        draw = true
        if (horizontalCheck() || verticalCheck() || ascendingDiagonalCheck() || descendingDiagonalCheck()) {
            println("Player ${if (winFP) firstPlayer else secondPlayer} won")
            if (winFP) {
                scoreFP += 2
                winFP = false
            } else {
                scoreSP += 2
                winSP = false
            }
            return true
        }
        if (draw) {
            println("It is a draw")
            scoreFP++
            scoreSP++
            return true
        }
        return false
    }

    private fun horizontalCheck(): Boolean {
        for (i in 0 until rows) {
            for (j in 0 until columns - 3) {
                cellValue = list[i][j]
                if (cellValue == " ") draw = false
                if (cellValue !== " " && list[i][j + 1 ] === cellValue && list[i][j + 2] === cellValue &&
                    list[i][j + 3] === cellValue) {
                    if (cellValue == "o") winFP = true else winSP = true
                    return true
                }
            }
        }
        return false
    }

    private fun verticalCheck(): Boolean {
        for (j in 0 until columns) {
            for (i in 0 until rows - 3) {
                cellValue = list[i][j]
                if (cellValue == " ") draw = false
                if (cellValue !== " " && list[i+1][j] === cellValue && list[i+2][j] === cellValue &&
                    list[i+3][j] === cellValue) {
                    if (cellValue == "o") winFP = true else winSP = true
                    return true
                }
            }
        }
        return false
    }

    private fun ascendingDiagonalCheck(): Boolean {
        for (i in 3 until rows) {
            for (j in 0 until columns - 3) {
                cellValue = list[i][j]
                if (cellValue == " ") draw = false
                if (cellValue !== " " && list[i - 1][j + 1] === cellValue && list[i - 2][j + 2] === cellValue &&
                    list[i - 3][j + 3] === cellValue
                ) {
                    if (cellValue == "o") winFP = true else winSP = true
                    return true
                }
            }
        }
        return false
    }

    private fun descendingDiagonalCheck(): Boolean {
        for (i in 3 until rows) {
            for (j in 3 until columns) {
                cellValue = list[i][j]
                if (cellValue == " ") draw = false
                if (cellValue !== " " && list[i - 1][j - 1] === cellValue && list[i - 2][j - 2] === cellValue &&
                    list[i - 3][j - 3] === cellValue
                ) {
                    if (cellValue == "o") winFP = true else winSP = true
                    return true
                }
            }
        }
        return false
    }

    private fun exit() {
        println("Game over!")
    }

    private fun checkSingleOrMulti(input: String): OUTPUT {
        if (input == "end") {
            exit()
            return OUTPUT.END
        }
        val num: Int = try {
            if (input.isEmpty()) {
                1
            } else {
                input.toInt()
            }
        } catch (e: Exception) {
            println("Invalid input")
            return OUTPUT.INCORRECT
        }
        if (num > 0) {
            return OUTPUT.CORRECT
        } else println("Invalid input")
        return OUTPUT.INCORRECT
    }

    private fun checkInput(input: String): OUTPUT {
        if (input == "end") {
            exit()
            return OUTPUT.END
        }
        val col: Int
        try {
            col = input.toInt()
        } catch (e: Exception) {
            println("Incorrect column number")
            return OUTPUT.INCORRECT
        }
        when (val row = upperRow(col - 1)) {
            -2 -> println("The column number is out of range (1 - $columns)")
            -1 -> println("Column $col is full")
            else ->  {
                makeTurn(row, col - 1)
                return OUTPUT.CORRECT
            }
        }
        return OUTPUT.INCORRECT
    }

    private fun makeTurn(row: Int, col: Int) {
        if (state == TURN.FIRST) {
            list[row][col] = "o"
            state = TURN.SECOND
        } else {
            list[row][col] = "*"
            state = TURN.FIRST
        }
    }

    private fun upperRow(column: Int): Int {
        if (column !in 0 until columns) return -2
        for (i in list.indices)
            if (list[i][column] != " ")
                return i - 1
        return rows - 1
    }

    private fun boxDrawing() {
        for (i in 1..columns) {
            print(" $i")
        }
        println()
        for (line in list) {
            for (ch in line) {
                print("║$ch")
            }
            println("║")
        }
        println("╚" + "═╩".repeat(columns - 1) + "═╝")
    }
}
fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstName = readLine()!!
    println("Second player's name:")
    val secondName = readLine()!!
    val (rows, columns) = dimensions()

    val game = ConnectFour(firstName, secondName, rows, columns)
    game.run()
}

fun dimensions(): Pair<Int, Int> {
    while(true) {
        println("Set the board dimensions (Rows x Columns)\n" +
                "Press Enter for default (6 x 7)")
        var input = readLine()!!
        if (input == "") return Pair(6, 7)
        input = input.filter { !it.isWhitespace() }
        if(!input.matches(Regex("\\d+[X|x]\\d+")))
            println("Invalid input")
        else {
            val (rows, columns) = input.split('X', 'x').map { it.toInt() }
            if(rows !in 5..9) println("Board rows should be from 5 to 9")
            else if(columns !in 5..9) println("Board columns should be from 5 to 9")
            else return Pair(rows, columns)
        }
    }
}