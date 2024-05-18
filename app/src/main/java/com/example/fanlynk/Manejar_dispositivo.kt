package com.example.fanlynk

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Manejar_dispositivo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manejar_dispositivo)

        val button = findViewById<ImageView>(R.id.imgAgregar)
        button.setOnClickListener{
            val intent = Intent(this, sincronizar::class.java)
            startActivity(intent)
        }
        val button2 = findViewById<ImageView>(R.id.imgVincular)
        button2.setOnClickListener{
            val intent = Intent(this, vincular::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}