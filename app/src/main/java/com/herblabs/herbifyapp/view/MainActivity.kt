package com.herblabs.herbifyapp.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.databinding.ActivityMainBinding
import com.herblabs.herbifyapp.view.camera.CameraActivity
import com.herblabs.herbifyapp.view.identify.IdentifyActivity
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavView.apply {
            background = null
            menu.getItem(1).isEnabled = false
            setupWithNavController(navController)
        }

        progressDialog = getDialogProgressBar().create()

        binding.fab.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }

    companion object{
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val TAG = "MainActivity"
        const val EXTRA_IMAGE = "extra_image"
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: IOException) {
            Log.d(TAG, "dispatchTakePictureIntent: ${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            imageBitmap.run {
                compress(Bitmap.CompressFormat.JPEG, 100, baos)
            }
            val data = baos.toByteArray()

            val imageRef = storageRef.child("images/$timeStamp")

            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                progressDialog.dismiss()
                Log.d(TAG, "onActivityResult: ${it.message}")
                Toast.makeText(this, "Error :( ${it.message.toString()}", Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Here we go !", Toast.LENGTH_LONG).show()
                Intent(this, IdentifyActivity::class.java).apply {
                    putExtra(EXTRA_IMAGE, imageBitmap)
                    startActivity(this)
                }
            }.addOnProgressListener {
                progressDialog.show()
            }

        }
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