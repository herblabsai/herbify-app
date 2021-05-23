package com.herblabs.herbifyapp.data.source.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.herblabs.herbifyapp.data.source.firebase.model.Recipe
import com.herblabs.herbifyapp.view.MainActivity

class FirestoreDataStore {
    companion object{
        const val TAG = "FirestoreDataStore"
    }

    private fun getRecipes(db: FirebaseFirestore, callback: LoadRecipesCallback) {
        db.collection(MainActivity.PATH_COLLECTION_RECIPES).get()
            .addOnCompleteListener {
                val recipesList = it.result!!.toObjects(Recipe::class.java)
                callback.onAllRecipesReceive(recipesList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents : $exception")
            }
    }

    interface LoadRecipesCallback {
        fun onAllRecipesReceive(recipe: List<Recipe>)
    }
}