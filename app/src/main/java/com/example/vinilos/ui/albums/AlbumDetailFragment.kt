package com.example.vinilos.ui.albums

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAlbumDetailBinding
import com.example.vinilos.ui.adapters.AlbumTracksAdapter
import com.example.vinilos.viemodels.AlbumDetailViewModel

class AlbumDetailFragment : Fragment() {
    private val viewModel: AlbumDetailViewModel by viewModels()
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var tracksAdapter: AlbumTracksAdapter
    private var currentAlbumId: Int = -1
    private var currentAlbumName: String = ""
    private var currentAlbumCover: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("AlbumDetailFragment", "onViewCreated - Arguments: $arguments")

        val albumId = arguments?.getInt("albumId") ?: run {
            Log.e("AlbumDetailFragment", "No se recibió albumId en arguments")
            Toast.makeText(context, "ID de álbum inválido", Toast.LENGTH_SHORT).show()
            return
        }

        currentAlbumId = albumId
        Log.d("AlbumDetailFragment", "AlbumId recibido: $albumId")

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        // Cargar detalles del álbum
        viewModel.loadAlbumDetail(albumId)
    }

    override fun onResume() {
        super.onResume()
        // Recargar los detalles al volver de agregar un track
        if (currentAlbumId != -1) {
            viewModel.loadAlbumDetail(currentAlbumId)
        }
    }

    private fun setupRecyclerView() {
        tracksAdapter = AlbumTracksAdapter()
        binding.tracksRecyclerView.apply {
            adapter = tracksAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fabAddTrack.setOnClickListener {
            navigateToAddTrack()
        }
    }

    private fun observeViewModel() {
        viewModel.album.observe(viewLifecycleOwner) { album ->
            Log.d("AlbumDetailFragment", "Álbum recibido: ${album.name} con ${album.tracks.size} tracks")

            currentAlbumName = album.name
            currentAlbumCover = album.cover

            binding.albumTitle.text = getString(R.string.album_name_title, album.name)
            binding.albumName.text = album.name
            binding.albumReleaseDate.text = album.releaseDate
            binding.albumDescription.text = album.description
            binding.albumGenre.text = album.genre

            Glide.with(this)
                .load(album.cover)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.albumCover)

            // Actualizar lista de tracks
            if (album.tracks.isEmpty()) {
                binding.emptyTracksMessage.visibility = View.VISIBLE
                binding.tracksRecyclerView.visibility = View.GONE
            } else {
                binding.emptyTracksMessage.visibility = View.GONE
                binding.tracksRecyclerView.visibility = View.VISIBLE
                tracksAdapter.updateTracks(album.tracks)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.e("AlbumDetailFragment", "Error al cargar el álbum: $errorMessage")
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToAddTrack() {
        val bundle = bundleOf(
            "albumId" to currentAlbumId,
            "albumName" to currentAlbumName,
            "albumCover" to currentAlbumCover
        )
        findNavController().navigate(
            R.id.action_albumDetailFragment_to_addTrackFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
