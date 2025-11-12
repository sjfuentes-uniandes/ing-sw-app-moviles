package com.example.vinilos.ui.albums
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.databinding.FragmentAddAlbumsBinding
import com.example.vinilos.ui.adapters.Track
import com.example.vinilos.ui.adapters.TrackAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale


class AddAlbumFragment: Fragment() {
    // ViewBinding para acceder a las vistas de forma segura
    private var _binding: FragmentAddAlbumsBinding? = null
    private val binding get() = _binding!!

    // Adapter para la lista de tracks
    private lateinit var trackAdapter: TrackAdapter
    private val trackList = mutableListOf<Track>()

    // ActivityResultLauncher para el selector de imágenes
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Cuando el usuario selecciona una imagen, se actualiza el ImageView
            uri?.let {
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

        // Llamamos a todas nuestras funciones de configuración
        setupToolbar()
        setupSpinners()
        setupClickListeners()
        setupRecyclerView()
    }

    /**
     * Configura el Toolbar.
     * El título y el ícono se definen en el XML, aquí solo manejamos el clic.
     */
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            // Navegar hacia atrás
            findNavController().popBackStack()
        }
    }

    /**
     * Configura los menús desplegables (Spinners).
     */
    private fun setupSpinners() {
        // Datos de ejemplo para los menús
        val artists = arrayOf("Artist 1", "Artist 2", "Artist 3", "Other")
        val tracksForDropdown = arrayOf("Demo Track A", "Demo Track B", "Demo Track C")

        // Adapter para el menú de Artistas
        val artistAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, artists)
        binding.artistAutoComplete.setAdapter(artistAdapter)

        // Adapter para el menú de Tracks
        val trackDropdownAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tracksForDropdown)
        binding.trackAutoComplete.setAdapter(trackDropdownAdapter)

        // Listener para cuando se selecciona un track del menú
        binding.trackAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedTrackName = parent.getItemAtPosition(position).toString()
            addTrackToList(selectedTrackName)
            binding.trackAutoComplete.text.clear() // Limpiar el campo después de añadir
            binding.trackAutoComplete.clearFocus()
        }
    }

    /**
     * Configura los listeners para los botones y campos interactivos.
     */
    private fun setupClickListeners() {
        // Listener para el campo de fecha
        binding.releaseDateEditText.setOnClickListener {
            showDatePicker()
        }

        // Listener para el botón de buscar imagen
        binding.browseImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*") // Abre el selector de archivos de imagen
        }

        // Listener para el botón de crear álbum
        binding.createAlbumButton.setOnClickListener {
            if (validateForm()) {
                createAlbum()
            }
        }
    }

    /**
     * Configura el RecyclerView para mostrar la lista de tracks.
     */
    private fun setupRecyclerView() {
        // Inicializa el adapter con la lambda para el botón de borrar
        trackAdapter = TrackAdapter { trackToRemove ->
            trackList.remove(trackToRemove)
            trackAdapter.submitList(trackList.toList()) // Actualiza la lista (enviando una nueva)
        }
        binding.tracksRecyclerView.adapter = trackAdapter
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * Añade un nuevo track a la lista interna y actualiza el adapter.
     */
    private fun addTrackToList(trackName: String) {
        // Usamos el timestamp como un ID único de ejemplo
        val newTrack = Track(id = System.currentTimeMillis().toString(), name = trackName)
        trackList.add(newTrack)
        trackAdapter.submitList(trackList.toList()) // Actualiza el RecyclerView
    }

    /**
     * Muestra el diálogo de selección de fecha (MaterialDatePicker).
     */
    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select release date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            // Formatea la fecha seleccionada (Ej: 25/12/2024)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.releaseDateEditText.setText(sdf.format(selection))
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    /**
     * Valida que los campos requeridos no estén vacíos.
     */
    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.nameEditText.text.isNullOrEmpty()) {
            binding.nameEditText.error = "Name is required"
            isValid = false
        }
        if (binding.artistAutoComplete.text.isNullOrEmpty()) {
            binding.artistAutoComplete.error = "Artist is required"
            isValid = false
        }
        // ... (puedes añadir más validaciones aquí) ...

        return isValid
    }

    /**
     * Lógica a ejecutar cuando se presiona "Create Album".
     */
    private fun createAlbum() {
        // Aquí recolectarías toda la información
   /*     val name = binding.nameEditText.text.toString()
        val artist = binding.artistAutoComplete.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val genre = binding.genreEditText.text.toString()
        val releaseDate = binding.releaseDateEditText.text.toString()
        // ... también necesitarías la URI de la imagen y la lista de tracks ...

        val tracksCount = trackList.size

        // Muestra un mensaje de confirmación
        Toast.makeText(
            requireContext(),
            "Album '$name' by $artist created with $tracksCount tracks!",
            Toast.LENGTH_LONG
        ).show()
*/
        // Opcional: navegar hacia atrás después de crear
        // findNavController().popBackStack()
    }

    /**
     * Limpia la referencia de binding cuando la vista se destruye.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ¡Importante para evitar memory leaks!
    }
}