package com.herblabs.herbifyapp.view.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.databinding.ActivityCameraBinding
import com.herblabs.herbifyapp.utils.DummyData
import com.herblabs.herbifyapp.view.ui.main.MainActivity
import com.herblabs.herbifyapp.vo.StatusMessage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var binding: ActivityCameraBinding

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var progressDialog: AlertDialog
    private lateinit var savedUri: Uri
    private val viewModel : CameraViewModel by viewModels()

    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        progressDialog = getDialogProgressBar().create()

        binding.back.setOnClickListener { onBackPressed() }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        val childPhoto = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        // Create time-stamped output file to hold the image
        val photoFile = File(outputDirectory, childPhoto)

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture
                .OutputFileOptions
                .Builder(photoFile)
                .build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    Toast.makeText(this@CameraActivity, "Image saving error", Toast.LENGTH_LONG).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Log.d(TAG, msg)

                    // Upload Image
                    uploadImage(photoFile)
                }
            })
    }

    private fun uploadImage(photoFile: File) {
        val herbsResponseCustom = DummyData.getHerbResponse()
        viewModel.uploadPredict(photoFile).observe(this@CameraActivity, { result ->
            if (result!=null){
                when(result.status){
                    StatusMessage.LOADING -> progressDialog.show()
                    StatusMessage.SUCCESS ->{
                        progressDialog.dismiss()
                        addCaptureToDB() //add Image Capture to DB
                        Log.d(TAG, "STATUS SUCCESS :${result.data}")
                        Intent().apply{
                            this.putExtra(MainActivity.EXTRA_RESULT_PREDICTED, herbsResponseCustom) // custom nanti diganti result.data
                            setResult(MainActivity.RESULT_IMAGE_CAPTURE, this)
                            finish()
                        }
                    }
                    StatusMessage.ERROR -> {
                        progressDialog.dismiss()
                        Log.e(TAG, "onUploadResult: ${result.message}")
//                        Toast.makeText(this@CameraActivity, "Error :( ${result.message}", Toast.LENGTH_LONG).show()
                        Toast.makeText(this@CameraActivity, "Error :( )", Toast.LENGTH_LONG).show()
                        // TODO : HANYA UNTUK TEST, NANTI DI HAPUS
                        // -------------------------------------------------
                        addCaptureToDB()  //add Image Capture to DB
                        Log.d(TAG, "STATUS ERROR : ${result.data}")
                        Intent().apply{
                            this.putExtra(MainActivity.EXTRA_RESULT_PREDICTED, herbsResponseCustom)
                            setResult(MainActivity.RESULT_IMAGE_CAPTURE, this)
                            finish()
                        }
                        // --------------------------------------------------
                    }
                    else -> {
                        progressDialog.dismiss()
                        Toast.makeText(this@CameraActivity, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                    }
                }
            } else{
                Log.e(TAG, "onUploadResult: Result Null")
            }
        })
    }

    private fun addCaptureToDB(){
        val mCaptureEntity = CaptureEntity(imageUri = "$savedUri")
        Log.d(MainActivity.TAG, mCaptureEntity.toString())
        viewModel.addCapture(mCaptureEntity)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun getDialogProgressBar(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Loading...")
        val progressBar = ProgressBar(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        progressBar.layoutParams = lp
        builder.setView(progressBar)

        return builder
    }
}