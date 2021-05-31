package com.herblabs.herbifyapp.view.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.herblabs.herbifyapp.data.HerbsRepository
import com.herblabs.herbifyapp.data.source.firebase.model.HerbsFirestore
import com.herblabs.herbifyapp.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: HerbsRepository
) : ViewModel(){

    fun searchHerbs(keyword: String): MutableLiveData<Resource<List<HerbsFirestore>>> =
        repository.searchHerbs(keyword)
}
