package com.example.eclin.kotlin

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private var answer = ByteArray(4)
    private var answerText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val check = findViewById<Button>(R.id.check)
        val input = findViewById<EditText>(R.id.input)
        var inputAnswer: String
        var attemptTime = 0
        val display = findViewById<EditText>(R.id.display)
        val restart: Button = findViewById(R.id.restart)

        generateAnswer()

        display.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                display.scrollTo(0, display.bottom)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        display.setText(R.string.game_start)

        answerText = answer.joinToString(separator = "")
//        display.append("\r\n$answerText")


        restart.setOnClickListener {
            generateAnswer()
            display.setText("")
            display.setText(R.string.game_restart)
            answerText = answer.joinToString(separator = "")
//            display.append("\r\n$answerText")
            display.scrollTo(0, display.bottom)
            attemptTime = 0
            check.isEnabled = true
            input.setText("")
            input.hint = resources.getString(R.string.hint)
        }

        check.setOnClickListener {
            if (input.text.toString() != "") {
                if (input.text.length == 4) {
                    attemptTime++
                    inputAnswer = input.text.toString()
                    val result = verifyAnswer(inputAnswer)
                    input.setText("")
                    input.hint = inputAnswer
                    display.append("\r\n$attemptTime : $inputAnswer -- $result")
                    display.scrollTo(0, display.bottom)
                    if (result == "4A0B") {
                        display.append("\r\n${resources.getString(R.string.win)}\r\n\r\n")
                        check.isEnabled = false
                    }
                    if (attemptTime == 10) {
                        display.append("\r\n${resources.getString(R.string.lose)} $answerText")
                        display.scrollTo(0, display.bottom)
                        input.setText("")
                        input.hint = inputAnswer
                        check.isEnabled = false
                    }
                } else {
                    input.setText("")
                    input.hint = resources.getString(R.string.hint)
                }
            }
        }

        val information = findViewById<Button>(R.id.information)

        information.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("How to Play")
            builder.setMessage(
                resources.getString(R.string.information).trimMargin()
            )
            builder.setPositiveButton("Confirm") { _, _ ->
                println("confirm")
            }
            val dialog = builder.create()
            dialog.show()
        }

        input.onSubmit { submit() }

    }

    private fun generateAnswer() {
        val numbers: ByteArray = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        var temp: Byte
        var rand: Int

        for (i: Int in 0..9) {
            rand = ((Math.random()) * 10).toInt()
            temp = numbers[rand]
            numbers[rand] = numbers[i]
            numbers[i] = temp
        }
        for (i: Int in 0 until 4) {
            answer[i] = numbers[i]
        }
    }

    private fun verifyAnswer(input: String): String {
        var a = 0
        var b = 0
        for (index: Int in 0 until 4) {
            if (answerText.contains(input[index])) {
                if (answerText.indexOf(input[index]) == index) {
                    a++
                } else {
                    b++
                }
            }
        }
        return "${a}A${b}B"
    }

    private fun EditText.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                func()
            }
            true
        }
    }

    private fun submit() {
        val check = findViewById<Button>(R.id.check)
        check.callOnClick()
    }
}
