package ee.taltech.iti0213_2019s_hw01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.game_board.*
import kotlinx.android.synthetic.main.game_statistics.*


class MainActivity : AppCompatActivity() {
    val uiLogic = UILogic()
    private lateinit var gameLogic : GameLogic

    private val intentFilter = IntentFilter()
    private val localBroadcastReceiver = BroadCastReceiverInMainActivity()

    private inner class BroadCastReceiverInMainActivity: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: " + (intent?.action ?: "Null intent"))
        }
    }

    companion object {
        private val TAG = this::class.java.declaringClass!!.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "lifecycle onCreate")
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            //savedInstanceState.getString("currentPlayer", "NONE")
            Log.d(TAG, "STARTINNGG  GUOPPPPP")
        }

        gameLogic = GameLogic(textViewTurn, textViewPlayerOneScore, textViewPlayerTwoScore)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "lifecycle onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "lifecycle onResume")

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(localBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "lifecycle onPause")

        LocalBroadcastManager
            .getInstance(this)
            .unregisterReceiver(localBroadcastReceiver)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "lifecycle onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "lifecycle onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "lifecycle onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "lifecycle onSaveInstanceState")

        outState.putString("currentGame", gameLogic.currentGame)
        outState.putString("currentPlayer", gameLogic.currentPlayer)
        outState.putIntegerArrayList("blackButtons", generateIntegerArrayListOfButtons(gameLogic.blackButtons))
        outState.putIntegerArrayList("whiteButtons", generateIntegerArrayListOfButtons(gameLogic.whiteButtons))
        outState.putIntegerArrayList("unusedButtons", generateIntegerArrayListOfButtons(gameLogic.unusedButtons))
        outState.putIntegerArrayList("blackActiveButtons", generateIntegerArrayListOfButtons(gameLogic.blackActiveButtons))
        outState.putIntegerArrayList("whiteActiveButtons", generateIntegerArrayListOfButtons(gameLogic.whiteActiveButtons))
        outState.putIntegerArrayList("unusedActiveButtons", generateIntegerArrayListOfButtons(gameLogic.unusedActiveButtons))
        outState.putInt("playerOneScore", gameLogic.playerOneScore)
        outState.putInt("playerTwoScore", gameLogic.playerTwoScore)

        if (gameLogic.gameboardPieceSelected != null) {
            outState.putString("gameboardPieceSelected", gameLogic.gameboardPieceSelected.toString())
        }

    }

    // generates list of buttons ids from buttons list
    fun generateIntegerArrayListOfButtons(buttons : List<Button>) : ArrayList<Int> {
        var buttonsAsStrings = arrayListOf<Int>()
        for (button in buttons) {
            buttonsAsStrings.add(button.id)
        }
        return buttonsAsStrings
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "lifecycle onRestoreInstanceState")

        val currentGame = savedInstanceState.getString("currentGame", "NONE")

        val currentPlayer = savedInstanceState.getString("currentPlayer", "NONE")

        gameLogic.restoreGameState(currentPlayer, currentGame,
            generateButtonsListOfIds(savedInstanceState.getIntegerArrayList("blackButtons")),
            generateButtonsListOfIds(savedInstanceState.getIntegerArrayList("whiteButtons")),
            generateButtonsListOfIds(savedInstanceState.getIntegerArrayList("unusedButtons")),
            generateButtonsListOfIds(savedInstanceState.getIntegerArrayList("blackActiveButtons")),
            generateButtonsListOfIds(savedInstanceState.getIntegerArrayList("whiteActiveButtons")),
            generateButtonsListOfIds(savedInstanceState.getIntegerArrayList("unusedActiveButtons")),
            savedInstanceState.getString("gameboardPieceSelected", ""),
            savedInstanceState.getInt("playerOneScore", 0),
            savedInstanceState.getInt("playerTwoScore", 0)
            )
    }

    // generates button list from buttons ids list
    fun generateButtonsListOfIds(ids: java.util.ArrayList<Int>?) : ArrayList<Button> {
        val buttons = arrayListOf<Button>()
        if (ids != null) {
            for (id in ids) {
                buttons.add(findViewById(id))
            }
        }
        return buttons
    }


    /* INIT GAMES */
    fun btnStartGame2Players(view: View) {
        initGameStart()
        gameLogic.btnStartGame2Players(toggleButtonChoosePlayer.isChecked)
    }

    fun btnStartGame1PlayerWhite(view: View) {
        initGameStart()
        gameLogic.btnStartGame1PlayerWhite(toggleButtonChoosePlayer.isChecked)
    }

    fun btnStartGame1PlayerBlack(view: View) {
        initGameStart()
        gameLogic.btnStartGame1PlayerBlack(toggleButtonChoosePlayer.isChecked)
    }

    fun btnStartGameAiVsAi(view: View) {
        initGameStart()
        gameLogic.btnStartGameAiVsAi(toggleButtonChoosePlayer.isChecked)

        uiLogic.changeTextViewText(textViewTurn, "AI vs AI game ongoing")

        var i = 0
        while (i < 500 && gameLogic.currentGame != "NONE") {
            gameLogic.aiVsAi()
            i++
        }
        if (gameLogic.currentGame != "NONE") {
            gameLogic.currentGame = "NONE"
            uiLogic.changeTextViewText(textViewTurn, "Couldn't finish within given time")
        }

    }

    fun initGameStart() {
        val blackButtons = arrayListOf<Button>(button11, button12, button13, button14, button15, button21, button25)
        val whiteButtons = arrayListOf<Button>(button41, button51, button52, button53, button54, button55, button45)
        val unusedButtons = arrayListOf<Button>(button22, button23, button24, button31, button32, button33, button34,
            button35, button42, button43, button44)
        gameLogic.initButtonsInBeginning(blackButtons, whiteButtons, unusedButtons)
    }
    /* INIT GAMES END */

    // when a gameboard button is clicked
    fun gameboardButtonOnClick(view: View) {
        Log.d(TAG, "Gameboard button clicked  " + view)

        gameLogic.gameBoardButtonClicked(view)
    }
}
