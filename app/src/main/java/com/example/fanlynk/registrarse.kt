package com.example.fanlynk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class registrarse : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        val btnRegistrarse: Button = findViewById(R.id.btnRegistrarse)
        val correoEditText: EditText = findViewById(R.id.textoCorreoRegistro)
        val contraseñaEditText: EditText = findViewById(R.id.textContraseñaRegistro)
        val confirmarContraseñaEditText: EditText = findViewById(R.id.textconfigurarContraseñaRegistro)
        val textCuenta: TextView = findViewById(R.id.textCuenta)


        btnRegistrarse.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()
            val confirmarContraseña = confirmarContraseñaEditText.text.toString()

            if (contraseña == confirmarContraseña) {
                registrarUsuario(correo, contraseña)
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }
        textCuenta.setOnClickListener {
            val intent = Intent(this, Iniciar_session::class.java)
            startActivity(intent)
        }
    }

    private fun registrarUsuario(correo: String, contraseña: String) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Manejar_dispositivo::class.java)
                    startActivity(intent)
                    finish() // Opcional: cierra la actividad actual si no deseas que el usuario vuelva atrás
                    // Aquí puedes guardar información adicional del usuario en la base de datos si es necesario
                } else {
                    // Registro fallido
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
