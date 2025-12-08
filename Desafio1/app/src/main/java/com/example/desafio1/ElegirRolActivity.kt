package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.databinding.ActivityElegirRolBinding

class ElegirRolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElegirRolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar usuario ID si se necesita
        val usuarioId = intent.getIntExtra("usuario_id", -1)

        binding.btProfesor.setOnClickListener {
            startActivity(Intent(this, ProfesorActivity::class.java).apply {
                putExtra("usuario_id", usuarioId)
            })
            finish()
        }

        binding.btAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
    }
}