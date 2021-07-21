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
import com.example.capstonesportapp.core.databinding.ItemTeamVsTeamBinding
import com.example.capstonesportapp.core.domain.model.Team

class TeamSportAdapter : RecyclerView.Adapter<TeamSportAdapter.ListViewHolder>() {

    private var listData = ArrayList<Team>()
    var onItemClick: ((Team) -> Unit)? = null

    fun setData(newListData: List<Team>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_team_vs_team, parent, false)
        )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemTeamVsTeamBinding = ItemTeamVsTeamBinding.bind(itemView)
        fun bind(data: Team) {
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

                txtSportName.text = data.name
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}