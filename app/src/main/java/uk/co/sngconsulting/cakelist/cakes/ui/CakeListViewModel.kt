/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepository


/**
 * Factory to create a [CakeListViewModel]
 */
class CakeListViewModelFactory(private val cakeListRepository: CakeRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CakeListViewModel::class.java)){
            CakeListViewModel(cakeListRepository) as T
        } else {
            throw IllegalArgumentException("Invalid model view class type")
        }
    }
}

/**
 * [ViewModel] for the [CakeListView]
 */
class CakeListViewModel(private val cakeListRepository: CakeRepository) : ViewModel() {

    val cakes = cakeListRepository.loadCakesOrderedByTitle()

    /**
     * Wrapper method for the [CakeRepository]
     */
    fun refresh(onCompletion: (result: CakeRepository.CakeRepositoryResult) -> Unit){
        cakeListRepository.refreshCakesFromCakeListService(onCompletion)
    }

}