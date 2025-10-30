package com.example.vinilos.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentArtistsBinding
import com.example.vinilos.models.Artist
import com.example.vinilos.ui.adapters.ArtistsAdapter
import com.example.vinilos.viemodels.ArtistsViewModel

class ArtistsFragment : Fragment() {
    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var artistsViewModel: ArtistsViewModel
    private var artistsAdapter: ArtistsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        val view = binding.root
        artistsAdapter = ArtistsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.artistsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = artistsAdapter

        binding.swipeRefresh.setOnRefreshListener {
            artistsViewModel.refreshArtists()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.artists)
        artistsViewModel = ViewModelProvider(this, ArtistsViewModel.Factory(activity.application)).get(ArtistsViewModel::class.java)
        artistsViewModel.artist.observe(viewLifecycleOwner, Observer<List<Artist>> {
            it.apply {
                artistsAdapter!!.artists = this
                binding.swipeRefresh.isRefreshing = false
            }
        })
        artistsViewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onResume() {
        super.onResume()
        artistsViewModel.refreshArtists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!artistsViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            artistsViewModel.onNetworkErrorShown()
        }
    }
}