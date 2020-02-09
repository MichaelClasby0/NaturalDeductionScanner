package com.hack.naturaldeductionscanner.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.time.LocalDateTime


class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        capture.setOnClickListener {
            onClick()
        }

        runWithPermissions(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
                return@runWithPermissions
            }
            camera_view.bindToLifecycle(this)
            camera_view.enableTorch(true)
        }

        close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, Intent())
            finish()
        }
    }

    private fun onClick() {
        val file = File(applicationContext.getExternalFilesDir(null)!!.absolutePath + "/Proof - " + LocalDateTime.now()+  ".jpeg")
        camera_view.takePicture(file, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(imageCaptureError: Int, message: String, cause: Throwable?) {
                    finish()
                }

                override fun onImageSaved(file: File) {
                    setResult(Activity.RESULT_OK, Intent().putExtra("file", file.absolutePath))
                    finish()
                }
            })
    }
}