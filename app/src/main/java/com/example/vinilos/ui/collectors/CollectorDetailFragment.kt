package com.example.vinilos.ui.collectors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentCollectorDetailBinding
import com.example.vinilos.viemodels.CollectorDetailViewModel
import com.bumptech.glide.Glide
import android.widget.TextView

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val viewModel: CollectorDetailViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("CollectorDetailFragment", "onViewCreated - Arguments: $arguments")
        
        val collectorId = arguments?.getInt("collectorId") ?: run {
            Log.e("CollectorDetailFragment", "No se recibió collectorId en arguments")
            Toast.makeText(context, "ID de coleccionista inválido", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("CollectorDetailFragment", "CollectorId recibido: $collectorId")

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.loadCollectorDetail(collectorId)
        viewModel.loadCollectorAlbumNames(collectorId)
        viewModel.collector.observe(viewLifecycleOwner) { collector ->
            Log.d("CollectorDetailFragment", "Coleccionista recibido: ${collector.name}")

            binding.collectorTitle.text = collector.name + "'s Details"
            binding.collectorName.text = collector.name
            binding.collectorTelephone.text = collector.telephone
            binding.collectorEmail.text = collector.email
        }

        viewModel.collectorAlbumNames.observe(viewLifecycleOwner) { albumNames ->
            binding.albumsContainer.removeAllViews()

            if (albumNames.isEmpty()) {
                val emptyText = TextView(requireContext())
                emptyText.text = "Este coleccionista no tiene álbumes registrados"
                emptyText.textSize = 12f
                binding.albumsContainer.addView(emptyText)
                return@observe
            }

            albumNames.forEach { name ->
                val tv = TextView(requireContext())
                tv.text = "• $name"
                tv.textSize = 12f
                tv.setPadding(0, 4, 0, 4)
                binding.albumsContainer.addView(tv)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMesssage ->
            Log.e("CollectorDetailFragment", "Error al cargar detalles del coleccionista: $errorMesssage")
            Toast.makeText(context, errorMesssage, Toast.LENGTH_SHORT).show()
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