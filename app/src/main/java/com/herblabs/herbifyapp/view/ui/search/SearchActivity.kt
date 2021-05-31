package com.herblabs.herbifyapp.view.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.databinding.ActivitySearchBinding
import com.herblabs.herbifyapp.view.adapter.HerbsAdapter
import com.herblabs.herbifyapp.view.ui.main.MainActivity
import com.herblabs.herbifyapp.vo.StatusMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var searchList: List<HerbsFirestore> = ArrayList()
    private var searchListAdapter = HerbsAdapter(searchList)
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = binding.searchView.text.toString()
                viewModel.searchHerbs(query).observe(this@SearchActivity, {
                    if(it != null){
                        when(it.status){
                            StatusMessage.SUCCESS -> {
                                binding.rvSearch.apply {
                                    searchListAdapter = HerbsAdapter(it.data!!)
                                    this.adapter = searchListAdapter
                                    this.layoutManager = LinearLayoutManager(context)
                                    searchListAdapter.notifyDataSetChanged()
                                }
                            }
                            StatusMessage.ERROR -> {
                                Log.e(TAG, "Search: ${it.message}" )
                            }
                            else -> {
                                Log.e(TAG, "Data tidak ditemukan" )
                            }
                        }
                    }
                })

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    companion object{
        const val TAG = "SearchActivity"
    }
}