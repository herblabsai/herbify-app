package com.herblabs.herbifyapp.view.identify

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herblabs.herbifyapp.databinding.ActivityIdentifyBinding
import com.herblabs.herbifyapp.view.MainActivity

class IdentifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageBitmap = intent.getParcelableExtra<Bitmap>(MainActivity.EXTRA_IMAGE)
        binding.imgResult.setImageBitmap(imageBitmap)

        binding.btnBack.setOnClickListener {
            goToMainPage()
        }
    }

    private fun goToMainPage() {
        startActivity(Intent(this@IdentifyActivity, MainActivity::class.java))
        finish()
    }
}