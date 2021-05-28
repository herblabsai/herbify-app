package com.herblabs.herbifyapp.view.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.R
import com.herblabs.herbifyapp.databinding.FragmentHomeBinding
import com.herblabs.herbifyapp.utils.HorizontalMarginItemDecoration
import com.herblabs.herbifyapp.utils.VerticalMarginItemDecoration
import com.herblabs.herbifyapp.view.adapter.HerbsAdapter
import com.herblabs.herbifyapp.view.adapter.RecipesAdapter
import com.herblabs.herbifyapp.vo.StatusMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var herbsAdapter: HerbsAdapter
    private val homeViewModel: HomeViewModel by viewModels()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                        // TODO : TEST DATA REPOSITORY
                        /**
                        homeViewModel.getHerbsByName("Meniran").observe(viewLifecycleOwner, { result ->
                            if (result!=null){
                                when(result.status){
                                    StatusMessage.LOADING -> {
                                        Log.d(TAG, "Loading....")
                                    }
                                    StatusMessage.SUCCESS ->{
                                        Log.d(TAG, "Loading.... Done")
                                        result.data?.forEach { herbs ->
                                            Log.d(TAG, "Result : $herbs")
                                        }
                                    }
                                    StatusMessage.ERROR -> {
                                        Log.d(TAG, "Loading.... Done , Error")
                                    }
                                    else -> { }
                                }
                            }

                        })
                        **/
                        Toast.makeText(requireActivity(), "Coming Soon !", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    private fun getRecipes(db: FirebaseFirestore) {

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