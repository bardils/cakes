/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

/**
 * Dao for the [CakesDatabase]
 */
@Dao
interface CakesDao {

    // Create

    @Insert(onConflict = REPLACE)
    fun insertCakes(vararg cake: Cake)

    // Read

    @Query("SELECT * FROM cakes ORDER BY title DESC")
    fun loadAllCakesOrderedByTitle(): LiveData<List<Cake>>
}