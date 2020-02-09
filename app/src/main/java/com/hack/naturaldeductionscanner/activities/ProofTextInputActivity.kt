package com.hack.naturaldeductionscanner.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.settings_activity.btnBack
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_proof_text_input.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.view.View
import android.widget.TextView
import com.hack.naturaldeductionscanner.proofverification.ProofVerifier


class ProofTextInputActivity : AppCompatActivity() {

     private fun processLines(allLines: String): String {
        val proofVerifier = ProofVerifier()
        val result: Int = proofVerifier.verifyProof(allLines)
        //0 proof is fine
        //-1 parsing error
        //otherwise = line error number
        Log.d("input", allLines)
        Log.d("Proof Result", result.toString())

        var verificationMessage = ""

        verificationMessage = when (result) {
            0 -> "Proof is correct!"
            (-1) -> "Parsing error, check your proof is in the correct layout"
            else -> "Error on line$result"
        }

        return verificationMessage


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proof_text_input)

        val linearLayout = findViewById<LinearLayout>(R.id.linLayoutTextHolder)

        var lineAdded = false

        var noOfLines = 0

        btnNewLine.setOnClickListener {
            noOfLines++
            // Create EditText
            val editText = EditText(this)
            editText.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            editText.setPadding(20, 20, 20, 20)
            editText.id = noOfLines
            editText.setTextColor(Color.WHITE)
            editText.isSingleLine = true
            // Add EditText to LinearLayout
            linearLayout?.addView(editText)

        }


        val textView = TextView(this)
        textView.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        textView.setPadding(20, 20, 20, 20)
        textView.setTextColor(Color.WHITE)

        textView.textSize = 18f

        btnSave.setOnClickListener {
            val linesList = ArrayList<String>()
            linesList.clear()
            linesList.add(editTextLine1.text.toString())//Adds the first line

            var linesConcat = editTextLine1.text.toString().replace("\\s".toRegex(), "")

            for (i in 1..noOfLines) {
                val nextLine = (findViewById<View>(i) as EditText).text.toString()
                if (nextLine != "") {
                    linesList.add(nextLine.replace("\\s".toRegex(), ""))
                    linesConcat += nextLine.replace("\\s".toRegex(), "")
                }
            }
            val verificationMessage = processLines(linesConcat)

            textView.text = verificationMessage


            // Add TextView to LinearLayout
            if (!lineAdded) {
                linearLayout?.addView(textView)
                lineAdded = true
            }
        }


        //Clicking back button to go back to main activity
        btnBack.setOnClickListener {
            finish()
        }


    }
}

