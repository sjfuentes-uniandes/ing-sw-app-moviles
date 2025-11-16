package com.example.vinilos.ui.albums

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAlbumDetailBinding
import com.example.vinilos.viemodels.AlbumDetailViewModel

class AlbumDetailFragment : Fragment() {
    private val viewModel: AlbumDetailViewModel by viewModels()
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

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

        Log.d("AlbumDetailFragment", "AlbumId recibido: $albumId")

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.loadAlbumDetail(albumId)

        viewModel.album.observe(viewLifecycleOwner) { album ->
            Log.d("AlbumDetailFragment", "Álbum recibido: ${album.name}")
            binding.albumTitle.text = album.name
            binding.albumName.text = album.name
            binding.albumReleaseDate.text = album.releaseDate
            binding.albumDescription.text = album.description
            binding.albumGenre.text = album.genre

            Glide.with(this)
                .load(album.cover)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.albumCover)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.e("AlbumDetailFragment", "Error al cargar el álbum: $errorMessage")
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
