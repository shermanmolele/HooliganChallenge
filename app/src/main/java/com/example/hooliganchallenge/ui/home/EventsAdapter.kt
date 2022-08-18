package com.example.hooliganchallenge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hooliganchallenge.databinding.EventListItemBinding
import com.example.hooliganchallenge.models.Events
import com.example.hooliganchallenge.utils.DateTimeUtils

class EventsAdapter(private val list: List<Events>, private val onClick: (Events) -> Unit) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: EventListItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(event: Events) {
            binding.title.text = event.title
            binding.subTitle.text = event.subtitle
            binding.date.text = DateTimeUtils.date2DayTime(event.date)
            val media = event.imageUrl
            Glide.with(itemView)
                .load(media)
                .into(binding.icon)
            binding.root.setOnClickListener {
                onClick(event)
            }
        }
    }
}
