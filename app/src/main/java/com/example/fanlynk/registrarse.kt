package com.example.fanlynk

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonRegister = findViewById<Button>(R.id.btnRegistrarse)
        buttonRegister.setOnClickListener {
            val name = findViewById<EditText>(R.id.textoCorreoRegistro).text.toString()
            val email = findViewById<EditText>(R.id.usuarioRegistro).text.toString()
            val password = findViewById<EditText>(R.id.textConfContraseñaRegistro).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.textContraseñaRegistro).text.toString()

            if (password == confirmPassword) {
                InsertarUsuario().execute(name, email, password)
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class InsertarUsuario : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            val name = params[0]
            val email = params[1]
            val password = params[2]

            return try {
                Class.forName("com.mysql.jdbc.Driver")
                val conn: Connection = DriverManager.getConnection("localhost/fanlink", "usuario", "12345")

                val query = "INSERT INTO usuario (nombre, correo_electronico, contraseña) VALUES (?, ?, ?)"
                val statement: PreparedStatement = conn.prepareStatement(query)
                statement.setString(1, name)
                statement.setString(2, email)
                statement.setString(3, password)

                statement.executeUpdate()
                conn.close()

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                Toast.makeText(applicationContext, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Iniciar_session::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
