package com.herblabs.herbifyapp.view.identify

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herblabs.herbifyapp.databinding.ActivityIdentifyBinding
import com.herblabs.herbifyapp.view.MainActivity

class IdentifyActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_IMAGE_URI = "extra_image"
    }
    private lateinit var binding: ActivityIdentifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)
        binding.imgResult.setImageURI(imageUri)

        binding.btnBack.setOnClickListener {
            goToMainPage()
        }
    }

    private fun goToMainPage() {
        startActivity(Intent(this@IdentifyActivity, MainActivity::class.java))
        finish()
    }
}