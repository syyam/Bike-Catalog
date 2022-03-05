package com.example.bikecatalog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bikecatalog.databinding.ItemBikesBinding
import com.example.bikecatalog.model.Bikes
import com.example.bikecatalog.ui.BikesFragmentDirections
import javax.inject.Inject

class BikesAdapter @Inject constructor() : RecyclerView.Adapter<MainViewHolder>(), Filterable {

    var bikesList = mutableListOf<Bikes>()
    var bikesListFiltered = mutableListOf<Bikes>()

    fun setBikes(bikes: List<Bikes>) {
        this.bikesList = bikes.toMutableList()
        this.bikesListFiltered = bikes.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBikesBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val bike = bikesListFiltered[position]

        holder.bind(createOnClickListener(bike, holder), bike)
    }

    private fun createOnClickListener(
        bike: Bikes,
        holder: MainViewHolder
    ): View.OnClickListener {
        return View.OnClickListener {
            val directions = BikesFragmentDirections.actionBikesFragmentToDetailsFragment(bike)
            val extras = FragmentNavigatorExtras(
                holder.binding.avatar to "avatar_${bike.id}"
            )
            it.findNavController().navigate(directions, extras)
        }
    }

    override fun getItemCount(): Int {
        return bikesListFiltered.size
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    bikesListFiltered = bikesList
                } else {
                    val filteredList: MutableList<Bikes> = ArrayList()
                    for (row in bikesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.bikeName.lowercase()
                                .contains(charString.lowercase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    bikesListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = bikesListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                bikesListFiltered = filterResults.values as MutableList<Bikes>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }

}

class MainViewHolder(val binding: ItemBikesBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: View.OnClickListener, bike: Bikes) {

        binding.apply {

            Glide.with(itemView)
                .load(bike.picture)
                .centerCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(avatar)

            price.text = "€" + bike.price.toString()
            name.text = bike.bikeName
            category.text = bike.category
            ViewCompat.setTransitionName(this.avatar, "avatar_${bike.id}")

            root.setOnClickListener(listener)
        }

    }

}