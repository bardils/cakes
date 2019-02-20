/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import androidx.lifecycle.Observer

/**
 * Controller for the [CakeListView].
 * Observes changes to the [CakeListViewModel] and updates the view accordingly
 */
class CakeListController(cakeListView: CakeListView, cakeListViewModel: CakeListViewModel) {

    init {
        cakeListViewModel.cakes.observe(cakeListView, Observer { cakes ->
            cakeListView.updateCakes(cakes)
        })
    }

}