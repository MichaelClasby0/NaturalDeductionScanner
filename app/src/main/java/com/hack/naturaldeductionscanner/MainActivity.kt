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

        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cardAdapter

        }

        val list = ArrayList<String>()
        for (x in 0..10) {
            list.add("Hello")
        }
        cardAdapter.submitList(list)
    }
}
