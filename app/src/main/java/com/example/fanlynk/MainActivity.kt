package com.example.fanlynk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencias a los botones
        val buttonLoginHome = findViewById<Button>(R.id.buttonLoginHome)
        val buttonCheckHome = findViewById<Button>(R.id.buttonCheckHome)

        // Configurar OnClickListener para los botones
        buttonLoginHome.setOnClickListener {
            Ir_a_inicio_de_sesion()
        }

        buttonCheckHome.setOnClickListener {
            Ir_a_Registrarse()
        }
    }

    private fun Ir_a_inicio_de_sesion() {
        val intent = Intent(this, Iniciar_session::class.java)
        startActivity(intent)
    }

    private fun Ir_a_Registrarse() {
        val intent = Intent(this, registrarse::class.java)
        startActivity(intent)
    }
}



