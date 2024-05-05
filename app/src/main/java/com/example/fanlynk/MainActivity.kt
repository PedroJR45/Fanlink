package com.example.pruebaconectividad

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebaconectividad.databinding.ActivityMainBinding
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Código para la inserción de datos en MySQL
        binding.button.setOnClickListener {
            val nombre = binding.editTextTextPersonName.text.toString()
            val email = binding.editTextTextPersonName2.text.toString()

            InsertarDatos().execute(nombre, email)
        }

        // Código para la navegación a otras actividades
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
            val nombre = params[0]
            val email = params[1]

            try {
                Class.forName("com.mysql.jdbc.Driver")
                val conn: Connection = DriverManager.getConnection("jdbc:mysql://ec2-54-90-34-74.compute-1.amazonaws.com:3306/itslp", "usuario", "12345")

                val query = "INSERT INTO usuarios (nombre, email) VALUES (?,?)"
                val statement: PreparedStatement = conn.prepareStatement(query)
                statement.setString(1, nombre)
                statement.setString(2, email)

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
