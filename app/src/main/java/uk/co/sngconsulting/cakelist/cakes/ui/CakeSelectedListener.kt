/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import uk.co.sngconsulting.cakelist.cakes.model.data.Cake

/**
 * Listener to observe when a cake is selected in the UI
 */
interface CakeSelectedListener {
    fun cakeSelected(cake: Cake)
}