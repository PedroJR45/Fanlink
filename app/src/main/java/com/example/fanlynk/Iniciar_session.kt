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
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Iniciar_session : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
        val btningresar: Button = findViewById(R.id.botonIniciarSesion)
        val txtemail: TextView = findViewById(R.id.CorreoInicioSesion)
        val txtpass: TextView = findViewById(R.id.contreaseñaInicioSesion)
        val textCuenta: TextView = findViewById(R.id.textoInicioSesion)
        firebaseAuth= Firebase.auth
        btningresar.setOnClickListener() {
            singIn(txtemail.text.toString(),txtpass.text.toString())
        }
        textCuenta.setOnClickListener {
            val intent = Intent(this, registrarse::class.java)
            startActivity(intent)
        }
    }

    private fun singIn(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Inicio exitoso.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, vincular::class.java)
                    startActivity(intent)
                    finish() // Opcional: cierra la actividad actual si no deseas que el usuario vuelva atrás
                } else {
                    Toast.makeText(
                        baseContext,
                        "Error de Email y/o contraseña",
                        Toast.LENGTH_SHORT).show()

                }

            }
    }

}