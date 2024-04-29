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



}