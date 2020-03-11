package ee.taltech.iti0213_2019s_hw01

import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.collections.ArrayList

class GameLogic ( turn : TextView, playerOneScore : TextView, playerTwoScore : TextView) {
    var textViewTurn : TextView = turn
    var textViewPlayerOneScore : TextView = playerOneScore  // black player
    var textViewPlayerTwoScore : TextView = playerTwoScore  // blue player

    val uiLogic = UILogic()

    var blackButtons = arrayListOf<Button>()
    var whiteButtons = arrayListOf<Button>()
    var unusedButtons = arrayListOf<Button>()

    var blackActiveButtons = arrayListOf<Button>()
    var whiteActiveButtons = arrayListOf<Button>()
    var unusedActiveButtons = arrayListOf<Button>()

    var currentGame = "NONE" // TWOPLAYERS, ONEPLAYERBLACK, ONEPLAYERWHITE, AIVSAI, NONE
    var currentPlayer = "NONE"  // BLACK, BLUE, NONE
    var gameboardPieceSelected :Button? = null

    /* START: UI CHANGES*/

    private fun colorRegularButtons() {
        blackButtons.forEach{uiLogic.colorButton(it, Color.BLACK, false)}
        whiteButtons.forEach{uiLogic.colorButton(it, Color.WHITE, false)}
        unusedButtons.forEach{uiLogic.colorButton(it, Color.UNUSED, false)}
    }

    private fun colorActiveButtons() {
        blackActiveButtons.forEach{uiLogic.colorButton(it, Color.BLACK, true)}
        whiteActiveButtons.forEach{uiLogic.colorButton(it, Color.WHITE, true)}
        unusedActiveButtons.forEach{uiLogic.colorButton(it, Color.UNUSED, true)}
    }

    private fun updateTextViewTurn() {
        uiLogic.changeTextViewText(textViewTurn, generateTextViewTurnText())
    }

    /* END: UI CHANGES*/


    /* START: INIT GAMES */
    // TWOPLAYERS, ONEPLAYERBLACK, ONEPLAYERWHITE, AIVSAI, NONE

    fun btnStartGame2Players(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "TWOPLAYERS"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()
    }

    fun btnStartGame1PlayerWhite(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERWHITE"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()

        // TODO if ai starts the game
        if (currentPlayer == "BLACK") {
            aiTurn(currentPlayer)
        }
    }

    fun btnStartGame1PlayerBlack(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERBLACK"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()

        // TODO if ai starts the game
        if (currentPlayer == "BLUE") {
            aiTurn(currentPlayer)
        }
    }

    fun btnStartGameAiVsAi(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "AIVSAI"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()

        // TODO startgame
    }

    private fun setStartingPlayer(toggleButtonIsChecked: Boolean) {
        // toggleButtonChoosePlayer.isChecked  = false = BLACK
        // toggleButtonChoosePlayer.isChecked  = true = BLUE
        if (toggleButtonIsChecked) {
            currentPlayer = "BLUE"
        } else {
            currentPlayer = "BLACK"
        }
    }

    // initial gameboard
    fun initButtonsInBeginning(black: ArrayList<Button>, white: ArrayList<Button>, unused: ArrayList<Button>) {
        blackButtons = black
        whiteButtons = white
        unusedButtons = unused
    }

    /* END: INIT GAMES */


    /* START: AI LOGIC */
    private fun aiTurn(player : String) {
        endGameIfNoMorePossibleMoves()

        if (currentGame != "NONE") {
            var buttons = listOf<Button>()

            if (player == "BLACK") {  // player == "BLACK"
                buttons = blackButtons.map { it }
            } else {  // player == "BLUE"
                buttons = whiteButtons.map { it }
            }

            var winCounts = hashMapOf<ArrayList<Button>, Int>()
            var initialMoves = ArrayList<ArrayList<Button>>()

            for (button in buttons) {
                var validMoves = findValidMovesForPiece(getButtonNameFromViewString(button.toString()))
                for (move in validMoves) {
                    var _move = arrayListOf<Button>(button, move)
                    initialMoves.add(_move)
                    winCounts[_move] = 0
                }
            }

            for (move in initialMoves) {
                var i = 0
                while (i < 100) {
                    val res = simulate()  // todo
                    if (res == "WIN") {
                        val winCounter = winCounts[move]
                        if (winCounter != null) {
                            winCounts[move] = winCounter + 2
                        }
                    } else if (res == "DRAW") {
                        val winCounter = winCounts[move]
                        if (winCounter != null) {
                            winCounts[move] = winCounter + 1
                        }
                    }
                    i++
                }
            }

            // find the move that won the most of played games
            var maxKey = initialMoves[0]
            var maxValue = 0
            for ((key, value) in winCounts) {
                if (value > maxValue) {
                    maxKey = key
                    maxValue = value
                }
            }

            // make move on actual gameboard
            // maxKey = [buttonToMove, buttonWhereToMove]
            makeAiMove(maxKey)
        }

    }

