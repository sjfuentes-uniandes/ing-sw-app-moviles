package com.example.vinilos.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAddTrackBinding
import com.example.vinilos.viemodels.AddTrackViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment para agregar un nuevo track a un álbum
 * HU008 - Como coleccionista, quiero agregar tracks a un álbum para actualizar mi catálogo
 */
class AddTrackFragment : Fragment() {

    private var _binding: FragmentAddTrackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddTrackViewModel by viewModels()

    private var albumId: Int = -1
    private var albumName: String = ""
    private var albumCover: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener argumentos
        albumId = arguments?.getInt("albumId") ?: -1
        albumName = arguments?.getString("albumName") ?: ""
        albumCover = arguments?.getString("albumCover") ?: ""

        if (albumId == -1) {
            Toast.makeText(context, "Error: ID de álbum inválido", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupUI() {
        // Mostrar nombre del álbum
        binding.albumNameText.text = albumName

        // Cargar imagen del álbum con Glide
        Glide.with(this)
            .load(albumCover)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.albumCoverImage)

        // Configurar toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        // Observar el estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.saveSongButton.isEnabled = !isLoading
        }

        // Observar el resultado de agregar track
        viewModel.trackAddedResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it.isSuccess) {
                    // Mostrar confirmación visual
                    Snackbar.make(
                        binding.root,
                        getString(R.string.track_added_successfully),
                        Snackbar.LENGTH_LONG
                    ).show()

                    // Volver a la pantalla de detalle del álbum después de 1 segundo
                    binding.root.postDelayed({
                        findNavController().navigateUp()
                    }, 1000)
                } else {
                    val errorMessage = it.exceptionOrNull()?.message
                        ?: getString(R.string.error_adding_track)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.saveSongButton.setOnClickListener {
            if (validateForm()) {
                saveTrack()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validar nombre del track
        val trackName = binding.trackNameEditText.text.toString().trim()
        if (trackName.isEmpty()) {
            binding.trackNameInputLayout.error = getString(R.string.error_track_name_required)
            isValid = false
        } else {
            binding.trackNameInputLayout.error = null
        }

        // Validar duración
        val trackDuration = binding.trackDurationEditText.text.toString().trim()
        if (trackDuration.isEmpty()) {
            binding.trackDurationInputLayout.error = getString(R.string.error_track_duration_required)
            isValid = false
        } else if (!isValidDurationFormat(trackDuration)) {
            binding.trackDurationInputLayout.error = getString(R.string.error_invalid_duration_format)
            isValid = false
        } else {
            binding.trackDurationInputLayout.error = null
        }

        return isValid
    }

    /**
     * Valida que el formato de duración sea M:SS o MM:SS
     */
    private fun isValidDurationFormat(duration: String): Boolean {
        val regex = Regex("^\\d{1,2}:[0-5]\\d$")
        return regex.matches(duration)
    }

    private fun saveTrack() {
        val trackName = binding.trackNameEditText.text.toString().trim()
        val trackDuration = binding.trackDurationEditText.text.toString().trim()

        viewModel.addTrackToAlbum(
            albumId = albumId,
            trackName = trackName,
            trackDuration = trackDuration,
            context = requireContext().applicationContext
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

