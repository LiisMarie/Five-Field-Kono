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

    var playerOneScore : Int = 0  // black player
    var playerTwoScore : Int = 0  // blue player


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

    private fun updatePlayersScore() {
        uiLogic.changeTextViewText(textViewPlayerOneScore, playerOneScore.toString())
        uiLogic.changeTextViewText(textViewPlayerTwoScore, playerTwoScore.toString())
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

        if (currentPlayer == "BLACK") { aiTurn(currentPlayer) }
    }

    fun btnStartGame1PlayerBlack(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "ONEPLAYERBLACK"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()

        if (currentPlayer == "BLUE") { aiTurn(currentPlayer) }
    }

    fun btnStartGameAiVsAi(toggleButtonIsChecked : Boolean) {
        emptyActiveButtonsLists()
        colorRegularButtons()
        currentGame = "AIVSAI"
        setStartingPlayer(toggleButtonIsChecked)
        updateTextViewTurn()
        //aiVsAi()
    }

    // sets starting player in the beginning of the game
    private fun setStartingPlayer(toggleButtonIsChecked: Boolean) {
        if (toggleButtonIsChecked) { currentPlayer = "BLUE" } // toggleButtonChoosePlayer.isChecked  = true = BLUE
        else { currentPlayer = "BLACK" } // toggleButtonChoosePlayer.isChecked  = false = BLACK
    }

    // initial gameboard
    fun initButtonsInBeginning(black: ArrayList<Button>, white: ArrayList<Button>, unused: ArrayList<Button>) {
        blackButtons = black
        whiteButtons = white
        unusedButtons = unused
    }

    /* END: INIT GAMES */


    /* START: AI LOGIC */
    fun aiVsAi() {
        // todo
        aiTurn(currentPlayer)
        /*
        while (i < 5 && currentGame != "NONE") {
            aiTurn(currentPlayer)
            Thread.sleep(3_000)
            i++
        }*/


    }

    private fun aiTurn(player : String) {
        endGameIfNoMorePossibleMoves()

        if (currentGame != "NONE") {
            var currentPlayersButtons: List<Button>

            if (player == "BLACK") {  // player == "BLACK"
                currentPlayersButtons = blackButtons.map { it }
            } else {  // player == "BLUE"
                currentPlayersButtons = whiteButtons.map { it }
            }

            var bestValue: Int = -10
            var bestMove: ArrayList<Button>? = null
            for (button in currentPlayersButtons) {
                var validMoves = findValidMovesForPiece(getButtonNameFromViewString(button.toString()))
                for (move in validMoves) {
                    val possibleMove = arrayListOf<Button>(button, move)
                    val moveValue = calcAiMoveValue(player, possibleMove)
                    if (bestMove == null) {
                        bestMove = possibleMove
                        bestValue = moveValue
                    }
                    if (moveValue > bestValue) {
                        bestValue = moveValue
                        bestMove = possibleMove
                    }
                }
            }
            // make move on actual gameboard
            if (bestMove != null) { makeAiMove(bestMove) } // maxKey = [buttonToMove, buttonWhereToMove]
        }

    }

    // calculate value for given ai player's move
    private fun calcAiMoveValue(player: String, move: ArrayList<Button>) : Int {
        val currentPos = getButtonNameFromViewString(move[0].toString()).substring(6).toInt()
        val newPos = getButtonNameFromViewString(move[1].toString()).substring(6).toInt()
        if (player == "BLACK") {
            val desiredPos = arrayListOf(41, 45, 51, 52, 53, 54, 55)
            if (desiredPos.contains(currentPos)) { return -10 }
            if (desiredPos.contains(newPos)) { return 20 }

            val zoneTwo = arrayListOf(42, 43, 44)
            if (zoneTwo.contains(currentPos)) { return -8 }
            if (zoneTwo.contains(newPos)) { return 18 }

            val zoneThree = arrayListOf(31, 32, 33, 34, 35)
            if (zoneThree.contains(currentPos)) { return -5 }
            if (zoneThree.contains(newPos)) { return 15 }

            val zoneFour = arrayListOf(21, 22, 23, 24, 25)
            if (zoneFour.contains(currentPos)) { return -3 }
            if (zoneFour.contains(newPos)) { return 10 }

            val zoneFive = arrayListOf(11, 12, 13, 14, 15)
            if (zoneFive.contains(currentPos)) { return -3 }
            if (zoneFive.contains(newPos)) { return -15 }

            return 0

        } else if (player == "BLUE" ) {
            val desiredPos = arrayListOf(11, 12, 13, 14, 15, 21, 25)
            if (desiredPos.contains(currentPos)) { return -10 }
            if (desiredPos.contains(newPos)) { return 20 }

            val zoneTwo = arrayListOf(22, 23, 24)
            if (zoneTwo.contains(currentPos)) { return -8 }
            if (zoneTwo.contains(newPos)) { return 18 }

            val zoneThree = arrayListOf(31, 32, 33, 34, 35)
            if (zoneThree.contains(currentPos)) { return -5 }
            if (zoneThree.contains(newPos)) { return 15 }

            val zoneFour = arrayListOf(41, 42, 43, 44, 45)
            if (zoneFour.contains(currentPos)) { return -3 }
            if (zoneFour.contains(newPos)) { return 10 }

            val zoneFive = arrayListOf(51, 52, 53, 54, 55)
            if (zoneFive.contains(currentPos)) { return -3 }
            if (zoneFive.contains(newPos)) { return -15 }
        }
        return 0
    }

    // make ai move on the gameboard
    private fun makeAiMove(move : ArrayList<Button>) {   // move = [buttonToMove, buttonWhereToMove]
        // make ai move and update the view
        if (currentPlayer == "BLACK") {
            blackButtons.remove(move[0])
            blackButtons.add(move[1])
            unusedButtons.remove(move[1])
            unusedButtons.add(move[0])
            if (isWinner(currentPlayer) ) { endGame() }
            currentPlayer = "BLUE"
        }
        else {
            whiteButtons.remove(move[0])
            whiteButtons.add(move[1])
            unusedButtons.remove(move[1])
            unusedButtons.add(move[0])
            if (isWinner(currentPlayer) ) { endGame() }
            currentPlayer = "BLACK"
        }
        emptyActiveColorAll()
        if (currentGame != "NONE") { updateTextViewTurn() }

    }

    /* END: AI LOGIC */

    // restores the state of the game
    fun restoreGameState(currentPlayer: String, currentGame: String,
        blackButtons: ArrayList<Button>, whiteButtons: ArrayList<Button>,
        unusedButtons: ArrayList<Button>, blackActiveButtons: ArrayList<Button>,
        whiteActiveButtons: ArrayList<Button>, unusedActiveButtons: ArrayList<Button>,
        gameboardPieceSelectedString: String, oneScore: Int, twoScore: Int
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

        // continue with gamelogic - 1 player
        if (currentGame == "ONEPLAYERWHITE" && currentPlayer == "BLACK") { aiTurn(currentPlayer) }
        else if (currentGame == "ONEPLAYERBLACK" && currentPlayer == "BLUE") { aiTurn(currentPlayer) }

        playerOneScore = oneScore
        playerTwoScore = twoScore
        updatePlayersScore()
        // TODO continue with gamelogic with AI vs AI
        // continue with gamelogic - AI vs AI
    }


    // handles gameboard button clicks when it's necessary
    fun gameBoardButtonClicked(view : View) {
        val clickedButton = findGameboardButtonById(view.id)
        if (currentGame != "NONE") {

            if (currentGame == "TWOPLAYERS") { // if 2 players play
                if (clickedButton != null) { playerTurn(clickedButton, view) }

            } else if (currentGame == "ONEPLAYERBLACK") {
                if (clickedButton != null && currentPlayer == "BLACK") { playerTurn(clickedButton, view) }

            } else if (currentGame == "ONEPLAYERWHITE") {
                if (clickedButton != null && currentPlayer == "BLUE") { playerTurn(clickedButton, view) }

            }
        }
    }

    // regulates player turn
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
                        emptyActiveColorAll()
                        gameboardPieceSelected = null
                        aiTurn(currentPlayer)
                    }
                } else {
                    whiteButtons.remove(localSelectedPiece)
                    if (localSelectedPiece != null) { unusedButtons.add(localSelectedPiece) }
                    whiteButtons.add(clickedButton)
                    if (isWinner("BLUE")) { endGame() }
                    currentPlayer = "BLACK"

                    if (currentGame == "ONEPLAYERWHITE") {
                        emptyActiveColorAll()
                        gameboardPieceSelected = null
                        aiTurn(currentPlayer)
                    }
                }

            }
            if (currentGame != "NONE") { updateTextViewTurn() }
            emptyActiveColorAll()
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
        endGameIfNoMorePossibleMoves()  // if there aren't then end the game
    }

    // updates whose turn it is in text view
    private fun generateTextViewTurnText(): String {
        if (currentPlayer != "NONE") { return currentPlayer + " player's turn" }
        return "Start new game!"
    }

    // checks if there are any more possible moves for current player, if not then end the game
    private fun endGameIfNoMorePossibleMoves() {
        if (currentGame != "NONE") {
            if (!isThereAnyValidMoves(currentPlayer)) {
                if (currentPlayer == "BLACK") { currentPlayer = "BLUE" }
                else { currentPlayer = "BLACK" }
                endGame()
            }
        }
    }

    // gameover procedure
    private fun endGame() {
        uiLogic.changeTextViewText(textViewTurn,currentPlayer + " won! Start a new game!")
        // todo SCORE LOGIC
        if (currentPlayer == "BLACK") { playerOneScore ++ }
        else if (currentPlayer == "BLUE") { playerTwoScore ++ }

        currentGame = "NONE"
        currentPlayer = "NONE"
        emptyActiveButtonsLists()
        colorRegularButtons()
        updatePlayersScore()
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
                if (findValidMovesForPiece(getButtonNameFromViewString(button.toString())).size != 0) { return true }
            }
        } else {
            for (button in whiteButtons) {
                if (findValidMovesForPiece(getButtonNameFromViewString(button.toString())).size != 0) { return true }
            }
        }
        return false
    }

    // checks if given player has won the game
    private fun isWinner(player : String) : Boolean {
        if (player == "BLACK") {
            val endForBlack = arrayListOf("button41", "button45", "button51", "button52", "button53", "button54", "button55")
            for (button in blackButtons) {
                if (!endForBlack.contains(getButtonNameFromViewString(button.toString()))) { return false }
            }
        } else {
            val endForWhite = arrayListOf("button11", "button12", "button13", "button14", "button15", "button21", "button25")
            for (button in whiteButtons) {
                if (!endForWhite.contains(getButtonNameFromViewString(button.toString()))) { return false }
            }
        }
        return true
    }

    // find valid moves for given buttonNameString = button11
    private fun findValidMovesForPiece(buttonNameString : String) : ArrayList<Button> {
        var validButtons = arrayListOf<Button>()
        val endNumber = buttonNameString.substring(buttonNameString.length - 2).toInt()
        val buttonNames = arrayListOf("button" + (endNumber - 11).toString(), "button" + (endNumber - 9).toString(),
            "button" + (endNumber + 11).toString(), "button" + (endNumber + 9).toString())
        for (buttonName in buttonNames) {
            val button = findGameboardButtonByName(buttonName)
            if (button != null) { if (unusedButtons.contains(button)) { validButtons.add(button) } }
        }
        return validButtons
    }

    // takes in view as a string and returns button's name, for example: button11
    fun getButtonNameFromViewString(viewString : String) : String {
        return viewString.substring(viewString.length - 9, viewString.length - 1)
    }

    // find button by its name,  for example: name = button11
    private fun findGameboardButtonByName(name: String) : Button? {
        for (button in blackButtons) { if (getButtonNameFromViewString(button.toString()) == name) { return button } }
        for (button in whiteButtons) { if (getButtonNameFromViewString(button.toString()) == name) { return button } }
        for (button in unusedButtons) { if (getButtonNameFromViewString(button.toString()) == name) { return button } }
        return null
    }

    // takes in id and finds the button with corresponding id
    private fun findGameboardButtonById(viewId: Int) : Button? {
        for (button in blackButtons) { if (button.id == viewId) { return button } }
        for (button in whiteButtons) { if (button.id == viewId) { return button } }
        for (button in unusedButtons) { if (button.id == viewId) { return button } }
        return null
    }

    // empties active buttons list, colors regular and active buttons
    private fun emptyActiveColorAll() {
        emptyActiveButtonsLists()
        colorRegularButtons()
        colorActiveButtons()
    }

}