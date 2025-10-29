package com.example.vinilos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vinilos.MainActivity
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnAlbums.setOnClickListener {
            (activity as MainActivity).binding.navView.selectedItemId = R.id.navigation_albums
        }
        
        binding.btnCollectors.setOnClickListener {
            (activity as MainActivity).binding.navView.selectedItemId = R.id.navigation_collector
        }

        binding.btnArtist.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_artist)
            (activity as MainActivity).binding.navView.selectedItemId = R.id.navigation_artists_list
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}