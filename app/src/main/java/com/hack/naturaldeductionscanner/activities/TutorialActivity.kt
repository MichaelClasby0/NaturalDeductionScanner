package com.hack.naturaldeductionscanner.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.settings_activity.btnBack


class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val intent = intent
        val data = intent.extras

        if (data != null) {
          txtTutorialTitle.text = data.getString("name")
        }


        btnShowAnswer.setOnClickListener {
            if (btnShowAnswer.text == "Show Answer!"){
                btnShowAnswer.text = "Hide Answer!"
                imgAnswer.isVisible = true
            } else {
                btnShowAnswer.text = "Show Answer!"
                imgAnswer.isVisible = false
            }
        }

        //Clicking back button to go back to tutorial menu activity
        btnBack.setOnClickListener {
            finish()
        }
    }
}
