package com.hack.naturaldeductionscanner.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.activity_proof_view.*
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_activity.btnBack
import java.io.File

class ProofViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proof_view)

        val intent = intent
        val data = intent.extras

        if (data != null) {
            imgProofView.load(File(data.getString("path")))
        }



        //Clicking back button to go back to main activity
        btnBack.setOnClickListener {
            finish()
        }
    }
}
