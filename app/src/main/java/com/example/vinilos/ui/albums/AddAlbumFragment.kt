package com.example.vinilos.ui.albums
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.databinding.FragmentAddAlbumsBinding
import com.example.vinilos.models.Artist
import com.example.vinilos.ui.adapters.Track
import com.example.vinilos.ui.adapters.TrackAdapter
import com.example.vinilos.viemodels.AlbumViewModel
import com.example.vinilos.viemodels.ArtistsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale


class AddAlbumFragment: Fragment() {
    private var _binding: FragmentAddAlbumsBinding? = null
    internal val binding get() = _binding!!
    internal lateinit var trackAdapter: TrackAdapter
    internal val trackList = mutableListOf<Track>()
    internal lateinit var albumViewModel: AlbumViewModel
    internal lateinit var artistsViewModel: ArtistsViewModel
    internal var selectedImageUri: Uri? = null
    internal var selectedArtist: Artist? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.albumImagePreview.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupClickListeners()
        setupRecyclerView()

        val activity = requireActivity()
        albumViewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application))[AlbumViewModel::class.java]
        artistsViewModel = ViewModelProvider(this, ArtistsViewModel.Factory(activity.application))[ArtistsViewModel::class.java]

        artistsViewModel.artist.observe(viewLifecycleOwner, Observer<List<Artist>> { artists ->
            setupSpinners(artists)
        })

        albumViewModel.createAlbumResult.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                if (it.isSuccess) {
                    Toast.makeText(
                        requireContext(),
                        "Álbum se creó exitosamente",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error creating album: ${it.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

        artistsViewModel.refreshArtists()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    internal fun setupSpinners(artists: List<Artist>) {
        val artistNames = artists.map { it.name }
        val tracksForDropdown = arrayOf("Demo Track A", "Demo Track B", "Demo Track C")

        val artistAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, artistNames)
        binding.artistAutoComplete.setAdapter(artistAdapter)

        val trackDropdownAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tracksForDropdown)
        binding.trackAutoComplete.setAdapter(trackDropdownAdapter)

        binding.artistAutoComplete.setOnItemClickListener { _, _, position, _ ->
            selectedArtist = artists[position]
        }

        binding.trackAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedTrackName = parent.getItemAtPosition(position).toString()
            addTrackToList(selectedTrackName)
            binding.trackAutoComplete.text.clear()
            binding.trackAutoComplete.clearFocus()
        }

        binding.trackAutoComplete.setOnEditorActionListener { _, _, _ ->
            val trackName = binding.trackAutoComplete.text.toString().trim()
            if (trackName.isNotEmpty()) {
                addTrackToList(trackName)
                binding.trackAutoComplete.text.clear()
                binding.trackAutoComplete.clearFocus()
            }
            true
        }
    }

    private fun setupClickListeners() {
        binding.releaseDateEditText.setOnClickListener {
            showDatePicker()
        }

        binding.browseImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.createAlbumButton.setOnClickListener {
            if (validateForm()) {
                createAlbum()
            }
        }
    }

    private fun setupRecyclerView() {
        trackAdapter = TrackAdapter { trackToRemove ->
            trackList.remove(trackToRemove)
            trackAdapter.submitList(trackList.toList())
        }
        binding.tracksRecyclerView.adapter = trackAdapter
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    internal fun addTrackToList(trackName: String) {
        val newTrack = Track(id = System.currentTimeMillis().toString(), name = trackName)
        trackList.add(newTrack)
        trackAdapter.submitList(trackList.toList())
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select release date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.releaseDateEditText.setText(displayFormat.format(selection))
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    internal fun formatDateForAPI(dateString: String): String {
        return try {
            val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = displayFormat.parse(dateString)
            date?.let {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val datePart = dateFormat.format(it)
                "${datePart}T00:00:00-05:00"
            } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    internal fun validateForm(): Boolean {
        var isValid = true

        if (binding.nameEditText.text.isNullOrEmpty()) {
            binding.nameEditText.error = "El Nombre es requerido"
            isValid = false
        } else {
            binding.nameEditText.error = null
        }

        if (binding.artistAutoComplete.text.isNullOrEmpty() || selectedArtist == null) {
            binding.artistAutoComplete.error = "El Artista es requerido"
            isValid = false
        } else {
            binding.artistAutoComplete.error = null
        }

        if (binding.descriptionEditText.text.isNullOrEmpty()) {
            binding.descriptionEditText.error = "La descripción es requerida"
            isValid = false
        } else {
            binding.descriptionEditText.error = null
        }

        if (binding.genreEditText.text.isNullOrEmpty()) {
            binding.genreEditText.error = "Genero es requerido"
            isValid = false
        } else {
            binding.genreEditText.error = null
        }

        if (binding.recordLabelEditText.text.isNullOrEmpty()) {
            binding.recordLabelEditText.error = "Discografia es requerida"
            isValid = false
        } else {
            binding.recordLabelEditText.error = null
        }

        if (binding.releaseDateEditText.text.isNullOrEmpty()) {
            binding.releaseDateEditText.error = "Fecha de lanzamiento es requerida"
            isValid = false
        } else {
            binding.releaseDateEditText.error = null
        }

        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Por favor seleccione una imagen", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (trackList.isEmpty()) {
            Toast.makeText(requireContext(), "Añade al menos una pista.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    internal fun createAlbum() {
        val name = binding.nameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val genre = binding.genreEditText.text.toString().trim()
        val recordLabel = binding.recordLabelEditText.text.toString().trim()
        val releaseDateString = binding.releaseDateEditText.text.toString().trim()

        val releaseDate = formatDateForAPI(releaseDateString)

        val cover = "https://via.placeholder.com/300"


        val tracks = trackList.map { track ->
            mapOf("name" to track.name)
        }

        albumViewModel.createAlbum(
            name = name,
            cover = cover,
            releaseDate = releaseDate,
            description = description,
            genre = genre,
            recordLabel = recordLabel,
            tracks = tracks
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}