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

object RegistroData {
    var correo: String? = null
    var usuario: String? = null
    var contraseña: String? = null
}

class registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//intento de bajar estos cambios
        findViewById<Button>(R.id.btnRegistrarse).setOnClickListener {
            val correo = findViewById<EditText>(R.id.textoCorreoRegistro).text.toString()
            val usuario = findViewById<EditText>(R.id.usuarioRegistro).text.toString()
            val contraseña = findViewById<EditText>(R.id.textContraseñaRegistro).text.toString()
            val confContraseña = findViewById<EditText>(R.id.textConfContraseñaRegistro).text.toString()

            if (contraseña == confContraseña) {
                RegistroData.correo = correo
                RegistroData.usuario = usuario
                RegistroData.contraseña = contraseña
                mostrarMensaje("Registrado")

                val intent = Intent(this, Manejar_dispositivo::class.java)
                startActivity(intent)
            } else {
                mostrarMensaje("Las contraseñas no coinciden")
            }
        }

        findViewById<TextView>(R.id.textCuenta).setOnClickListener {
            val intent = Intent(this, Iniciar_session::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }
    //bajar cambios
}
