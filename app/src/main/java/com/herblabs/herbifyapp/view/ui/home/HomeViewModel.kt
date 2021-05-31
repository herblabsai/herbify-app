package com.herblabs.herbifyapp.view.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.view.ui.main.MainActivity
import com.herblabs.herbifyapp.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HerbsRepository
): ViewModel() {
    private val TAG: String = "HomeViewModel"
    private val _recipe = MutableLiveData<List<Recipe>>()
    private val _herbs = MutableLiveData<List<HerbsFirestore>>()

    val recipe : LiveData<List<Recipe>>
        get() = _recipe

    val herbs : LiveData<List<HerbsFirestore>>
        get() = _herbs


    fun getRecipes(db: FirebaseFirestore){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                db.collection(MainActivity.PATH_COLLECTION_RECIPES)
                    .whereEqualTo("isPopular", true)
                    .get()
                    .addOnCompleteListener {
                        val recipesList = it.result!!.toObjects(Recipe::class.java)
                        _recipe.postValue(recipesList)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("MainActivity", "Error getting documents : $exception")
                    }
            }catch (t: Throwable){
                Log.d(TAG, "getRecipes: $t")
            }
        }
    }

    fun getAllHerbs(db: FirebaseFirestore){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                db.collection(MainActivity.PATH_COLLECTION_HERBS).get()
                    .addOnCompleteListener {
                        val herbsList = it.result!!.toObjects(HerbsFirestore::class.java)
                        _herbs.postValue(herbsList)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("MainActivity", "Error getting documents : $exception")
                    }
            }catch (t: Throwable){
                Log.d(TAG, "getRecipes: $t")
            }
        }
    }


}