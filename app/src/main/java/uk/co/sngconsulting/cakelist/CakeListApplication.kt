/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist

import android.app.Application
import uk.co.sngconsulting.cakelist.cakes.provider.CakesProvider

/**
 *
 */
class CakeListApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CakesProvider.initialise(applicationContext)
    }
}