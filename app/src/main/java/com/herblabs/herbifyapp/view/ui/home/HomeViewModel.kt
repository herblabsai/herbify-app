package com.herblabs.herbifyapp.view.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.view.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val TAG: String = "HomeViewModel"
    private val _recipe = MutableLiveData<List<Recipe>>()
    private val _herbs = MutableLiveData<List<HerbsFirestore>>()

    val recipe : LiveData<List<Recipe>>
        get() = _recipe

    val herbs : LiveData<List<HerbsFirestore>>
        get() = _herbs

    private var job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    fun getRecipes(db: FirebaseFirestore){
        uiScope.launch {
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
        uiScope.launch {
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}