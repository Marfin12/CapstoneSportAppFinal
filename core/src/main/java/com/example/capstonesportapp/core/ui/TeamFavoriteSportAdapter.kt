package com.example.capstonesportapp.core.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.capstonesportapp.core.R
import com.example.capstonesportapp.core.databinding.ItemFavoriteBinding
import com.example.capstonesportapp.core.domain.model.Team

class TeamFavoriteSportAdapter : RecyclerView.Adapter<TeamFavoriteSportAdapter.ListViewHolder>() {

    private var listData = ArrayList<Team>()
    var onItemClick: ((Team) -> Unit)? = null
    var onFavClick: ((Team) -> Unit)? = null

    fun setData(newListData: List<Team>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemFavoriteBinding = ItemFavoriteBinding.bind(itemView)
        fun bind(data: Team) {
            with(binding) {
                with(incSportImage) {
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
                }

                with(incSportContent) {
                    txtSportName.text = data.name
                    txtFavoriteSportContent.text = data.description
                }

                with(incSportRightBar) {
                    imgFavorite.setOnClickListener {
                        onFavClick?.invoke(data)
                        notifyDataSetChanged()
                    }
                    imgShare.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_SUBJECT, data.name)
                            .putExtra(Intent.EXTRA_TEXT, data.description)

                        itemView.context.startActivity(intent)
                    }
                }
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}