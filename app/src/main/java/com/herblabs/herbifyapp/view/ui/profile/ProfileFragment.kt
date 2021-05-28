package com.herblabs.herbifyapp.view.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.data.source.local.entity.CaptureEntity
import com.herblabs.herbifyapp.databinding.FragmentProfileBinding
import com.herblabs.herbifyapp.view.adapter.CaptureAdapter
import com.herblabs.herbifyapp.view.ui.signin.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var mCaptureAdapter : CaptureAdapter
    private val viewModel : ProfileViewModel by viewModels()

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCaptureAdapter = CaptureAdapter(requireActivity())
        binding.rvCapture.setHasFixedSize(false)
        binding.rvCapture.adapter = mCaptureAdapter

        viewModel.getCapture().observe(viewLifecycleOwner , captureObserver)

        if (activity != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

            sharedPreferences = requireActivity().getSharedPreferences(SignInActivity.SIGN_IN_PREF, Context.MODE_PRIVATE)
            binding.tvName.text = sharedPreferences.getString(SignInActivity.EXTRA_USERNAME, "")

            // sementara di buat untuk logout
            binding.btnSettings.setOnClickListener {
                googleSignInClient.signOut().addOnCompleteListener {
                    startActivity(Intent(requireContext(), SignInActivity::class.java))
                }
            }
        }

    }

    private val captureObserver = Observer<PagedList<CaptureEntity>> { captureList ->
        showWarning(false)
        if (captureList != null) {
            if (captureList.toString() == "[]"){
                showWarning(true)
            }
            mCaptureAdapter.submitList(captureList)
        }
    }

    private fun showWarning(state: Boolean) {
        val stateView = if (state) {
            View.VISIBLE
        } else {
            View.GONE
        }
        // TODO : BUAT WARNING DATA KOSONG
//        binding.warning.visibility = stateView
    }


}