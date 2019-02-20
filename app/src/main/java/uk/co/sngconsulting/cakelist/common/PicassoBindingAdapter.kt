/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.common

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

/**
 * [BindingAdapter] that allows Picasso image loading with databinding
 */
@BindingAdapter("imageUrl", "error", "placeholder")
fun loadImage(view: ImageView, url: String?, error: Drawable, placeHolder: Drawable) {
    var urlToLoad = url
    if (urlToLoad != null && urlToLoad.isEmpty()) {
        urlToLoad = null // convert to null so that Picasso shows a placeholder for missing URLs
    }

    Picasso.get().load(urlToLoad).placeholder(placeHolder).error(error).into(view)
}