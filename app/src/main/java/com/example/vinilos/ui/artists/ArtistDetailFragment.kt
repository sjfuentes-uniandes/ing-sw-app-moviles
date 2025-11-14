package com.example.vinilos.ui.artists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentArtistDetailBinding
import com.example.vinilos.viemodels.ArtistDetailViewModel

class ArtistDetailFragment : Fragment() {

    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArtistDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ArtistDetailFragment", "onViewCreated - Arguments: $arguments")

        val artistId = arguments?.getInt("artistId") ?: run {
            Log.e("ArtistDetailFragment", "No se recibió artistId en arguments")
            Toast.makeText(context, "ID de artista inválido", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("ArtistDetailFragment", "ArtistId recibido: $artistId")

        // Inicializar ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(ArtistDetailViewModel::class.java)

        // Habilitar botón de regreso
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.loadArtistDetail(artistId)

        viewModel.artist.observe(viewLifecycleOwner) { artist ->
            Log.d("ArtistDetailFragment", "Artista recibido: ${artist.name}")
            binding.artistName.text = artist.name
            binding.artistDescription.text = artist.description
            binding.artistCreationDate.text = artist.creationDate

            // Cargar imagen con Glide
            Glide.with(this)
                .load(artist.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.artistImage)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Log.e("ArtistDetailFragment", "Error recibido: $error")
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }

        // Configurar botón de regreso
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
