package com.example.hooliganchallenge.ui.videoPlayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hooliganchallenge.databinding.FragmentVideoPlayerBinding
import com.example.hooliganchallenge.models.Events
import com.example.hooliganchallenge.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPlayerFragment : Fragment() {
    private var _binding: FragmentVideoPlayerBinding? = null
    private lateinit var eventResult: Events
    private val viewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getEvents()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventResult = arguments?.getSerializable("event") as Events
        viewModel.getEvent(eventResult.id)

        binding.videoView1.setVideoPath(eventResult.videoUrl)
        val mediaController = MediaController(requireContext())
        mediaController?.setAnchorView(binding.videoView1)
       binding.videoView1.setMediaController(mediaController)
       binding.videoView1.setOnPreparedListener { mp ->
            mp.isLooping = true
        }
        binding.videoView1.start()
    }
}
