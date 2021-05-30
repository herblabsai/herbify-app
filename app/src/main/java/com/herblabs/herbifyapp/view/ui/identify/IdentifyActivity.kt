package com.herblabs.herbifyapp.view.ui.identify

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.firebase.model.Feedback
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.data.source.remote.response.HerbsResponse
import com.herblabs.herbifyapp.databinding.ActivityIdentifyBinding
import com.herblabs.herbifyapp.view.adapter.IdentifyAdapter
import com.herblabs.herbifyapp.view.ui.signin.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdentifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifyBinding
    private val viewModel: IdentifyViewModel by viewModels()
    private lateinit var feedback: Feedback
    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        const val TAG = "IdentifyActivity"
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

        val identify = intent.getParcelableExtra<HerbsResponse>("identify")
        if(identify != null){
            Log.d("Result", "onCreate: $identify")
            val identifyAdapter = IdentifyAdapter(identify.data)
            with(binding.rvResultIdentify){
                layoutManager = LinearLayoutManager(this@IdentifyActivity)
                adapter = identifyAdapter
            }
        }

        binding.btnFeedback.setOnClickListener {
            val singleItems = arrayOf("Hasil kurang sesuai", "Kamera jelek")
            val checkedItem = 0

            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                .setTitle(resources.getString(R.string.title_feedback))
                .setPositiveButton(resources.getString(R.string.ok)){ _, _ ->
                    addFeedbackToFirestore(feedback)
                }
                .setSingleChoiceItems(singleItems, checkedItem){ _, which ->
                    feedback = Feedback("", email!!, singleItems[which], identify!!.data)
                }
                .setCancelable(true)
                .show()
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
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
}