/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import android.content.res.Resources
import androidx.lifecycle.Observer
import uk.co.sngconsulting.cakelist.R
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepository

/**
 * Controller for the [CakeListView].
 * Observes changes to the [CakeListViewModel] and updates the view accordingly
 */
class CakeListController(
    private val cakeListView: CakeListView,
    private val cakeListViewModel: CakeListViewModel,
    private val resources: Resources
) : CakeSelectedListener {

    init {
        cakeListViewModel.cakes.observe(cakeListView, Observer { cakes ->
            cakeListView.updateCakes(cakes)
        })

        // initial data load
        refresh()
    }

    /**
     * Refreshes the list of cakes from the server, reporting any errors to the view
     */
    fun refresh() {
        cakeListView.dismissError()

        cakeListViewModel.refresh { result ->
            when (result) {
                CakeRepository.CakeRepositoryResult.SUCCESS -> { /* There is currently nothing to do on success. If some kind of refresh indicator is shown, we could dismiss it here */
                }
                CakeRepository.CakeRepositoryResult.API_ERROR -> cakeListView.showError(resources.getString(R.string.cakes_message_unable_to_refresh))
                CakeRepository.CakeRepositoryResult.NETWORK_ERROR -> cakeListView.showError(resources.getString(R.string.cakes_message_no_connection))
            }
        }
    }

    // CakeSelectedListener

    override fun cakeSelected(cake: Cake) {
        cakeListView.showCakeDetail(cake)
    }
}