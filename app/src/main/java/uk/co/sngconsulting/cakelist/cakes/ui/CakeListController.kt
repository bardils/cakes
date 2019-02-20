/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import androidx.lifecycle.Observer
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake

/**
 * Controller for the [CakeListView].
 * Observes changes to the [CakeListViewModel] and updates the view accordingly
 */
class CakeListController(private val cakeListView: CakeListView, cakeListViewModel: CakeListViewModel) : CakeSelectedListener {

    init {
        cakeListViewModel.cakes.observe(cakeListView, Observer { cakes ->
            cakeListView.updateCakes(cakes)
        })
    }

    // CakeSelectedListener

    override fun cakeSelected(cake: Cake) {
        cakeListView.showCakeDetail(cake)
    }
}