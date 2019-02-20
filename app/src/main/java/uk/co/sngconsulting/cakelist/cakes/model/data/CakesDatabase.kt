/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A [RoomDatabase] of [Cake]
 */
@Database(entities = [Cake::class], version = 1)
abstract class CakesDatabase : RoomDatabase() {

    abstract val cakesDao: CakesDao

    companion object {

        @Volatile
        private var instance: CakesDatabase? = null

        fun getInstance(context: Context): CakesDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }


        private fun buildDatabase(context: Context) =
            Room
                .databaseBuilder(context.applicationContext, CakesDatabase::class.java, "CakesDb")
                .build()

    }
}