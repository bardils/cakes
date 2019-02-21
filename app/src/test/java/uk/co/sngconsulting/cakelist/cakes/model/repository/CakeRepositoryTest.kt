/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.model.repository

import android.os.Handler
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.sngconsulting.cakelist.BuildConfig
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake
import uk.co.sngconsulting.cakelist.cakes.model.data.CakesDao
import uk.co.sngconsulting.cakelist.cakes.model.retrofit.CakeListService
import uk.co.sngconsulting.cakelist.cakes.ui.safeAny
import uk.co.sngconsulting.cakelist.cakes.ui.safeRefEq


/**
 *
 */
class CakeRepositoryTest {

    private val mockHandler: Handler = Mockito.mock(Handler::class.java)

    private val mockCakesDao: CakesDao = Mockito.mock(CakesDao::class.java)

    private val mockWebServer = MockWebServer()

    private val mockCakeListService: CakeListService
        get() {

            val baseUrl = mockWebServer.url("/test/")

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CakeListService::class.java)
        }


    private val validSingleCakeJson = """
            [{
	"title": "title1",
	"desc": "desc1",
	"image": "image1"
}]
        """.trimIndent()

    private val validSingleCake = Cake("title1", "desc1", "image1")


    private val validMultipleCakesJson = """
        [{
		"title": "title1",
		"desc": "desc1",
		"image": "image1"
	},
	{
		"title": "title2",
		"desc": "desc2",
		"image": "image2"
	},
	{
		"title": "title3",
		"desc": "desc3",
		"image": "image3"
	}
]
    """.trimIndent()

    private val validMultipleCakes = arrayOf(
        Cake("title1", "desc1", "image1"),
        Cake("title2", "desc2", "image2"),
        Cake("title3", "desc3", "image3"))

    private val multipleCakesWithDuplicatesJson = """
        [{
		"title": "title1",
		"desc": "desc1",
		"image": "image1"
	},
	{
		"title": "title2",
		"desc": "desc2",
		"image": "image2"
	},
	{
		"title": "title3",
		"desc": "desc3",
		"image": "image3"
	},
{
		"title": "title1",
		"desc": "desc1",
		"image": "image1"
	},
	{
		"title": "title2",
		"desc": "desc2",
		"image": "image2"
	},
	{
		"title": "title3",
		"desc": "desc3",
		"image": "image3"
	}
]
    """.trimIndent()

    private val validMultipleCakesWithDuplicatesRemoved = validMultipleCakes

    @Before
    fun setup() {
        Mockito.`when`(mockHandler.post(safeAny())).thenAnswer {
            (it.arguments[0] as Runnable).run()
            null
        }
    }

    @Test
    fun refreshIsSuccessfulWhenASingleValidCakeIsReturnedFromTheCakeListService() {
        refreshCakesFromCakeListService(validSingleCakeJson) {
            assertEquals(CakeRepository.CakeRepositoryResult.SUCCESS, it)
        }
    }

    @Test
    fun refreshInsertsASingleCakeIntoTheDatabaseWhenASingleValidCakeIsReturnedFromTheCakeListService() {
        refreshCakesFromCakeListService(validSingleCakeJson) {
            Mockito.verify(mockCakesDao).insertCakes(safeRefEq(validSingleCake))
        }
    }

    @Test
    fun refreshInsertsMultipleCakesWhenReturnedFromCakeListService() {
        refreshCakesFromCakeListService(validMultipleCakesJson) {
            Mockito.verify(mockCakesDao).insertCakes(*validMultipleCakes)
        }
    }

    @Test
    fun refreshDoesNotInsertDuplicatesWhenCakeListServiceReceivesDuplicates() {
        refreshCakesFromCakeListService(multipleCakesWithDuplicatesJson) {
            Mockito.verify(mockCakesDao).insertCakes(*validMultipleCakesWithDuplicatesRemoved)
        }
    }

    // TODO: Error handling tests

    // Helpers

    private fun refreshCakesFromCakeListService(
        fakeResponseBody: String,
        onCompletion: CakeRepositoryCompletionHandler

    ) {
        mockWebServer.enqueue(MockResponse().setBody(fakeResponseBody))
        mockWebServer.start()

        val cakeRepository = CakeRepository(mockHandler, mockCakeListService, mockCakesDao)
        cakeRepository.refreshCakesFromCakeListService(onCompletion)
    }


}


