/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import org.junit.Test
import org.mockito.Mockito
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepository
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepositoryCompletionHandler

/**
 *
 */
class CakeListViewModelTest{

    private val mockCakeRepository: CakeRepository = Mockito.mock(CakeRepository::class.java)

    @Test
    fun refreshesCakesFromTheRepositoryOnRequest(){
        val model = CakeListViewModel(mockCakeRepository)
        val fakeCompletion : CakeRepositoryCompletionHandler = {

        }
        model.refresh(fakeCompletion)
        Mockito.verify(mockCakeRepository).refreshCakesFromCakeListService(safeRefEq(fakeCompletion))
    }
}
