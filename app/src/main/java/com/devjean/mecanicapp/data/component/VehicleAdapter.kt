package com.devjean.mecanicapp.data.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devjean.mecanicapp.data.models.Vehicle
import com.devjean.mecanicapp.R

class VehicleAdapter(private val vehicles: List<Vehicle>) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val currentItem = vehicles[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = vehicles.size

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cellPhoneTextView: TextView = itemView.findViewById(R.id.cellPhoneTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val driverNameTextView: TextView = itemView.findViewById(R.id.driverNameTextView)
        private val licensePlateTextView: TextView = itemView.findViewById(R.id.licensePlateTextView)

        fun bind(vehicle: Vehicle) {
            cellPhoneTextView.text = vehicle.cellPhone
            descriptionTextView.text = vehicle.description
            driverNameTextView.text = vehicle.driverName
            licensePlateTextView.text = vehicle.licensePlate
            // Puedes agregar la lógica para cargar la imagen aquí si es necesario
        }
    }
}

