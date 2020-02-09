package com.hack.naturaldeductionscanner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hack.naturaldeductionscanner.R
import com.hack.naturaldeductionscanner.adapters.TutorialCard
import com.hack.naturaldeductionscanner.adapters.TutorialCardItemAdapter
import kotlinx.android.synthetic.main.activity_tutorial_menu.*
import kotlinx.android.synthetic.main.settings_activity.btnBack


class TutorialMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_menu)

        val tutorialCardAdapter =
            TutorialCardItemAdapter { card ->

                val intent = Intent(this, TutorialActivity::class.java)
                intent.putExtra("name", card.title)
                startActivity(intent)

            }



        tutorialRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TutorialMenuActivity)
            adapter = tutorialCardAdapter

        }

        val list = ArrayList<TutorialCard>()
//        for (x in 0..10) {
//            list.add(
//                TutorialCard(
//                    "Tutorial $x",
//                    "Test tutorial description To teach you how to do something sure why not Test tutorial description testing testing testing".trimMargin(),
//                    "Completed!"
//                )
//            )
//        }

        list.add(
            TutorialCard(
                "My first proof!",
                "The first tutorial to introduce you to the And-Introduction rule.".trimMargin(),
                "Not attempted",
                "",
                ""
            )
        )

        list.add(
            TutorialCard(
                "Your second proof!",
                "The second tutorial to introduce you to the And-Elimination rule.".trimMargin(),
                "Not attempted",
                "",
                ""
            )
        )

        tutorialCardAdapter.submitList(list)

        //Clicking back button to go back to main activity
        btnBack.setOnClickListener {
            finish()
        }
    }
}
