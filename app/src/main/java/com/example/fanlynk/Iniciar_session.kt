package com.example.fanlynk

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Iniciar_session : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
        val btningresar: Button = findViewById(R.id.botonIniciarSesion)
        val txtemail: TextView = findViewById(R.id.CorreoInicioSesion)
        val txtpass: TextView = findViewById(R.id.contreaseñaInicioSesion)
        btningresar.setOnClickListener() {

        }
    }

    /*private fun singIn(email: String, password: String) {

        firebaseAuth.singInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, user?.uid.toString(), Toast.LENGTH_SHORT.show())
                } else {
                    Toast.makeText(
                        baseContext,
                        "Error de Email y/o contraseña",
                        Toast.LENGTH_SHORT.show()
                    )


                }
            }
    }

     */
}




