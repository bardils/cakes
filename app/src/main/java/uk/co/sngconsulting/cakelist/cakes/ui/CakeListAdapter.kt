/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake
import uk.co.sngconsulting.cakelist.databinding.CakeBinding

/**
 * Adapter for the Cake List RecyclerView
 */
class CakeListAdapter(private val cakeSelectedListener: CakeSelectedListener) :
    RecyclerView.Adapter<CakeListAdapter.CakeViewHolder>() {

    private var cakes: List<Cake>? = null

    /**
     * Updates the list of [Cake] used to populate the [RecyclerView]
     */
    fun updateCakes(newCakes: List<Cake>) {
        if (cakes == null) {
            cakes = newCakes
            notifyItemRangeInserted(0, newCakes.size)
        } else {

            checkNotNull(cakes) { "Cakes should not be null when updating" }
            cakes?.let {

                val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                    override fun getOldListSize(): Int {
                        return cakes!!.size
                    }

                    override fun getNewListSize(): Int {
                        return newCakes.size
                    }

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return cakes!![oldItemPosition] == newCakes[newItemPosition]
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        // currently cakes equality checks all fields, so this is the same as
                        return cakes!![oldItemPosition] == newCakes[newItemPosition]
                    }
                })
                cakes = newCakes
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = CakeBinding.inflate(LayoutInflater.from(parent.context))
        return CakeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cakes?.count() ?: 0
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        checkNotNull(cakes) { "Cakes should not be null when binding view holder" }
        holder.bind(cakes!![position], cakeSelectedListener)
    }

    /**
     * [RecyclerView.ViewHolder] for a [Cake]
     */
    class CakeViewHolder(private val cakeBinding: CakeBinding) :
        RecyclerView.ViewHolder(cakeBinding.root) {

        fun bind(cake: Cake, cakeSelectedListener: CakeSelectedListener) {
            cakeBinding.cake = cake
            cakeBinding.cakeSelectedListener = cakeSelectedListener
            cakeBinding.executePendingBindings()
        }
    }
}