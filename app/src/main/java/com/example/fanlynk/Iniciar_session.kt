package com.example.fanlynk

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

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

        findViewById<Button>(R.id.button).setOnClickListener {
            val correo_electronico = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
            val contraseña = findViewById<EditText>(R.id.editTextTextPersonName2).text.toString()

            InsertarDatos().execute(correo_electronico, contraseña)
        }

        val btn1: Button = findViewById(R.id.buttonLoginHome)
        btn1.setOnClickListener {
            val intent: Intent = Intent(this, Iniciar_session::class.java)
            startActivity(intent)
        }

        val btn2: Button = findViewById(R.id.buttonCheckHome)
        btn2.setOnClickListener {
            val intent: Intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }

    private inner class InsertarDatos : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val correo_electronico = params[0]
            val contraseña = params[1]

            try {
                Class.forName("com.mysql.jdbc.Driver")
                val conn: Connection = DriverManager.getConnection("jdbc:mysql://ec2-54-90-34-74.compute-1.amazonaws.com:3306/fanlink", "usuario", "12345")

                val query = "INSERT INTO usuarios (nombre, email) VALUES (?,?)"
                val statement: PreparedStatement = conn.prepareStatement(query)
                statement.setString(1, correo_electronico)
                statement.setString(2, contraseña)

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