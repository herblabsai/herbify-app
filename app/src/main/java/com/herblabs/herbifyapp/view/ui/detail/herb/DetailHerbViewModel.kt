package com.herblabs.herbifyapp.view.ui.detail.herb

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.view.ui.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailHerbViewModel : ViewModel() {
    private val TAG: String = "DetailViewModel"
    private val _recipe = MutableLiveData<List<Recipe>>()

    val recipe : LiveData<List<Recipe>>
        get() = _recipe

    private var job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    fun getRecipes(db: FirebaseFirestore, id: String){
        uiScope.launch {
            try {
                db.collection(MainActivity.PATH_COLLECTION_RECIPES)
                    .whereEqualTo(FieldPath.documentId(), id)
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}