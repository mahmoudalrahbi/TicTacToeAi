package com.example.tictactoeai

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    var board = arrayOf(
        charArrayOf('_', '_', '_'),
        charArrayOf('_', '_', '_'),
        charArrayOf('_', '_', '_')
    )


    var xChar: Char = 'x'
    var oChar: Char = 'o'
    var human: Char = xChar
    var computer: Char = oChar

    var ai: Ai = Ai(computer, human)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//calling the function to load our tic tac toe board
        loadBoard()
    }


    /*
   * This function is generating the tic tac toe board
   * */
    private fun loadBoard() {

        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )

                //attached a click listener to the board
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))


                layout_board.addView(boardCells[i][j])

            }
        }
    }


    inner class CellClickListener(
        val i: Int,
        val j: Int
    ) : View.OnClickListener {

        override fun onClick(p0: View?) {
            //here we will code the move

            if (!ai.isGameOver(board) && ai.isCillEmpty(board, i, j)) {
                board[i][j] = human
                mapBoardToUi()

                if (!ai.isGameOver(board)) {

                    var pesetMove: Ai.Move = ai.findBestMove(board)

                    board[pesetMove.row][pesetMove.col] = computer
                    mapBoardToUi()


                    if (ai.isGameOver(board)) {
                        gameFinshed()
                    }

                } else {
                    gameFinshed()
                }
            }

        }

    }


    /////////////////////////////////////////////////

    private fun mapBoardToUi() {
        for (i in board.indices) {
            for (j in board.indices) {
                when (board[i][j]) {
                    oChar -> {
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    xChar -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }


    private fun gameFinshed() {
        var msg: String = ""

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over")

        if (ai.hasComputerWon(board)) {
            msg= "هاردلك!!"
        }else if(ai.hasHumanWon(board)){
            msg="أنت وحش"
        }else{
            msg="تعادل"
        }

        builder.setMessage(msg)

        builder.setPositiveButton("New Game"){dialogInterface, i ->
            newGame()
        }

        builder.setNegativeButton("Exit"){dialogInterface, i ->
            finish()
        }


        builder.show()


    }

    private fun newGame() {

        for (i in board.indices) {
            for (j in board.indices) {
                board[i][j] = '_'
            }
        }

        mapBoardToUi()

    }
}