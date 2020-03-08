package ee.taltech.iti0213_2019s_hw01

import android.view.View
import android.widget.Button
import android.widget.TextView

class GameLogic ( turn : TextView, playerOneScore : TextView, playerTwoScore : TextView) {
    var textViewTurn : TextView = turn
    var textViewPlayerOneScore : TextView = playerOneScore  // black player
    var textViewPlayerTwoScore : TextView = playerTwoScore

    val uiLogic = UILogic()

    var blackButtons = arrayListOf<Button>()
    var whiteButtons = arrayListOf<Button>()
    var unusedButtons = arrayListOf<Button>()

    var blackActiveButtons = arrayListOf<Button>()
    var whiteActiveButtons = arrayListOf<Button>()
    var unusedActiveButtons = arrayListOf<Button>()

    var currentGame = "NONE" // TWOPLAYERS, ONEPLAYERBLACK, ONEPLAYERWHITE, AIVSAI, NONE
    var currentPlayer = "NONE"  // BLACK, BLUE, NONE


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

    /* UI CHANGES END */


    /* INIT GAMES */
    // TWOPLAYERS, ONEPLAYERBLACK, ONEPLAYERWHITE, AIVSAI, NONE
    fun btnStartGame2Players(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "TWOPLAYERS"
        setStartingPlayer(toggleButtonIsChecked)
        uiLogic.changeTextViewText(textViewTurn, generateTextViewTurnText())

        // TODO startgame
    }

    fun btnStartGame1PlayerWhite(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERWHITE"
        setStartingPlayer(toggleButtonIsChecked)
        uiLogic.changeTextViewText(textViewTurn, generateTextViewTurnText())

        // TODO startgame
    }

    fun btnStartGame1PlayerBlack(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERBLACK"
        setStartingPlayer(toggleButtonIsChecked)
        uiLogic.changeTextViewText(textViewTurn, generateTextViewTurnText())

        // TODO startgame
    }

    fun btnStartGameAiVsAi(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "AIVSAI"
        setStartingPlayer(toggleButtonIsChecked)
        uiLogic.changeTextViewText(textViewTurn, generateTextViewTurnText())

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

    private fun generateTextViewTurnText(): String {
        if (!currentPlayer.equals("NONE")) {
            return currentPlayer + " player's turn"
        }
        return "Start new game!"
    }

    fun restoreGameState(currentPlayer : String, currentGame : String, blackButtons : ArrayList<Button>,
                         whiteButtons : ArrayList<Button>, unusedButtons : ArrayList<Button>,
                         blackActiveButtons : ArrayList<Button>, whiteActiveButtons : ArrayList<Button>,
                         unusedActiveButtons : ArrayList<Button>) {
        this.currentGame = currentGame
        this.currentPlayer = currentPlayer

        this.blackButtons = blackButtons
        this.whiteButtons = whiteButtons
        this.unusedButtons = unusedButtons
        this.blackActiveButtons = blackActiveButtons
        this.whiteActiveButtons = whiteActiveButtons
        this.unusedActiveButtons = unusedActiveButtons

        uiLogic.changeTextViewText(textViewTurn, generateTextViewTurnText())
        colorRegularButtons()
        colorActiveButtons()

        // TODO continue with gamelogic
    }

    fun gameBoardButtonClicked(view : View) {
        val viewString = view.toString()
        val clickedButtonString = viewString.substring(viewString.length - 9, viewString.length - 1)

        // TODO
        uiLogic.changeTextViewText(textViewTurn, clickedButtonString)
        var clickedButton = findGameboardButtonById(view.id)
        if (blackButtons.contains(clickedButton)) {
            if (clickedButton != null) {
                blackActiveButtons.add(clickedButton)
            }
        }
        colorActiveButtons()
    }

    // takes in id and finds the button with corresponding id
    fun findGameboardButtonById(viewId: Int) : Button? {
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