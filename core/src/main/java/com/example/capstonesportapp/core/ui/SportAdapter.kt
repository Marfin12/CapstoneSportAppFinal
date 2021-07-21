package com.example.capstonesportapp.core.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.capstonesportapp.core.R
import com.example.capstonesportapp.core.databinding.ItemEventSportBinding
import com.example.capstonesportapp.core.domain.model.Sport
import java.util.*

open class SportAdapter : RecyclerView.Adapter<SportAdapter.ListViewHolder>() {

    private var listData = ArrayList<Sport>()
    var onItemClick: ((Sport, Int) -> Unit)? = null

    companion object {
        @Volatile
        var defaultSportIdx: Int = 0
    }

    fun setData(newListData: List<Sport>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        onRefreshAdapter()
    }

    open fun onRefreshAdapter() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_event_sport, parent, false)
        )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data, position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemEventSportBinding = ItemEventSportBinding.bind(itemView)
        fun bind(data: Sport, position: Int) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.image)
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                        ) {
                            imgSport.background = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            imgSport.setBackgroundResource(R.drawable.sport_bg)
                            super.onLoadFailed(errorDrawable)
                        }
                    })

                viewOverlay.visibility = if (position == defaultSportIdx) View.GONE
                else View.VISIBLE

                txtSportName.text = data.name
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition], adapterPosition)
            }
        }
    }
}