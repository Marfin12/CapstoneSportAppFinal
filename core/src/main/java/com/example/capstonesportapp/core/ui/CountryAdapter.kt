package com.example.capstonesportapp.core.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonesportapp.core.R
import com.example.capstonesportapp.core.databinding.ItemCountryBinding
import com.example.capstonesportapp.core.domain.model.Country
import java.util.*

open class CountryAdapter : RecyclerView.Adapter<CountryAdapter.ListViewHolder>() {

    private var listData = ArrayList<Country>()
    var onItemClick: ((Int) -> Unit)? = null

    companion object {
        @Volatile
        var defaultCountryIdx: Int = 0
    }

    fun setData(newListData: List<Country>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        onRefreshAdapter()
    }

    open fun onRefreshAdapter() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
    )

    override fun getItemCount() = listData.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data, position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemCountryBinding = ItemCountryBinding.bind(itemView)
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(data: Country, position: Int) {
            with(binding) {
                val context = itemView.context

                if (position == defaultCountryIdx)
                    cardEventSportCard.setBackgroundColor(context.resources.getColor(R.color.material_on_surface_stroke, context.theme))
                else
                    cardEventSportCard.setBackgroundColor(context.resources.getColor(R.color.design_default_color_on_primary, context.theme))

                txtSportName.text = data.name_en
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
}