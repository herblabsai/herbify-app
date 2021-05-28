package com.herblabs.herbifyapp.data.source.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.vo.Resource
import java.lang.Exception
import javax.inject.Inject

class FirestoreDataStore @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    companion object{
        const val TAG = "FirestoreDataStore"
        const val PATH_COLLECTION_HERBS = "herbs"
        const val PATH_COLLECTION_RECIPES = "recipes"
    }


    fun getHerbs() : LiveData<Resource<List<HerbsFirestore>>> {
        val result = MutableLiveData<Resource<List<HerbsFirestore>>>()

        try {
            result.value = Resource.loading(null)
            firestore.collection(PATH_COLLECTION_HERBS).get()
                .addOnCompleteListener {
                    val herbsList = it.result!!.toObjects(HerbsFirestore::class.java)
                    result.value = Resource.success(herbsList)
                }
                .addOnFailureListener { exception ->
                    result.value = Resource.error("$exception", null)
                    Log.e(TAG, "Error getting documents : $exception")
                }
        } catch (e : Exception){
            Log.e(TAG, e.printStackTrace().toString())
        }

        return result
    }

    fun getRecipes(): LiveData<Resource<List<Recipe>>> {
        val result = MutableLiveData<Resource<List<Recipe>>>()

        try {
            result.value = Resource.loading(null)
            firestore.collection(PATH_COLLECTION_RECIPES).get()
                .addOnCompleteListener {
                    val recipeList = it.result!!.toObjects(Recipe::class.java)
                    result.value = Resource.success(recipeList)
                }
                .addOnFailureListener { exception ->
                    result.value = Resource.error("$exception", null)
                    Log.e(TAG, "Error getting documents : $exception")
                }

        } catch (e : Exception){
            Log.e(TAG, e.printStackTrace().toString())
        }

        return result
    }

    fun getHerbsByName(name: String) : LiveData<Resource<List<HerbsFirestore>>> {
        val result = MutableLiveData<Resource<List<HerbsFirestore>>>()

        try {
            result.value = Resource.loading(null)
            firestore.collection(PATH_COLLECTION_HERBS).whereEqualTo("name", name).get()
                .addOnCompleteListener {
                    val herbsList = it.result!!.toObjects(HerbsFirestore::class.java)
                    result.value = Resource.success(herbsList)
                }
                .addOnFailureListener { exception ->
                    result.value = Resource.error("$exception", null)
                    Log.e(TAG, "Error getting documents : $exception")
                }
        } catch (e : Exception){
            Log.e(TAG, e.printStackTrace().toString())
        }
        return result
    }

    fun getRecipeByDocumentID(id: String) : LiveData<List<Recipe>> {
        val result = MutableLiveData<List<Recipe>>()

        try {
            firestore.collection(PATH_COLLECTION_HERBS).whereEqualTo(FieldPath.documentId(), id).get()
                .addOnCompleteListener {
                    val herbsList = it.result!!.toObjects(Recipe::class.java)
                    result.value = herbsList
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents : $exception")
                }
        } catch (e : Exception){
            Log.e(TAG, e.printStackTrace().toString())
        }
        return result

    }
}