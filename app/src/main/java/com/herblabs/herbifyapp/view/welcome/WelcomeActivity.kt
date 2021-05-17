package com.herblabs.herbifyapp.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.databinding.ActivityWelcomeBinding
import com.herblabs.herbifyapp.view.MainActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var welcomeAdapter: WelcomeAdapter
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        welcomeAdapter = WelcomeAdapter(listOf(
            WelcomeModel(
            "Identify Herbs",
            "Mari mengenal berbagai tumbuhan baru yang tidak Anda kenal.",
            R.drawable.ic_people_searching
            ),
            WelcomeModel(
                "Get Recipes",
                "Jika sudah kenal jangan lupa melihat segudang manfaat yang dibawa.",
                R.drawable.ic_people_walking
            ),
        ))

        binding.viewPager.adapter = welcomeAdapter
        binding.btnNext.setOnClickListener {
            if(binding.viewPager.currentItem + 1 < welcomeAdapter.itemCount){
                binding.viewPager.currentItem += 1
                binding.btnNext.text = "Get Started"
            }else{

                Intent(this@WelcomeActivity, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }
}