package com.hack.naturaldeductionscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardAdapter = ProofCardItemAdapter {

        }


        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cardAdapter

        }

        //Clicking settings button to open settings activity
        btnSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        //Clicking tutorial button to open tutorial activity
        btnHelp.setOnClickListener {
            val tutorialIntent = Intent(this, TutorialMenuActivity::class.java)
            startActivity(tutorialIntent)
        }

        //val testCard = ProofCard("Title", "08/02/2020", "True", "Image")

        val storageFile = this.getExternalFilesDir(null)


        val logicImagePath = storageFile!!.absolutePath + "/"

        val testImage = logicImagePath + "Proof1.png"

        //Log.d("FILE PATH", testImage)

        val list = ArrayList<ProofCard>()
        for (x in 0..10) {
            list.add(ProofCard("Title $x", "08/02/2020", "True", testImage))
        }
        cardAdapter.submitList(list)
    }
}
