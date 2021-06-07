package com.herblabs.herbifyapp.view.ui.identify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.Feedback
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.local.entity.PredictedEntity
import com.herblabs.herbifyapp.data.source.remote.response.Data
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.databinding.ActivityIdentifyBinding
import com.herblabs.herbifyapp.view.adapter.IdentifyAdapter
import com.herblabs.herbifyapp.view.ui.main.MainActivity
import com.herblabs.herbifyapp.view.ui.signin.SignInActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class IdentifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifyBinding
    private val viewModel: IdentifyViewModel by viewModels()
    private lateinit var feedback: Feedback
    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        const val TAG = "IdentifyActivity!!"
        const val EXTRA_CAPTURE = "extra_capture"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SignInActivity.SIGN_IN_PREF, Context.MODE_PRIVATE)
        val email = sharedPreferences.getString(SignInActivity.EXTRA_EMAIL, "")

        val captureEntity = intent.getParcelableExtra<CaptureEntity>(EXTRA_CAPTURE)
        Log.d(TAG,"$captureEntity")

        var identify = HerbsResponse()
        if (captureEntity != null){
            binding.imgResult.setImageURI(Uri.parse(captureEntity.imageUri))
            viewModel.getPredictedByCaptureId(captureEntity.captureId).observe(this, { listPredicted ->
                if (listPredicted != null){
                    setupAdapter(listPredicted)

                    GlobalScope.launch(Dispatchers.Main) {
                        val listData = ArrayList<Data>()
                        withContext(Dispatchers.Main) {
                            listPredicted.forEach {
                                val data = Data(it.imageUrl, it.name, it.confident)
                                listData.add(data)
                            }
                        }
                        identify = HerbsResponse(listData)

                    }

                }
            })
        }

        binding.btnFeedback.setOnClickListener {
            val singleItems = arrayOf("Hasil kurang sesuai", "Data tidak cocok")
            val checkedItem = 0

            if (identify.data.isNotEmpty()){
                MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    .setTitle(resources.getString(R.string.title_feedback))
                    .setPositiveButton(resources.getString(R.string.ok)){ _, _ ->
                        addFeedbackToFirestore(feedback)
                    }
                    .setSingleChoiceItems(singleItems, checkedItem){ _, which ->
                        feedback = Feedback(identify.imageUploaded!!, email!!, singleItems[which], identify.data)
                    }
                    .setCancelable(true)
                    .show()
            }

        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun setupAdapter(list: List<PredictedEntity>?) {
        val identifyAdapter = IdentifyAdapter(list as List<PredictedEntity>)
        with(binding.rvResultIdentify){
            layoutManager = LinearLayoutManager(this@IdentifyActivity)
            adapter = identifyAdapter
        }
    }

    private fun addFeedbackToFirestore(feedback: Feedback) {
        firebaseFirestore.collection("feedback").add(feedback).addOnCompleteListener {
            if(!it.isSuccessful){
                Log.d(TAG, "addFeedbackToFirestore: ${it.exception}")
            }else{
                Toast.makeText(this, "Your feedback has send", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }
}