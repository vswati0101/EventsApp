package com.example.eventsapp.ui.fragments
import EventsViewModel
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eventsapp.R
import com.example.eventsapp.adapters.WishlistAdapter
import com.example.eventsapp.databinding.FragmentTicketBinding
import com.example.eventsapp.ui.EventsActivity
import com.google.android.material.snackbar.Snackbar

class WishlistFragment : Fragment(R.layout.fragment_ticket) {

    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var binding: FragmentTicketBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTicketBinding.bind(view)
        eventsViewModel = (activity as EventsActivity).eventsViewModel
        setUpTicketRecycler()
        wishlistAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("event", it)
            }
            findNavController().navigate(R.id.action_ticketFragment_to_eventsFragment, bundle)
        }
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val event = wishlistAdapter.differ.currentList[position]
                eventsViewModel.removeFromHistory(event)
                Snackbar.make(view, "Removed from Wishlist", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        eventsViewModel.addToHistory(event)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.recyclerHistory)
        }
        eventsViewModel.getTickets().observe(viewLifecycleOwner, Observer { attractions ->
            if (attractions.isNotEmpty()) {
                binding.recyclerHistory.visibility = View.VISIBLE
                binding.textNoData.visibility = View.GONE
                wishlistAdapter.differ.submitList(attractions)
            } else {
                binding.recyclerHistory.visibility = View.GONE
                binding.textNoData.visibility = View.VISIBLE
            }
        })
    }

    private fun setUpTicketRecycler() {
        wishlistAdapter = WishlistAdapter()
        binding.recyclerHistory.apply {
            adapter = wishlistAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
