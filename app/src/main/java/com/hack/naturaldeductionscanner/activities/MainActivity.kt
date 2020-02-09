package com.hack.naturaldeductionscanner.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.FileObserver
import android.util.Log
import android.view.View
import androidx.camera.view.CameraView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.hack.naturaldeductionscanner.R
import com.hack.naturaldeductionscanner.adapters.ProofCard
import com.hack.naturaldeductionscanner.adapters.ProofCardItemAdapter
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.internal.MutablePropertyReference


class MainActivity : AppCompatActivity() {
    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i("OpenCV", "OpenCV loaded successfully")
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }

    }

    private val cards: MutableLiveData<MutableList<ProofCard>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_camera.setOnClickListener {
            startActivityForResult(Intent(this, CameraActivity::class.java), 1)
        }

        open_input.setOnClickListener{
            val intent = Intent(this, ProofTextInputActivity::class.java)
            startActivity(intent)
        }

        val cardAdapter =
            ProofCardItemAdapter {path ->
                val intent = Intent(this, ProofViewActivity::class.java)
                intent.putExtra("path", path)
                startActivity(intent)

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



        //val testImage = logicImagePath + "Proof1.png"


//        for (x in 0..10) {
//            list.add(ProofCard("Title $x", "08/02/2020", "True", testImage))
//        }

        getAllProofs()

        cards.observe(this, androidx.lifecycle.Observer {
            cardAdapter.submitList(it)
        })

        val observer = object: FileObserver(applicationContext.getExternalFilesDir(null)!!) { /* set up a file observer to watch this directory on sd card */
            override fun onEvent(p0: Int, p1: String?) {
                if (!cards.value!!.map { it.image == p1 }.any()) {
                    cards.value!!.add(getProofCard(File(p1!!)))
                }
            }
        }

        observer.startWatching()


    }

    private fun getProofCard(currentFile:File): ProofCard {
        val logicImagePath = applicationContext.getExternalFilesDir(null)!!.absolutePath + "/"
        Log.e("FilePath", currentFile.absolutePath)
        // File Name
        Log.e("FileName", currentFile.name)
        val tempFile = File(logicImagePath + currentFile.name)
        val modifiedDate = Date(tempFile.lastModified())

        return ProofCard(currentFile.name, modifiedDate.toString(), "Pending", logicImagePath + currentFile.name,currentFile.absolutePath,"")

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

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d(
                "OpenCV",
                "Internal OpenCV library not found. Using OpenCV Manager for initialization"
            )
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("file")!!
                val myBitmap = BitmapFactory.decodeFile(result)

                val src: Mat = bitmapToMat(myBitmap)
                Imgproc.erode(
                    src,
                    src,
                    Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(5.0, 5.0))
                )
                Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY)
                Imgproc.blur(src, src, Size(5.0, 5.0))
                Imgproc.adaptiveThreshold(
                    src,
                    src,
                    255.0,
                    Imgproc.ADAPTIVE_THRESH_MEAN_C,
                    Imgproc.THRESH_BINARY,
                    21,
                    5.0
                )

                val ei = ExifInterface(result)
                val orientation: Int = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )

                val bitmap = matToBitmap(src)
                val rotatedBitmap = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
                    else -> bitmap
                }

                val fOut = FileOutputStream(result)
                rotatedBitmap!!.compress(Bitmap.CompressFormat.PNG, 85, fOut)
                fOut.flush()
                fOut.close()
            }
        }
    }

    private fun bitmapToMat(bitmap: Bitmap): Mat {
        val mat = Mat(bitmap.height, bitmap.width, CvType.CV_8U, Scalar(4.0))
        val bitmap32: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Utils.bitmapToMat(bitmap32, mat)
        return mat
    }

    private fun matToBitmap(mat: Mat): Bitmap {
        val bitmap = Bitmap.createBitmap(
            mat.cols(),
            mat.rows(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(mat, bitmap)
        return bitmap
    }

    private fun rotateImage(source: Bitmap, angle: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}
