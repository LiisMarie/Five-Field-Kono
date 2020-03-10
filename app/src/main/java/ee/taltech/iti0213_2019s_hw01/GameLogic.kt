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

    /* UI CHANGES*/

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

    /* UI CHANGES END */


    /* INIT GAMES */
    // TWOPLAYERS, ONEPLAYERBLACK, ONEPLAYERWHITE, AIVSAI, NONE
    fun btnStartGame2Players(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "TWOPLAYERS"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()
        // TODO startgame
    }

    fun btnStartGame1PlayerWhite(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERWHITE"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()

        // TODO startgame
    }

    fun btnStartGame1PlayerBlack(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERBLACK"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()

        // TODO startgame
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

    fun emptyActiveButtonsLists() {
        blackActiveButtons = arrayListOf<Button>()
        whiteActiveButtons = arrayListOf<Button>()
        unusedActiveButtons = arrayListOf<Button>()
    }
    /* INIT GAMES END*/

    // updates whose turn it is in text view
    private fun generateTextViewTurnText(): String {
        if (!currentPlayer.equals("NONE")) {
            return currentPlayer + " player's turn"
        }
        return "Start new game!"
    }

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
            // todo
        }

        updateTextViewTurn()
        colorRegularButtons()
        colorActiveButtons()

        // TODO continue with gamelogic
    }

    // takes in view as a string and returns button's name, for example: button11
    fun getButtonNameFromViewString(viewString : String) : String {
        return viewString.substring(viewString.length - 9, viewString.length - 1)
    }

    fun gameBoardButtonClicked(view : View) {
        val viewString = view.toString()
        //val clickedButtonString = viewString.substring(viewString.length - 9, viewString.length - 1)

        val clickedButton = findGameboardButtonById(view.id)

        if (currentGame != "NONE") {

            // todo if 2 players
            if (currentGame == "TWOPLAYERS") {
                if (clickedButton != null) { playerTurn(clickedButton, view) }
            }

            // todo if 1 player

        }

    }

    fun playerTurn(clickedButton : Button, view : View) {
        if (gameboardPieceSelected != null) {  // if player has chosen his piece and clicks on another button on the gameboard
            val localSelectedPiece = gameboardPieceSelected

            if (unusedActiveButtons.contains(clickedButton)) {
                unusedButtons.remove(clickedButton)

                if (currentPlayer == "BLACK") {
                    blackButtons.remove(localSelectedPiece)
                    if (localSelectedPiece != null) { unusedButtons.add(localSelectedPiece) }
                    if (clickedButton != null) { blackButtons.add(clickedButton) }
                    currentPlayer = "BLUE"

                    if (isWinner("BLACK")) {
                        uiLogic.changeTextViewText(textViewPlayerOneScore,"BLACKK WON")
                        currentGame = "NONE"
                        currentPlayer = "NONE"
                    }
                } else {
                    whiteButtons.remove(localSelectedPiece)
                    if (localSelectedPiece != null) { unusedButtons.add(localSelectedPiece) }
                    if (clickedButton != null) { whiteButtons.add(clickedButton) }
                    currentPlayer = "BLACK"

                    if (isWinner("BLUE")) {
                        uiLogic.changeTextViewText(textViewPlayerOneScore,"BLUEE WON")
                        currentGame = "NONE"
                        currentPlayer = "NONE"
                    }
                }

            }
            updateTextViewTurn()
            emptyActiveButtonsLists()
            colorRegularButtons()
            colorActiveButtons()
            gameboardPieceSelected = null

        } else {  // no gameboard button has been chosen

            if (currentPlayer == "BLACK") {  // currentPlayer == "BLACK"
                if (blackButtons.contains(clickedButton)) {
                    if (clickedButton != null) {
                        blackActiveButtons.add(clickedButton)
                        unusedActiveButtons.addAll(findValidMovesForPiece(getButtonNameFromViewString(view.toString())))
                        colorActiveButtons()
                        gameboardPieceSelected = clickedButton
                    }
                }

            } else {  // currentPlayer == "BLUE"
                if (whiteButtons.contains(clickedButton)) {
                    if (clickedButton != null) {
                        whiteActiveButtons.add(clickedButton)
                        unusedActiveButtons.addAll(findValidMovesForPiece(getButtonNameFromViewString(view.toString())))
                        colorActiveButtons()
                        gameboardPieceSelected = clickedButton
                    }
                }
            }

        }
    }


    // gameover procedure

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

    // find valid moves for given buttonNameString
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