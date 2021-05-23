package com.herblabs.herbifyapp.view.ui.welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.databinding.ActivityWelcomeBinding
import com.herblabs.herbifyapp.view.ui.signin.SignInActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var welcomeAdapter: WelcomeAdapter
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = getSharedPreferences(WELCOME_PAGE_PREFERENCE, Context.MODE_PRIVATE)
        if(!preference.getBoolean(INTRO_PREF_EXTRA, true)){
            goToMainPage()
        }


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
        binding.viewPager.isUserInputEnabled = false

        binding.btnNext.setOnClickListener {
            if(binding.viewPager.currentItem == welcomeAdapter.itemCount - 1){
                goToMainPage()
            }else{
                binding.viewPager.currentItem += 1
                binding.btnNext.text = "Get Started"
            }
        }
    }

    private fun goToMainPage() {
        startActivity(Intent(this@WelcomeActivity, SignInActivity::class.java))
        finish()
        val editor = preference.edit()
        editor.putBoolean(INTRO_PREF_EXTRA, false)
        editor.apply()
    }

    companion object{
        const val WELCOME_PAGE_PREFERENCE = "welcome_preference"
        const val INTRO_PREF_EXTRA = "intro_pref_extr"
    }
}