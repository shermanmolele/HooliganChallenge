package com.example.hooliganchallenge.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hooliganchallenge.databinding.FragmentScheduleBinding
import com.example.hooliganchallenge.models.Events
import com.example.hooliganchallenge.models.Schedule
import com.example.hooliganchallenge.ui.home.EventsAdapter
import com.example.hooliganchallenge.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val viewModel: ScheduleViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSchedule()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       observe()
    }

    private fun observe() {
        viewModel.getSchedule.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data.let { scheduleResponse ->
                        if (scheduleResponse != null) {
                            Log.d("response", "response succesful $scheduleResponse")
                            setUpRecyclerView(scheduleResponse.toList())
                        }
                    }
                }
                is Resource.Error -> {
                    response.message.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun setUpRecyclerView(list: List<Schedule>) {

        scheduleAdapter = ScheduleAdapter(list.sortedBy {it.date})
        binding.scheduleRecyclerView.adapter = scheduleAdapter
        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.scheduleRecyclerView.addItemDecoration(dividerItemDecoration)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
