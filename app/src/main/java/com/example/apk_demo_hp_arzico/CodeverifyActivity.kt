package com.example.aplication_aplication_taskman

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CodeverifyActivity : AppCompatActivity() {

    private lateinit var digits: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verify)

        digits = arrayOf(
            findViewById(R.id.digit1),
            findViewById(R.id.digit2),
            findViewById(R.id.digit3),
            findViewById(R.id.digit4),
            findViewById(R.id.digit5),
            findViewById(R.id.digit6)
        )

        // Set autofill hint for SMS one-time-code where available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                for (d in digits) {
                    // Use the platform SMS OTP autofill hint string to avoid compile-time symbol issues
                    d.setAutofillHints("sms_otp")
                }
            } catch (t: Throwable) {
                // ignore on older or unsupported devices
            }
        }

        // Add text watchers to auto-advance and handle paste
        digits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val text = s?.toString() ?: ""
                    if (text.length > 1) {
                        // User pasted the whole code or multiple digits — distribute
                        distributePaste(text)
                        return
                    }

                    if (text.length == 1) {
                        // move to next
                        if (index < digits.size - 1) {
                            digits[index + 1].requestFocus()
                        } else {
                            // last digit — hide keyboard and optionally submit
                            digits[index].clearFocus()
                        }
                    } else if (text.isEmpty()) {
                        // if user deleted, move focus back
                        if (index > 0) {
                            digits[index - 1].requestFocus()
                        }
                    }
                }
            })

            // handle delete key backspace properly by listening to editor action
            editText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    trySubmitCode()
                    true
                } else false
            }
        }

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            trySubmitCode()
        }
    }

    private fun distributePaste(pasted: String) {
        // Remove non-digits
        val digitsOnly = pasted.filter { it.isDigit() }
        if (digitsOnly.length >= 6) {
            for (i in 0 until 6) {
                digits[i].setText(digitsOnly[i].toString())
            }
            digits[5].requestFocus()
        } else {
            // place as many as available
            for (i in digitsOnly.indices) {
                digits[i].setText(digitsOnly[i].toString())
            }
        }
    }

    private fun trySubmitCode() {
        val code = digits.joinToString(separator = "") { it.text.toString() }
        if (code.length == 6 && code.all { it.isDigit() }) {
            // TODO: verify the code with backend
            Toast.makeText(this, "کد: $code", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "کد نامعتبر است", Toast.LENGTH_SHORT).show()
        }
    }
}
