package com.example.fanlynk

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

        // Obtener referencias a los EditText y al botón
        val textNombre = findViewById<EditText>(R.id.usuarioRegistro)
        val textContraseña = findViewById<EditText>(R.id.textContraseñaRegistro)
        val textCorreo_electronico = findViewById<EditText>(R.id.textoCorreoRegistro)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        // Ejecutar la tarea asíncrona al hacer clic en el botón de registro
        btnRegistrarse.setOnClickListener {
            val nombre = textNombre.text.toString()
            val contraseña = textContraseña.text.toString()
            val correo_electronico = textCorreo_electronico.text.toString()

            val insertarDatos = InsertarDatos()
            insertarDatos.execute(nombre, contraseña, correo_electronico)
        }
    }

    private inner class InsertarDatos : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val nombre = params[0]
            val contraseña = params[1]
            val correo_electronico = params[2]

            try {
                Class.forName("com.mysql.jdbc.Driver")
                val conn: Connection = DriverManager.getConnection("jdbc:mysql://ec2-54-90-34-74.compute-1.amazonaws.com:3306/fanlink", "usuario", "12345")
                val query = "INSERT INTO usuario (nombre, contraseña, correo_electronico) VALUES (?,?,?)"
                val statement: PreparedStatement = conn.prepareStatement(query)
                statement.setString(1, nombre)
                statement.setString(2, contraseña)
                statement.setString(3, correo_electronico)

                val rowsInserted: Int = statement.executeUpdate()
                conn.close()

                return if (rowsInserted > 0) {
                    "Datos insertados correctamente"
                } else {
                    "No se pudo insertar los datos"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Excepción: ${e.message}"
            }
        }

        override fun onPostExecute(result: String) {
            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
        }
    }
}
