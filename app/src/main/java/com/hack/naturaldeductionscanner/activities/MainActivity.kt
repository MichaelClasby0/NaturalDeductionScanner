package com.hack.naturaldeductionscanner.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.FileObserver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hack.naturaldeductionscanner.R
import com.hack.naturaldeductionscanner.adapters.ProofCard
import com.hack.naturaldeductionscanner.adapters.ProofCardItemAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private val cards: MutableLiveData<MutableList<ProofCard>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_camera.setOnClickListener {
            startActivityForResult(Intent(this, CameraActivity::class.java), 1)
        }

        val cardAdapter = ProofCardItemAdapter {
            }


        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cardAdapter

        }

        btnSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        btnHelp.setOnClickListener {
            val tutorialIntent = Intent(this, TutorialMenuActivity::class.java)
            startActivity(tutorialIntent)
        }

        getAllProofs()

        cards.observe(this, androidx.lifecycle.Observer {
            cardAdapter.submitList(it)
        })

        val observer = object :
            FileObserver(applicationContext.getExternalFilesDir(null)!!) {
            override fun onEvent(p0: Int, p1: String?) {
                if (!cards.value!!.map { it.image == p1 }.any()) {
                    cards.value!!.add(getProofCard(File(p1!!)))
                }
            }
        }

        observer.startWatching()


    }

    private fun postRequest(postUrl: String, postBody: RequestBody) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(postUrl)
            .post(postBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("ERROR", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("RES", response.body!!.string())
            }

        })
    }


    private fun getProofCard(currentFile: File): ProofCard {
        val logicImagePath = applicationContext.getExternalFilesDir(null)!!.absolutePath + "/"
        Log.e("FilePath", currentFile.absolutePath)
        // File Name
        Log.e("FileName", currentFile.name)
        val tempFile = File(logicImagePath + currentFile.name)
        val modifiedDate = Date(tempFile.lastModified())

        return ProofCard(
            currentFile.name,
            modifiedDate.toString(),
            "True",
            logicImagePath + currentFile.name
        )

    }


    private fun getAllProofs() {
        val storageFile = applicationContext.getExternalFilesDir(null)

        val list = mutableListOf<ProofCard>()

        val allFiles = storageFile!!.listFiles()
        if (allFiles != null && allFiles.isNotEmpty()) {
            for (currentFile in allFiles) {
                if (currentFile.name.endsWith(".jpeg")) {
                    // File absolute path
                    list.add(getProofCard(currentFile))
                }
            }
        }

        cards.value = list
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("file")!!
                val postUrl = "http://195.154.114.164:6200"

                val stream = ByteArrayOutputStream()
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.RGB_565
                val bitmap = BitmapFactory.decodeFile(result, options)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()

                val postBodyImage = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "logic.jpg",
                        result,
                        byteArray.toRequestBody("image/*jpg".toMediaTypeOrNull(), 0, byteArray.size)
                    )
                    .build()

                postRequest(postUrl, postBodyImage)

            }
        }
    }
}
