package com.hack.naturaldeductionscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardAdapter = CardItemAdapter {

        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cardAdapter

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
