package com.example.hooliganchallenge.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hooliganchallenge.databinding.EventListItemBinding
import com.example.hooliganchallenge.databinding.ScheduleListItemBinding
import com.example.hooliganchallenge.models.Events
import com.example.hooliganchallenge.models.Schedule
import com.example.hooliganchallenge.utils.DateTimeUtils

class ScheduleAdapter(private val list : List<Schedule>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(list[position])
    }

}
class ViewHolder(private val binding:ScheduleListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(schedule: Schedule) {
        binding.title.text = schedule.title
        binding.subTitle.text = schedule.title

        binding.date.text = DateTimeUtils.date2DayTime(schedule.date)
        val media = schedule.imageUrl
        Glide.with(itemView)
            .load(media)
            .into(binding.sheduleIcon)
    }
}
