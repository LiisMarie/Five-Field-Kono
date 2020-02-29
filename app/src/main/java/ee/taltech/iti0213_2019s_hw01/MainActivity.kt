package ee.taltech.iti0213_2019s_hw01

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.game_board.*


class MainActivity : AppCompatActivity() {

    var blackButtons = listOf<Button>()
    var whiteButtons = listOf<Button>()
    var unusedButtons = listOf<Button>()

    companion object {
        private val TAG = this::class.java.declaringClass!!.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        blackButtons = listOf<Button>(button11, button12, button13, button14, button15, button21, button25)
        whiteButtons = listOf<Button>(button41, button51, button52, button53, button54, button55, button45)
        unusedButtons = listOf<Button>(button22, button23, button24, button31, button32, button33, button34,
        button35, button42, button43, button44)
    }

    fun gameboardButtonOnClick(view: View) {
        Log.d(TAG, "Gameboard button clicked  " + view)
        makeActive(view)
    }

    fun makeActive(button: View) {
        if (button in blackButtons) {
            button.setBackgroundResource(R.drawable.button_black_active)
        } else if (button in whiteButtons) {
            button.setBackgroundResource(R.drawable.button_white_active)
        } else if (button in unusedButtons) {
            button.setBackgroundResource(R.drawable.button_unused_active)
        }
    }

}
