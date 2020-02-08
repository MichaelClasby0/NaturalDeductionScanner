package com.hack.naturaldeductionscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_tutorial_menul.*
import kotlinx.android.synthetic.main.settings_activity.btnBack

class TutorialMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_menul)

        val tutorialCardAdapter = TutorialCardItemAdapter {

        }

        tutorialRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TutorialMenuActivity)
            adapter = tutorialCardAdapter

        }

        val list = ArrayList<TutorialCard>()
        for (x in 0..10) {
            list.add(
                TutorialCard(
                    "Tutorial $x",
                    "Test tutorial description To teach you how to do something sure why not Test tutorial description testing testing testing".trimMargin(),
                    "Completed!"
                )
            )
        }
        tutorialCardAdapter.submitList(list)

        //Clicking back button to go back to main activity
        btnBack.setOnClickListener {
            finish()
        }
    }
}
