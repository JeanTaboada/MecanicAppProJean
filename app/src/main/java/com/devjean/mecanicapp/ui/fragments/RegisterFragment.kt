package com.devjean.mecanicapp.ui.fragments

import android.app.Activity
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.devjean.mecanicapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.util.UUID
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var imageFile: File? = null
    private var imagePhoto: ImageView? = null
    private lateinit var centeredImage: ImageView
    private lateinit var subtitleImage: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val textLicensePlate = view.findViewById<EditText>(R.id.textLicensePlate)
        val textDriverName = view.findViewById<EditText>(R.id.textDriverName)
        val textDescription = view.findViewById<EditText>(R.id.textDescription)
        val btnAddVehicle = view.findViewById<Button>(R.id.btnAddVehicle)
        imagePhoto = view.findViewById(R.id.imagePhoto)
        centeredImage = view.findViewById(R.id.centeredImage)
        subtitleImage = view.findViewById(R.id.subtitleImage)
        imagePhoto?.setOnClickListener { selectImage() }
        centeredImage.visibility = View.VISIBLE
        subtitleImage.visibility = View.VISIBLE
//        btnAddVehicle.setOnClickListener {
//            val currentUser = auth.currentUser
//            currentUser?.let { user ->
//                val licensePlate = textLicensePlate.text.toString()
//                val driverName = textDriverName.text.toString()
//                val description = textDescription.text.toString()
//
//                // Generar un registerID único usando randomUUID
//                val registerID = UUID.randomUUID().toString().substring(0, 20)
//
//                // Obtener el UID del usuario autenticado
//                val uid = user.uid
//
//                // Crear un nuevo mapa con los datos del vehículo
//                val vehicleData = hashMapOf(
//                    "registerID" to registerID,
//                    "licensePlate" to licensePlate,
//                    "driverName" to driverName,
//                    "description" to description,
//                    "userUID" to uid
//                )
//
//                // Obtener una referencia al documento del usuario en la colección "Registered Vehicles"
//                val userDocRef = firestore.collection("Registered Vehicles").document(uid)
//
//                // Obtener la lista actual de vehículos del usuario (o crear una lista vacía si no existe)
//                userDocRef.get().addOnSuccessListener { documentSnapshot ->
//                    val existingData = documentSnapshot.data
//
//                    if (existingData != null) {
//                        val existingVehiclesList = existingData["vehicles"] as ArrayList<Map<String, Any>>?
//                        if (existingVehiclesList != null) {
//                            existingVehiclesList.add(vehicleData)
//                        } else {
//                            val vehiclesList = arrayListOf(vehicleData)
//                            existingData["vehicles"] = vehiclesList
//                        }
//                        userDocRef.set(existingData)
//                    } else {
//                        val newData = hashMapOf(
//                            "vehicles" to arrayListOf(vehicleData)
//                        )
//                        userDocRef.set(newData)
//                    }.addOnSuccessListener {
//                        Toast.makeText(context, "Vehículo registrado", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener { e ->
//                        Toast.makeText(context, "Error al registrar el vehículo: $e", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

        btnAddVehicle.setOnClickListener {
            val licensePlate = textLicensePlate.text.toString().trim()
            val driverName = textDriverName.text.toString().trim()
            val description = textDescription.text.toString().trim()

            if (licensePlate.isEmpty() || driverName.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar si el nombre del conductor contiene solo letras
            if (!driverName.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$".toRegex())) {
                Toast.makeText(context, "El nombre del conductor solo puede contener letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Continuar con el proceso de registro si la validación es exitosa
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val registerID = UUID.randomUUID().toString().substring(0, 20)
                val uid = user.uid

                val vehicleData = hashMapOf(
                    "registerID" to registerID,
                    "licensePlate" to licensePlate,
                    "driverName" to driverName,
                    "description" to description,
                    "userUID" to uid
                )

                // Guardar la imagen en Firebase Storage
                if (imageFile != null) {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("vehicle_images/${registerID}.jpg")

                    val uploadTask = imageRef.putFile(Uri.fromFile(imageFile))

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Obtener la URL de la imagen
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Agregar la URL de la imagen a los datos del vehículo
                            vehicleData["imageUrl"] = imageUrl

                            // Guardar los datos del vehículo en Firestore
                            val vehicleDocRef = firestore.collection("Registered Vehicles").document(registerID)
                            vehicleDocRef.set(vehicleData)
                                .addOnSuccessListener {
                                    // Limpiar los campos de texto después de un registro exitoso
                                    textLicensePlate.setText("")
                                    textDriverName.setText("")
                                    textDescription.setText("")
                                    // Restaurar el estado inicial de la imagen
                                    //NO LIMPIA LA IMAGEN DPS DE LA RESPUESTA EXITOSA
                                    imagePhoto = null

                                    //OK
                                    centeredImage.visibility = View.VISIBLE
                                    subtitleImage.visibility = View.VISIBLE
                                    //Mensaje de exito
                                    Toast.makeText(context, "Vehículo registrado", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error al registrar el vehículo: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al subir la imagen: $e", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Si no se seleccionó ninguna imagen, guardar los datos del vehículo sin URL de imagen
                    val vehicleDocRef = firestore.collection("Registered Vehicles").document(registerID)
                    vehicleDocRef.set(vehicleData)
                        .addOnSuccessListener {
                            // Limpiar los campos de texto después de un registro exitoso
                            textLicensePlate.setText("")
                            textDriverName.setText("")
                            textDescription.setText("")

                            Toast.makeText(context, "Vehículo registrado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al registrar el vehículo: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        return view
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            dataResult(result)
        }

    private fun dataResult(result: ActivityResult) {
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                imageFile = fileUri?.path?.let {
                    File(it)
                }
                imagePhoto?.setImageURI(fileUri)
                centeredImage.visibility = View.GONE
                subtitleImage.visibility = View.GONE
            }

            ImagePicker.RESULT_ERROR -> {
                centeredImage.visibility = View.VISIBLE
                subtitleImage.visibility = View.VISIBLE
            }

            else -> {
                /**Si se cierra la vista*/
            }
        }
    }

    private fun selectImage() {
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1080, 1080).createIntent { intent -> startImageForResult.launch(intent) }
    }
}