    // make ai move on the gameboard
    // move = [buttonToMove, buttonWhereToMove]
    private fun makeAiMove(move : ArrayList<Button>) {
        // make ai move and update the view
        if (currentPlayer == "BLACK") {
            blackButtons.remove(move[0])
            blackButtons.add(move[1])
            unusedButtons.remove(move[1])
            unusedButtons.add(move[0])
            currentPlayer = "BLUE"
        }
        else {
            whiteButtons.remove(move[0])
            whiteButtons.add(move[1])
            unusedButtons.remove(move[1])
            unusedButtons.add(move[0])
            currentPlayer = "BLACK"
        }

        emptyActiveButtonsLists()
        colorRegularButtons()
        colorActiveButtons()
        updateTextViewTurn()

    }

    private fun simulate() : String {
        // returns "LOST", "DRAW", "WIN"
        // todo
        return ""
    }

    /* END: AI LOGIC */


    fun restoreGameState(
        currentPlayer: String,
        currentGame: String,
        blackButtons: ArrayList<Button>,
        whiteButtons: ArrayList<Button>,
        unusedButtons: ArrayList<Button>,
        blackActiveButtons: ArrayList<Button>,
        whiteActiveButtons: ArrayList<Button>,
        unusedActiveButtons: ArrayList<Button>,
        gameboardPieceSelectedString: String
    ) {
        this.currentGame = currentGame
        this.currentPlayer = currentPlayer

        this.blackButtons = blackButtons
        this.whiteButtons = whiteButtons
        this.unusedButtons = unusedButtons
        this.blackActiveButtons = blackActiveButtons
        this.whiteActiveButtons = whiteActiveButtons
        this.unusedActiveButtons = unusedActiveButtons


        if (gameboardPieceSelectedString != "") {
            gameboardPieceSelected = findGameboardButtonByName(getButtonNameFromViewString(gameboardPieceSelectedString))
        }

        updateTextViewTurn()
        colorRegularButtons()
        colorActiveButtons()

        // TODO continue with gamelogic with 1 player
        // TODO continue with gamelogic with AI vs AI
    }


    // handles gameboard button clicks when it's necessary
    fun gameBoardButtonClicked(view : View) {
        //val viewString = view.toString()
        //val clickedButtonString = viewString.substring(viewString.length - 9, viewString.length - 1)

        val clickedButton = findGameboardButtonById(view.id)

        if (currentGame != "NONE") {

            // if 2 players play
            if (currentGame == "TWOPLAYERS") {
                if (clickedButton != null) { playerTurn(clickedButton, view) }

            } else if (currentGame == "ONEPLAYERBLACK") {
                if (clickedButton != null && currentPlayer == "BLACK") {
                    // todo
                    playerTurn(clickedButton, view)
                }

            } else if (currentGame == "ONEPLAYERWHITE") {
                if (clickedButton != null && currentPlayer == "BLUE") {
                    // todo
                    playerTurn(clickedButton, view)
                }

            }

        }

    }

    private fun playerTurn(clickedButton : Button, view : View) {
        if (gameboardPieceSelected != null) {  // if player has chosen his piece and clicks on another button on the gameboard
            val localSelectedPiece = gameboardPieceSelected

            if (unusedActiveButtons.contains(clickedButton)) {
                unusedButtons.remove(clickedButton)

                if (currentPlayer == "BLACK") {
                    blackButtons.remove(localSelectedPiece)
                    if (localSelectedPiece != null) { unusedButtons.add(localSelectedPiece) }
                    blackButtons.add(clickedButton)
                    if (isWinner("BLACK")) { endGame() }
                    currentPlayer = "BLUE"

                    if (currentGame == "ONEPLAYERBLACK") {
                        aiTurn(currentPlayer)
                    }
                } else {
                    whiteButtons.remove(localSelectedPiece)
                    if (localSelectedPiece != null) { unusedButtons.add(localSelectedPiece) }
                    whiteButtons.add(clickedButton)
                    if (isWinner("BLUE")) { endGame() }
                    currentPlayer = "BLACK"

                    if (currentGame == "ONEPLAYERWHITE") {
                        aiTurn(currentPlayer)
                    }
                }

            }
            if (currentGame != "NONE") { updateTextViewTurn() }
            emptyActiveButtonsLists()
            colorRegularButtons()
            colorActiveButtons()
            gameboardPieceSelected = null

        } else {  // no gameboard button has been chosen

            if (currentPlayer == "BLACK") {  // currentPlayer == "BLACK"
                if (blackButtons.contains(clickedButton)) {
                    blackActiveButtons.add(clickedButton)

                    val validMoves = findValidMovesForPiece(getButtonNameFromViewString(view.toString()))
                    unusedActiveButtons.addAll(validMoves)
                    colorActiveButtons()
                    gameboardPieceSelected = clickedButton
                }

            } else {  // currentPlayer == "BLUE"
                if (whiteButtons.contains(clickedButton)) {
                    whiteActiveButtons.add(clickedButton)

                    val validMoves = findValidMovesForPiece(getButtonNameFromViewString(view.toString()))
                    unusedActiveButtons.addAll(validMoves)
                    colorActiveButtons()
                    gameboardPieceSelected = clickedButton
                }
            }

        }

        // if game is not over yet then check for the next move that are there any possible moves
        // if there aren't then end the game
        endGameIfNoMorePossibleMoves()
    }

