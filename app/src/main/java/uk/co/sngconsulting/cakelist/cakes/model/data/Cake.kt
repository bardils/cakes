/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.model.data

import androidx.room.Entity

/**
 * Entity representing a cake. As the data from the server has no key and contains duplicates, this
 * class assumes that all fields form part of the primary key.
 * TODO: confirm primary key requirement
 */
@Entity(tableName = "cakes", primaryKeys = ["title", "desc", "image"])
data class Cake(val title: String, val desc: String, val image: String)