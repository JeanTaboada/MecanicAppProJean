package com.devjean.mecanicapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devjean.mecanicapp.ui.activities.BottomNavigationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnLogin: Button? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        auth = Firebase.auth

        btnLogin?.setOnClickListener {
            val emailUser = edtEmail?.text.toString().trim()
            val passwordUser = edtPassword?.text.toString().trim()

            if (emailUser.isEmpty() && passwordUser.isEmpty()) {
                Toast.makeText(this@MainActivity, "Ingresar los datos", Toast.LENGTH_SHORT).show()
            } else {
                validateUserFirebaseAuth(emailUser, passwordUser)
            }
        }
    }

    private fun validateUserFirebaseAuth(emailUser: String, passwordUser: String) {
        auth.signInWithEmailAndPassword(emailUser, passwordUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@MainActivity, BottomNavigationActivity::class.java))
                } else {
                    Toast.makeText(this@MainActivity, "Error, Verifique su correo y/o contraseña", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, "Error al iniciar sesión" + e, Toast.LENGTH_SHORT).show()
            }
    }


}