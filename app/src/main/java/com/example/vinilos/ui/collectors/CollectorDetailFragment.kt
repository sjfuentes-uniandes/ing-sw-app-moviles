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
        viewModel.collector.observe(viewLifecycleOwner) { collector ->
            Log.d("CollectorDetailFragment", "Coleccionista recibido: ${collector.name}")

            binding.collectorName.text = collector.name
            binding.collectorTelephone.text = collector.telephone
            binding.collectorEmail.text = collector.email
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