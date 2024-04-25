package com.example.eventsapp.ui.fragments


import EventsViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventsapp.R
import com.example.eventsapp.adapters.EventsAdapter
import com.example.eventsapp.databinding.FragmentHomeBinding
import com.example.eventsapp.models.Attraction
import com.example.eventsapp.ui.EventsActivity
import com.example.eventsapp.util.Constants
import com.example.eventsapp.util.Resource


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var retryButton: Button
    private lateinit var errorText: TextView
    private lateinit var itemEventsError: CardView
    private lateinit var binding: FragmentHomeBinding
    private var isLoggedIn = false
    private lateinit var onBackPressedCallback: OnBackPressedCallback

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
        binding.textView3.text = userName

        eventsAdapter.setOnItemClickListener { event ->
            navigateToEventsFragment(event)
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

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // If the user is logged out, exit the app when the back button is pressed on the login screen
                if (!isLoggedIn) {
                    requireActivity().moveTaskToBack(true)
                    requireActivity().finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                } else {
                    // Otherwise, handle back button press as usual
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private var isError = false
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
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
    private fun navigateToEventsFragment(event: Attraction) {
        val action = HomeFragmentDirections.actionHomeFragmentToEventsFragment(event)
        findNavController().navigate(action)
    }
}
