package com.herblabs.herbifyapp.view.ui.identify

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.databinding.ActivityIdentifyBinding
import com.herblabs.herbifyapp.view.ui.main.MainActivity.Companion.EXTRA_CAPTURE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdentifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifyBinding
    private val viewModel: IdentifyViewModel by viewModels()

    companion object {
        const val TAG = "IdentifyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val captureEntity = intent.getParcelableExtra<CaptureEntity>(EXTRA_CAPTURE)
        Log.d(TAG,"$captureEntity")
        if (captureEntity != null){
            binding.imgResult.setImageURI(Uri.parse(captureEntity.imageUri))
            viewModel.getCaptureWithPredicted(captureEntity.captureId).observe(this, {
                if (it != null ){
                    Log.d(TAG,"${it.mCapture}")
                    Log.d(TAG,"${it.mCapture.captureId}")
                    Log.d(TAG,"${it.mCapture.imageUri}")
                    Log.d(TAG,"${it.mPredicted}")
                }
            })
        }


        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}