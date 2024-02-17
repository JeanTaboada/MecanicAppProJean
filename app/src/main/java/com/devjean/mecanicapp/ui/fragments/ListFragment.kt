package com.devjean.mecanicapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjean.mecanicapp.R
import com.devjean.mecanicapp.data.component.VehicleAdapter
import com.devjean.mecanicapp.data.models.Vehicle
import com.google.firebase.firestore.FirebaseFirestore

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VehicleAdapter
    private val vehiclesList = ArrayList<Vehicle>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Configurar RecyclerView y adaptador
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = VehicleAdapter(vehiclesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Obtener datos de Firestore y actualizar el adaptador
        fetchVehicleData()

        return view
    }

    private fun fetchVehicleData() {
        val vehiclesCollectionRef = firestore.collection("Registered Vehicles")
        vehiclesCollectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val vehicle = document.toObject(Vehicle::class.java)
                    vehiclesList.add(vehicle)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}
