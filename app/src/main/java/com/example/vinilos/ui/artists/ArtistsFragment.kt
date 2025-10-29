package com.example.vinilos.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.databinding.FragmentArtistsBinding
import com.example.vinilos.viemodels.ArtistsViewModel

class ArtistsFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArtistsViewModel
    private lateinit var artistsAdapter: ArtistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Obtén el ViewModel
        // (Asumiendo que creas ArtistsViewModel en la carpeta /viewmodels)
        viewModel = ViewModelProvider(this).get(ArtistsViewModel::class.java)

        // 2. Prepara el Adapter con una lista vacía
        artistsAdapter = ArtistsAdapter(emptyList())

        // 3. Conecta el RecyclerView del XML con el Adapter
        binding.artistsRecyclerView.adapter = artistsAdapter
        // (El layoutManager ya lo pusimos en el XML, no es necesario aquí)

        // 4. Observa los datos del ViewModel
        viewModel.artists.observe(viewLifecycleOwner) { artistList ->
            // Cuando los datos lleguen, actualiza el adapter
            artistsAdapter.updateData(artistList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpia la referencia al binding
    }
}