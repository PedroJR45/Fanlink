package com.example.fanlynk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Iniciar_session : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.botonIniciarSesion).setOnClickListener {
            val correo_electronico = findViewById<EditText>(R.id.CorreoInicioSesion).text.toString()
            val contraseña = findViewById<EditText>(R.id.contreaseñaInicioSesion).text.toString()

            val registroEncontrado = RegistroData.registros.find { it.correo == correo_electronico && it.contraseña == contraseña }

            if (registroEncontrado != null) {
                mostrarMensaje("Inicio de sesión exitoso")
                val intent = Intent(this, Manejar_dispositivo::class.java)
                startActivity(intent)
            } else {
                mostrarMensaje("Correo o contraseña incorrectos")
            }
        }

        findViewById<TextView>(R.id.textoInicioSesion).setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }
}