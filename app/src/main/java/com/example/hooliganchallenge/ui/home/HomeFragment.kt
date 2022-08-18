package com.example.hooliganchallenge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hooliganchallenge.R
import com.example.hooliganchallenge.databinding.FragmentHomeBinding
import com.example.hooliganchallenge.models.Events
import com.example.hooliganchallenge.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvAdapter: EventsAdapter
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var eventResult: Events

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getEvents()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        viewModel.getEvents.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data.let { eventsResponse ->
                        if (eventsResponse != null) {
                           setUpRecyclerView(eventsResponse.toList())
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

    private fun setUpRecyclerView(list: List<Events>) {
        rvAdapter = EventsAdapter(list.sortedBy { it.date }){
            playBlack(it)
        }
        binding.eventsRecyclerView.adapter = rvAdapter
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.eventsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun playBlack(events: Events) {
            val bundle = Bundle().apply {
                putSerializable("event", events)
            }
            findNavController().navigate(R.id.action_navigation_home_to_videoPlayerFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
