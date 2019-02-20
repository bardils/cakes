/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.cakelist_activity.*
import kotlinx.android.synthetic.main.cakelist_content.*
import uk.co.sngconsulting.cakelist.R
import uk.co.sngconsulting.cakelist.cakes.model.data.Cake
import uk.co.sngconsulting.cakelist.cakes.provider.CakesProvider

interface CakeListView : LifecycleOwner{
    fun updateCakes(cakes: List<Cake>)
    fun showCakeDetail(cake: Cake)
}

class CakeListActivity : AppCompatActivity(), CakeListView {

    private lateinit var cakeListController: CakeListController
    private lateinit var cakeListAdapter: CakeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cakelist_activity)
        setSupportActionBar(toolbar)

        // TODO: move construction to provider



        ContextCompat.getDrawable(this, R.drawable.list_divider)?.let {
            val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            dividerItemDecoration.setDrawable(it)
            cakeListRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        val cakeListViewModel = ViewModelProviders.of(this, CakesProvider.cakeListViewModelFactory).get(CakeListViewModel::class.java)
        cakeListController = CakeListController(this, cakeListViewModel)

        cakeListAdapter = CakeListAdapter(cakeListController)
        cakeListRecyclerView.adapter = cakeListAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cakelist_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // CakeListView

    override fun updateCakes(cakes: List<Cake>) {
        cakeListAdapter.updateCakes(cakes)
    }

    override fun showCakeDetail(cake: Cake) {
        // TODO: make a nicer detail view
        Toast.makeText(this, cake.desc, Toast.LENGTH_LONG).show()
    }
}
