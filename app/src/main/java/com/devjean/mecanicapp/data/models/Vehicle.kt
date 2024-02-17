package com.devjean.mecanicapp.data.models

import com.google.firebase.firestore.PropertyName

data class Vehicle(
    var cellPhone: String = "",
    var description: String = "",
    var driverName: String = "",
    var imageUrl: String = "",
    var licensePlate: String = "",
    var registerID: String = "",
    var userID: String = ""
) {
    // Constructor vacío necesario para la deserialización de Firestore
    constructor() : this("", "", "", "", "", "", "")

}
