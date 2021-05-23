package com.herblabs.herbifyapp.view.ui.identify

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.herblabs.herbifyapp.databinding.ActivityIdentifyBinding
import com.herblabs.herbifyapp.view.ui.main.MainActivity.Companion.EXTRA_IMAGE_URI

class IdentifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifyBinding

    companion object {
        const val TAG = "IdentifyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)
        Log.d(TAG,"$imageUri")
        if (imageUri!=null){
            binding.imgResult.setImageURI(imageUri)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}