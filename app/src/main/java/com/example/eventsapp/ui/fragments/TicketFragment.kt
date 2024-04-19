package com.example.eventsapp.ui.fragments

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
import com.example.eventsapp.adapters.EventsAdapter
import com.example.eventsapp.databinding.FragmentTicketBinding
import com.example.eventsapp.ui.EventsActivity
import com.example.eventsapp.viewmodel.EventsViewModel
import com.google.android.material.snackbar.Snackbar


class TicketFragment : Fragment(R.layout.fragment_ticket) {


    lateinit var eventsViewModel: EventsViewModel
    lateinit var eventsAdapter: EventsAdapter
    lateinit var binding:FragmentTicketBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTicketBinding.bind(view)
        eventsViewModel = (activity as EventsActivity).eventsViewModel
        setUpTicketRecycler()
        eventsAdapter.setOnItemClickListener {
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
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val event = eventsAdapter.differ.currentList[position]
                eventsViewModel.deleteEvent(event)
                Snackbar.make(view, "Removed from History", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        eventsViewModel.addToHistory(event)
                    }
                    show()
                }
            }


        }
        ItemTouchHelper(itemTouchHelperCallBack).apply{
            attachToRecyclerView(binding.recyclerHistory)
        }
        eventsViewModel.getTickets().observe(viewLifecycleOwner, Observer { attractions->

            eventsAdapter.differ.submitList(attractions)
        })

    }
    private fun setUpTicketRecycler(){
            eventsAdapter= EventsAdapter()
            binding.recyclerHistory.apply{
                adapter=eventsAdapter
                layoutManager= LinearLayoutManager(activity)
            }


    }

}




