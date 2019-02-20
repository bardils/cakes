/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.model.retrofit

import retrofit2.Call
import retrofit2.http.GET
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake

interface CakeListService {

    @GET("t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client")
    fun getCakes(): Call<Set<Cake>>

}