package com.example.fanlynk

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Registrarse : AppCompatActivity() {

    private lateinit var editTextNombre: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var editTextContrasena: EditText
    private lateinit var buttonRegistrar: Button
    private lateinit var databaseHelper: SLQliteConexion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        editTextNombre = findViewById(R.id.usuarioRegistro)
        editTextCorreo = findViewById(R.id.textoCorreoRegistro)
        editTextContrasena = findViewById(R.id.textContraseñaRegistro)
        buttonRegistrar = findViewById(R.id.btnRegistrarse)

        databaseHelper = SLQliteConexion(this)

        buttonRegistrar.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val correo = editTextCorreo.text.toString().trim()
            val contrasena = editTextContrasena.text.toString().trim()

            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena)) {
                Toast.makeText(this@Registrarse, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else if (databaseHelper.checkUser(correo)) {
                Toast.makeText(this@Registrarse, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show()
            } else {
                val result = databaseHelper.addUser(nombre, correo, contrasena)
                if (result > 0) {
                    Toast.makeText(this@Registrarse, "Registro exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Registrarse, "Registro fallido", Toast.LENGTH_SHORT).show()
                }
            }
        }


        }
    }







