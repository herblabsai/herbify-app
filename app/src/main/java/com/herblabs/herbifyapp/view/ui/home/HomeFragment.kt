package com.herblabs.herbifyapp.view.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.databinding.FragmentHomeBinding
import com.herblabs.herbifyapp.utils.HorizontalMarginItemDecoration
import com.herblabs.herbifyapp.utils.VerticalMarginItemDecoration
import com.herblabs.herbifyapp.view.adapter.HerbsAdapter
import com.herblabs.herbifyapp.view.adapter.RecipesAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var herbsAdapter: HerbsAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupToolbar()

        showProgressBarRecipe(true)
        showProgressBarHerbs(true)

        val db = FirebaseFirestore.getInstance()
        getRecipes(db)
        getAllHerbs(db)
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setOnMenuItemClickListener {
                when(it?.itemId){
                    R.id.item_search -> {
//                        val intent = Intent(context, SearchActivity::class.java)
//                        startActivity(intent)
                        Toast.makeText(requireActivity(), "Coming Soon !", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    private fun getRecipes(db: FirebaseFirestore) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.getRecipes(db)
        homeViewModel.recipe.observe({lifecycle}, {
            binding.rvTopRecipe.apply {
                recipesAdapter = RecipesAdapter(it)
                this.adapter = recipesAdapter
                this.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(HorizontalMarginItemDecoration(24))
                recipesAdapter.notifyDataSetChanged()
                showProgressBarRecipe(false)
            }
        })
    }

    private fun getAllHerbs(db: FirebaseFirestore) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.getAllHerbs(db)
        homeViewModel.herbs.observe({lifecycle}, {
            binding.rvHerbs.apply {
                herbsAdapter = HerbsAdapter(it)
                this.adapter = herbsAdapter
                this.layoutManager = LinearLayoutManager(requireActivity())
                addItemDecoration(VerticalMarginItemDecoration(16))
                herbsAdapter.notifyDataSetChanged()
                showProgressBarHerbs(false)
            }
        })
    }

    private fun showProgressBarRecipe(state: Boolean){
        if(state){
            binding.progressBarRecipe.visibility = View.VISIBLE
        }else{
            binding.progressBarRecipe.visibility = View.GONE
        }
    }

    private fun showProgressBarHerbs(state: Boolean){
        if(state){
            binding.progressBarHerbs.visibility = View.VISIBLE
        }else{
            binding.progressBarHerbs.visibility = View.GONE
        }
    }

}