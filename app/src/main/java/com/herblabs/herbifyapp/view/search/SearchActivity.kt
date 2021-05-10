package com.herblabs.herbifyapp.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herblabs.herbifyapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}