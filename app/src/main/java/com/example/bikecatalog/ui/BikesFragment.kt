package com.example.bikecatalog.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.bikecatalog.R
import com.example.bikecatalog.adapter.BikesAdapter
import com.example.bikecatalog.databinding.FragmentBikesBinding
import com.example.bikecatalog.model.Bikes
import com.example.bikecatalog.utils.Constants
import com.example.bikecatalog.viewModel.BikesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class BikesFragment :
    Fragment(R.layout.fragment_bikes) {

    private val viewModel by viewModels<BikesViewModel>()


    private lateinit var binding: FragmentBikesBinding
    private lateinit var items: List<Bikes>
    private lateinit var initialItems: List<Bikes>
    private lateinit var filteredItems: ArrayList<Bikes>
    private lateinit var filteredItemsBySize: ArrayList<Bikes>
    private var minPrice: Float = 0.0f
    private var maxPrice: Float = 0.0f
    private lateinit var frameSize: String

    @Inject
    lateinit var bikesAdapter: BikesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding = FragmentBikesBinding.bind(view)

        initialize()
        listeners()
        observables()

        viewModel.fetchBikes()
        setHasOptionsMenu(true)
    }

    private fun initialize() {
        initConst()
        binding.progress.visibility = View.VISIBLE
        filteredItems = arrayListOf<Bikes>()
        binding.recycler.adapter = bikesAdapter
    }

    private fun initConst() {
        minPrice = Constants.MIN_PRICE
        maxPrice = Constants.MAX_PRICE
        frameSize = ""
    }

    private fun observables() {
        viewModel.progressBarStatus.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.progress.visibility = View.GONE
                binding.error.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.VISIBLE
                binding.error.text = it
            } else {
                binding.progress.visibility = View.GONE
            }
        })

        viewModel.bikeList.observe(viewLifecycleOwner, Observer {

            items = it

            // storing data in initialItems to use when clearing the filters
            initialItems = items
            bikesAdapter.setBikes(items)

        })
    }

    private fun listeners() {

        binding.apply {
            slider.addOnChangeListener { slider, value, fromUser ->
                val values = slider.values
                minPrice = values[0]
                maxPrice = values[1]
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextChange(qString: String): Boolean {
                    bikesAdapter.filter!!.filter(qString)
                    return true
                }

                override fun onQueryTextSubmit(qString: String): Boolean {
                    bikesAdapter.filter!!.filter(qString)
                    searchView.clearFocus()
                    return true
                }
            })

            btnRetry.setOnClickListener {
                viewModel.fetchBikes()
                binding.progress.visibility = View.VISIBLE
                binding.error.visibility = View.GONE
                binding.btnRetry.visibility = View.GONE
            }


            // get selected radio button from radioGroup
            radioGroupSort.setOnCheckedChangeListener { group, checkedId ->
                // checkedId is the RadioButton selected
                if (checkedId == radioAsc.id) {
                    ascending()
                } else if (checkedId == radioDec.id) {
                    descending()
                }
            }

            radioGroupSize.setOnCheckedChangeListener { group, checkedId ->
                // checkedId is the RadioButton selected
                when (checkedId) {
                    radioXS.id -> frameSize = Constants.SIZE_XS
                    radioS.id -> frameSize = Constants.SIZE_S
                    radioM.id -> frameSize = Constants.SIZE_M
                    radioL.id -> frameSize = Constants.SIZE_L
                    radio2xl.id -> frameSize = Constants.SIZE_2XL
                    radio3xl.id -> frameSize = Constants.SIZE_3XL
                }
            }
            btnSubmit.setOnClickListener {
                if (::items.isInitialized)
                    submitFilter()
            }
            btnClear.setOnClickListener {
                clearFilter()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_items, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sort) {
            binding.llSort.visibility = View.VISIBLE
        } else if (item.itemId == R.id.action_filter) {
            binding.llFilter.visibility = View.VISIBLE
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitFilter() {
        filteredItems = arrayListOf<Bikes>()
        filteredItemsBySize = arrayListOf<Bikes>()

        initialItems.forEach { bikes ->
            if (bikes.price!! <= maxPrice && bikes.price >= minPrice) {
                filteredItems.add(bikes)
            }

        }
        if (frameSize != "") {
            // If size filter is selected then our result will be range filter + size filter
            filteredItems.forEach { bikes ->
                if (bikes.frameSize.equals(frameSize)) {
                    filteredItemsBySize.add(bikes)
                }

            }
            items = filteredItemsBySize.toList()
            bikesAdapter.setBikes(items)
        } else {
//            if the size filter is not selected then our result will be only range filter
            items = filteredItems.toList()
            bikesAdapter.setBikes(items)

        }
    }

    private fun clearFilter() {
        initConst()
        binding.slider.setValues(minPrice, maxPrice)
        binding.radioGroupSize.clearCheck()
        items = initialItems.toList()
        bikesAdapter.setBikes(items)
    }


    private fun ascending() {
        val sortedList = items.sortedBy {
            it.price
        }

        bikesAdapter.setBikes(sortedList)

    }

    private fun descending() {
        val sortedList = items.sortedByDescending {
            it.price
        }

        bikesAdapter.setBikes(sortedList)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}