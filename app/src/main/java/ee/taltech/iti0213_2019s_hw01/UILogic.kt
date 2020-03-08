package ee.taltech.iti0213_2019s_hw01

import android.widget.Button
import android.widget.TextView

class UILogic {

    // changes given button color
    fun colorButton(button: Button, color: Color, makeActive: Boolean) {
        if (color == Color.BLACK) {
            if (makeActive) {
                button.setBackgroundResource(R.drawable.button_black_active)
            } else {
                button.setBackgroundResource(R.drawable.button_black)
            }
        } else if (color == Color.WHITE) {
            if (makeActive) {
                button.setBackgroundResource(R.drawable.button_white_active)
            } else {
                button.setBackgroundResource(R.drawable.button_white)
            }
        } else {
            if (makeActive) {
                button.setBackgroundResource(R.drawable.button_unused_active)
            } else {
                button.setBackgroundResource(R.drawable.button_unused)
            }
        }
    }

    // changes text in given text view
    fun changeTextViewText(textView: TextView, text: String) {
        textView.text = text
    }

}