    // updates whose turn it is in text view
    private fun generateTextViewTurnText(): String {
        if (!currentPlayer.equals("NONE")) {
            return currentPlayer + " player's turn"
        }
        return "Start new game!"
    }

    // checks if there are any more possible moves for current player, if not then end the game
    private fun endGameIfNoMorePossibleMoves() {
        if (currentGame != "NONE") {
            if (!isThereAnyValidMoves(currentPlayer)) {
                if (currentPlayer == "BLACK") {
                    currentPlayer = "BLUE"
                } else {
                    currentPlayer = "BLACK"
                }
                endGame()
            }
        }
    }

    // gameover procedure
    private fun endGame() {
        uiLogic.changeTextViewText(textViewTurn,currentPlayer + " won! Start a new game!")
        // todo SCORE LOGIC
        currentGame = "NONE"
        currentPlayer = "NONE"
        emptyActiveButtonsLists()
        colorRegularButtons()
    }

    // empties active buttons lists
    private fun emptyActiveButtonsLists() {
        blackActiveButtons = arrayListOf<Button>()
        whiteActiveButtons = arrayListOf<Button>()
        unusedActiveButtons = arrayListOf<Button>()
    }

    // checks if there are any valid moves for given player
    private fun isThereAnyValidMoves(player: String) : Boolean {
        if (player == "BLACK") {
            for (button in blackButtons) {
                if (findValidMovesForPiece(getButtonNameFromViewString(button.toString())).size != 0) {
                    return true
                }
            }
        } else {
            for (button in whiteButtons) {
                if (findValidMovesForPiece(getButtonNameFromViewString(button.toString())).size != 0) {
                    return true
                }
            }
        }
        return false
    }

    // checks if given player has won the game
    private fun isWinner(player : String) : Boolean {
        if (player == "BLACK") {
            val endForBlack = arrayListOf("button41", "button45", "button51", "button52",
                "button53", "button54", "button55")
            for (button in blackButtons) {
                if (!endForBlack.contains(getButtonNameFromViewString(button.toString()))) {
                    return false
                }
            }
        } else {
            val endForWhite = arrayListOf("button11", "button12", "button13", "button14",
                "button15", "button21", "button25")
            for (button in whiteButtons) {
                if (!endForWhite.contains(getButtonNameFromViewString(button.toString()))) {
                    return false
                }
            }
        }
        return true
    }

    // find valid moves for given buttonNameString = button11
    private fun findValidMovesForPiece(buttonNameString : String) : ArrayList<Button> {
        var validButtons = arrayListOf<Button>()
        val endNumber = buttonNameString.substring(buttonNameString.length - 2).toInt()
        val buttonNames = arrayListOf("button" + (endNumber - 11).toString(),
            "button" + (endNumber - 9).toString(),
            "button" + (endNumber + 11).toString(),
            "button" + (endNumber + 9).toString())
        for (buttonName in buttonNames) {
            val button = findGameboardButtonByName(buttonName)
            if (button != null) {
                if (unusedButtons.contains(button)) {
                    validButtons.add(button)
                }
            }
        }
        return validButtons
    }

    // takes in view as a string and returns button's name, for example: button11
    fun getButtonNameFromViewString(viewString : String) : String {
        return viewString.substring(viewString.length - 9, viewString.length - 1)
    }

    // find button by its name,  for example: name = button11
    private fun findGameboardButtonByName(name: String) : Button? {
        for (button in blackButtons) {
            if (getButtonNameFromViewString(button.toString()) == name) {
                return button
            }
        }
        for (button in whiteButtons) {
            if (getButtonNameFromViewString(button.toString()) == name) {
                return button
            }
        }
        for (button in unusedButtons) {
            if (getButtonNameFromViewString(button.toString()) == name) {
                return button
            }
        }
        return null
    }

    // takes in id and finds the button with corresponding id
    private fun findGameboardButtonById(viewId: Int) : Button? {
        for (button in blackButtons) {
            if (button.id == viewId) {
                return button
            }
        }
        for (button in whiteButtons) {
            if (button.id == viewId) {
                return button
            }
        }
        for (button in unusedButtons) {
            if (button.id == viewId) {
                return button
            }
        }
        return null
    }

}