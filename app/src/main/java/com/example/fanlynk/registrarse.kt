package com.example.fanlynk

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class registrarse: AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var db: SLQliteConexion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        editTextName = findViewById(R.id.usuarioRegistro)
        editTextEmail = findViewById(R.id.textoCorreoRegistro)
        editTextPassword = findViewById(R.id.textContraseñaRegistro)
        buttonRegister = findViewById(R.id.btnRegistrarse)

        db = SLQliteConexion(this)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (db.checkUser(email)) {
                Toast.makeText(this, "Usuario ya existe", Toast.LENGTH_SHORT).show()
            } else {
                val result = db.addUser(name, email, password)
                if (result != -1L) {
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}








