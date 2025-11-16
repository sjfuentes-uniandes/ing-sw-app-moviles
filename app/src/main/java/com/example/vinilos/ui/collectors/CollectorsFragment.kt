package com.example.vinilos.ui.collectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentCollectorsBinding
import com.example.vinilos.models.Collector
import com.example.vinilos.ui.adapters.CollectorsAdapter
import com.example.vinilos.viemodels.CollectorViewModel

class CollectorsFragment: Fragment() {
    private var _binding: FragmentCollectorsBinding?= null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorViewModel
    private var viewModelAdapter: CollectorsAdapter?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectorsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorsRv
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = viewModelAdapter
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshCollectors()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.collectors)
        viewModel = ViewModelProvider(this, CollectorViewModel.Factory(activity.application)).get(CollectorViewModel::class.java)
        viewModel.collectors.observe(viewLifecycleOwner, Observer<List<Collector>> {
            it.apply {
                viewModelAdapter!!.collectors = this
                binding.swipeRefresh.isRefreshing = false
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean>{ isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshCollectors()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError(){
        if(!viewModel.isNetworkErrorShown.value!!){
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}