/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito

/**
 *
 */
fun <T> safeAny(): T {
    Mockito.any<T>()
    return uninitialized()
}

fun <T : Any> safeRefEq(value: T): T = eq(value) ?: value

fun <T> uninitialized(): T = null as T