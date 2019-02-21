/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.model.repository

import android.os.Handler
import androidx.lifecycle.LiveData
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake
import uk.co.sngconsulting.cakelist.cakes.model.data.CakesDao
import uk.co.sngconsulting.cakelist.cakes.model.retrofit.CakeListService
import java.io.IOException

/**
 * A repository for [Cake]
 * TODO: unit test
 *
 */
typealias CakeRepositoryCompletionHandler = (result: CakeRepository.CakeRepositoryResult) -> Unit

class CakeRepository(
    private val handler: Handler,
    private val cakeListService: CakeListService,
    private val cakesDao: CakesDao
) {

    enum class CakeRepositoryResult {
        NETWORK_ERROR,
        API_ERROR,
        SUCCESS
    }

    /**
     * Loads all current [Cake], ordering by the [Cake.title]
     */
    fun loadCakesOrderedByTitle(onCompletion: CakeRepositoryCompletionHandler? = null): LiveData<List<Cake>> {
        return cakesDao.loadAllCakesOrderedByTitle()
    }


    /**
     * Downloads [Cake] from the [CakeListService] storing them in the [CakesDao] if successful.
     * Callers can supply an [onCompletion] function to be informed when the refresh is complete, if required.
     */
    fun refreshCakesFromCakeListService(onCompletion: CakeRepositoryCompletionHandler? = null) {
        val call = cakeListService.getCakes()
        handler.post {
            val response = try {
                call.execute()
            } catch (ioe: IOException) {
                null
            }

            when {
                response == null -> onCompletion?.invoke(CakeRepositoryResult.NETWORK_ERROR)
                response.isSuccessful -> {
                    response.body()?.let { cakes ->
                        cakesDao.insertCakes(*cakes.toTypedArray())
                    }
                    onCompletion?.invoke(CakeRepositoryResult.SUCCESS)
                }
                else ->
                    // TODO: the API doesn't seem to send back any custom errors. Determine if this is the case and act accordingly.
                    onCompletion?.invoke(CakeRepositoryResult.API_ERROR)
            }

        }
    }

}
