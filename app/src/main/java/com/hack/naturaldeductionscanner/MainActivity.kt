package com.hack.naturaldeductionscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.util.Log
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


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

        val fileNameList = ArrayList<String>()

        val logicImagePath = storageFile!!.absolutePath + "/"

        var allFiles = storageFile!!.listFiles()
        if (allFiles != null && allFiles.size > 0) {
            for (currentFile in allFiles) {
                if (currentFile.name.endsWith(".png")) {
                    // File absolute path
                    Log.e("FilePath", currentFile.absolutePath)
                    // File Name
                    Log.e("FileName", currentFile.name)
                    fileNameList.add(currentFile.name)
                }
            }
        }

        //val testImage = logicImagePath + "Proof1.png"
        val list = ArrayList<ProofCard>()

        for (imageName in fileNameList) {
            val tempFile = File(logicImagePath + imageName)
            val modifiedDate = Date(tempFile.lastModified())
            list.add(ProofCard(imageName, modifiedDate.toString(), "True", logicImagePath + imageName))
        }

//        for (x in 0..10) {
//            list.add(ProofCard("Title $x", "08/02/2020", "True", testImage))
//        }
        cardAdapter.submitList(list)
    }
}
