package com.herblabs.herbifyapp.view.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.ktx.Firebase
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.databinding.ActivityMainBinding
import com.herblabs.herbifyapp.utils.DummyData
import com.herblabs.herbifyapp.view.ui.camera.CameraActivity
import com.herblabs.herbifyapp.view.ui.identify.IdentifyActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var progressDialog: AlertDialog
    private lateinit var captureEntity: CaptureEntity
    private val viewModel : MainViewModel by viewModels()

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
        binding.fab.setOnClickListener {
            dispatchTakePictureIntent()
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

    private fun dispatchTakePictureIntent() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    companion object{
        private const val REQUEST_IMAGE_CAPTURE = 100
        const val RESULT_IMAGE_CAPTURE = 101
        const val TAG = "MainActivity!!"
        const val EXTRA_CAPTURE = "extra_capture"
        const val EXTRA_RESULT_PREDICTED= "extra_result_predicted"
        const val PATH_COLLECTION_HERBS = "herbs"
        const val PATH_COLLECTION_RECIPES = "recipes"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_IMAGE_CAPTURE) {
            try {
                val herbsResponse = data?.getParcelableExtra<HerbsResponse>(EXTRA_RESULT_PREDICTED)

                viewModel.getLastedCapture().observe(this, {
                    captureEntity = it[0]
                    Log.d(TAG, "4 : $captureEntity")
                    if (herbsResponse != null) {
                        insertPredicted(herbsResponse)
                    }
                })

            } catch (e:Exception){
                Log.e(TAG, "${e.message}")
            }
        }
    }

    private fun insertPredicted(herbsResponse: HerbsResponse){
        if (captureEntity.captureId != 0) {
            Log.d(TAG, "$captureEntity")
            viewModel.addPredicted(herbsResponse,captureEntity)

            Toast.makeText(this@MainActivity, "Here we go !", Toast.LENGTH_LONG).show()
            Intent(this@MainActivity, IdentifyActivity::class.java).apply {
                putExtra(EXTRA_CAPTURE, captureEntity)
                putExtra("identify", herbsResponse)
                startActivity(this)
            }
        }
    }
}