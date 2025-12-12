package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.databinding.ActivityAlumnoHechizosBinding


class AlumnoHechizos : AppCompatActivity() {

    private lateinit var binding: ActivityAlumnoHechizosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAlumnoHechizosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnVolver.setOnClickListener {
            val intent = Intent(this, AlumnoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}