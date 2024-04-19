package com.example.eventsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventsapp.R
import com.example.eventsapp.adapters.EventsAdapter
import com.example.eventsapp.databinding.FragmentHomeBinding
import com.example.eventsapp.ui.EventsActivity
import com.example.eventsapp.util.Constants
import com.example.eventsapp.util.Resource
import com.example.eventsapp.viewmodel.EventsViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var eventsViewModel: EventsViewModel
    lateinit var eventsAdapter: EventsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemEventsError: CardView
    lateinit var binding: FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        itemEventsError = view.findViewById(R.id.itemEventsError)
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)
        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)
        eventsViewModel = (activity as EventsActivity).eventsViewModel
        setUpEventsRecycler()
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", null)
        // Assuming you have a TextField with id "textFieldUsername"
        binding.textView3.text = userName
//        eventsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("events", it)
//            }
//            findNavController().navigate(R.id.action_homeFragment_to_eventsFragment, bundle)
//        }
        eventsAdapter.setOnItemClickListener { event ->
            val action = HomeFragmentDirections.actionHomeFragmentToEventsFragment(event)
            findNavController().navigate(action)
        }
        eventsViewModel.event.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { eventResponse ->
                        eventsAdapter.differ.submitList(eventResponse._embedded.attractions.toList())
                        isLastPage = eventsViewModel.eventsPage == eventResponse.page.totalPages
                        if (isLastPage) {
                            binding.recyclerEvents.setPadding(0, 0, 0, 0)
                        }

                    }
                }

                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Sorry error:$message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })
        retryButton.setOnClickListener {
            eventsViewModel.getEvents("us")
        }
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemEventsError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemEventsError.visibility = View.VISIBLE
        errorText.text = message
        isError = true

    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                eventsViewModel.getEvents("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpEventsRecycler() {
        eventsAdapter = EventsAdapter()
        binding.recyclerEvents.apply {
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }

    }


}