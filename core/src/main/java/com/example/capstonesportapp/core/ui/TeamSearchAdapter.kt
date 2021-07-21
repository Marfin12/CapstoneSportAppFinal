package com.example.capstonesportapp.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonesportapp.core.R
import com.example.capstonesportapp.core.databinding.ItemAutoCompleteBinding

class TeamSearchAdapter : RecyclerView.Adapter<TeamSearchAdapter.ListViewHolder>() {

    private var listData = ArrayList<String>()
    var onItemClick: ((String) -> Unit)? = null

    fun setData(newListData: ArrayList<String>) {
        if (newListData.isEmpty() || newListData.equals("")) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_auto_complete, parent, false)
        )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemAutoCompleteBinding = ItemAutoCompleteBinding.bind(itemView)

        fun bind(data: String) {
            with(binding) {
                txtTeamSearch.text = data
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}