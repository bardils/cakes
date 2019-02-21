/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import uk.co.sngconsulting.cakelist.R
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepository
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepositoryCompletionHandler


/**
 *
 */
class CakeListControllerTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val mockCakeListView: CakeListView = Mockito.mock(CakeListView::class.java)
    private val mockCakeListViewModel: CakeListViewModel = Mockito.mock(CakeListViewModel::class.java)
    private val mockResources: Resources = Mockito.mock(Resources::class.java)

    private lateinit var fakeCakeLiveData: MutableLiveData<List<Cake>>

    @Before
    fun setup() {
        val fakeLifecycle = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
        fakeLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        Mockito.`when`(mockCakeListView.lifecycle).thenReturn(fakeLifecycle)

        fakeCakeLiveData = MutableLiveData()
        Mockito.`when`(mockCakeListViewModel.cakes).thenReturn(fakeCakeLiveData)
    }

    @Test
    fun refreshesCakesOnInitialisation() {
        CakeListController(mockCakeListView, mockCakeListViewModel, mockResources)
        Mockito.verify(mockCakeListViewModel).refresh(safeAny())
    }

    @Test
    fun updatesViewCakesWhenRefreshCompletes() {
        CakeListController(mockCakeListView, mockCakeListViewModel, mockResources)
        val fakeCake1 = Cake("title1", "desc1", "image1")
        val fakeCake2 = Cake("title2", "desc2", "image2")
        val fakeCake3 = Cake("title3", "desc3", "image3")
        val expected = listOf(fakeCake1, fakeCake2, fakeCake3)
        fakeCakeLiveData.value = expected
        Mockito.verify(mockCakeListView).updateCakes(safeRefEq(expected))
    }

    @Test
    fun dismissesErrorWhenRefreshing() {
        CakeListController(mockCakeListView, mockCakeListViewModel, mockResources)
        Mockito.verify(mockCakeListView).dismissError()
    }

    @Test
    fun displaysErrorWhenRefreshFailsDueToApiError() {
        testDisplaysErrorWhenFailedRefresh(
            R.string.cakes_message_unable_to_refresh,
            CakeRepository.CakeRepositoryResult.API_ERROR
        )
    }

    @Test
    fun displaysErrorWhenRefreshFailsDueToNetworkError() {
        testDisplaysErrorWhenFailedRefresh(
            R.string.cakes_message_no_connection,
            CakeRepository.CakeRepositoryResult.NETWORK_ERROR
        )
    }


    private fun testDisplaysErrorWhenFailedRefresh(
        errorStringResource: Int,
        cakeRepositoryResult: CakeRepository.CakeRepositoryResult
    ) {
        val expectedErrorMessage = "ERROR"
        Mockito.`when`(mockResources.getString(errorStringResource))
            .thenReturn(expectedErrorMessage)


        Mockito.`when`(mockCakeListViewModel.refresh(safeAny())).thenAnswer { invocation ->
            val completion = invocation.arguments[0] as CakeRepositoryCompletionHandler
            completion(cakeRepositoryResult)

        }
        CakeListController(mockCakeListView, mockCakeListViewModel, mockResources)
        Mockito.verify(mockCakeListView).showError(safeRefEq(expectedErrorMessage))
    }


}