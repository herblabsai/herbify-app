package com.herblabs.herbifyapp.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.databinding.FragmentHomeBinding
import com.herblabs.herbifyapp.utils.HorizontalMarginItemDecoration
import com.herblabs.herbifyapp.utils.VerticalMarginItemDecoration
import com.herblabs.herbifyapp.view.adapter.HerbsAdapter
import com.herblabs.herbifyapp.view.adapter.RecipesAdapter
import com.herblabs.herbifyapp.view.ui.search.SearchActivity
import com.herblabs.herbifyapp.vo.StatusMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var herbsAdapter: HerbsAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private var state = false
    private var state2 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object{
        const val TAG = "HomeFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        getRecipes()
        getAllHerbs()
    }

    private fun getAllHerbs() {
        homeViewModel.getAllHerbs().observe(viewLifecycleOwner, {
            if(it != null){
                when(it.status){
                    StatusMessage.LOADING -> {
                        state = true
                        showShimmeringLoading(state,state2)
                    }
                    StatusMessage.SUCCESS -> {
                        binding.rvHerbs.apply {
                            herbsAdapter = HerbsAdapter(it.data!!)
                            this.adapter = herbsAdapter
                            this.layoutManager = LinearLayoutManager(requireActivity())
                            addItemDecoration(VerticalMarginItemDecoration(16))
                            herbsAdapter.notifyDataSetChanged()
                            state = false
                            showShimmeringLoading(state,state2)
                        }
                    }
                    StatusMessage.ERROR -> {
                        state = false
                        showShimmeringLoading(state,state2)
                        Log.e(TAG, "getAllHerbs: ${it.message}")
                    }
                    StatusMessage.EMPTY -> {
                        state = false
                        showShimmeringLoading(state,state2)
                        Toast.makeText(requireActivity(), "Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getRecipes() {
        homeViewModel.getRecipes().observe(viewLifecycleOwner, {
            if(it != null){
                when(it.status){
                    StatusMessage.LOADING -> {
                        state2 = true
                        showShimmeringLoading(state,state2)
                    }
                    StatusMessage.SUCCESS -> {
                        binding.rvTopRecipe.apply {
                            recipesAdapter = RecipesAdapter(it.data!!)
                            this.adapter = recipesAdapter
                            this.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                            addItemDecoration(HorizontalMarginItemDecoration(24))
                            recipesAdapter.notifyDataSetChanged()
                            state2 = false
                            showShimmeringLoading(state,state2)
                        }
                    }
                    StatusMessage.ERROR -> {
                        state2 = false
                        showShimmeringLoading(state,state2)
                        Log.e(TAG, "getRecipes: ${it.message}")
                    }
                    StatusMessage.EMPTY -> {
                        state2 = false
                        showShimmeringLoading(state,state2)
                        Toast.makeText(requireActivity(), "Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setOnMenuItemClickListener {
                when(it?.itemId){
                    R.id.item_search -> {
                        val intent = Intent(context, SearchActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }
        }
    }

    private fun showShimmeringLoading(state: Boolean, state2: Boolean){
        if(state && state2){
            binding.loadingShimmer.visibility = View.VISIBLE
            binding.dataList.visibility = View.GONE
        }else if (!state && !state2){
            binding.loadingShimmer.visibility = View.GONE
            binding.dataList.visibility = View.VISIBLE
        }
    }

}