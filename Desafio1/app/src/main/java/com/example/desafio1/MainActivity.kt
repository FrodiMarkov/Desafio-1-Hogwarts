package com.example.desafio1

import ViewModel.LoginViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.desafio1.databinding.ActivityMainBinding
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

<<<<<<< .merge_file_9aZauL
        binding.btRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btLogin.setOnClickListener {
            val emailLogin = binding.etUser.text.toString()
            val passwordLogin = binding.etPassw.text.toString()

            lifecycleScope.launch {
                try {
                    val usuarios = retrofit.listarUsuariosConRoles()
                    val usuario = usuarios.find { it.nombre == emailLogin && it.contrasena == passwordLogin }

                    if (usuario != null) {
                        when {
                            usuario.roles.contains(1) -> {
                                val intent = Intent(this@MainActivity, AlumnoActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            usuario.roles.contains(4) -> {
                                val intent = Intent(this@MainActivity, DumbledorActivity::class.java)
                                intent.putExtra("usuario_id", usuario.id)
                                startActivity(intent)
                                finish()
                            }
                            usuario.roles.contains(2) -> {
                                val intent = Intent(this@MainActivity, ProfesorActivity::class.java)
                                intent.putExtra("usuario_id", usuario.id)
                                startActivity(intent)
                                finish()
                            }
                            else -> Toast.makeText(this@MainActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error en la conexión: ${e.message}", Toast.LENGTH_SHORT).show()
=======
        viewModel.usuarioLogueado.observe(this) { usuario ->
            usuario?.let {
                when {
                    it.roles.contains(1) -> startActivity(Intent(this, AlumnoActivity::class.java))
                    it.roles.contains(4) -> startActivity(Intent(this, DumbledorActivity::class.java).apply {
                        putExtra("usuario_id", it.id)
                    })
                    it.roles.contains(2) -> startActivity(Intent(this, ProfesorActivity::class.java).apply {
                        putExtra("usuario_id", it.id)
                    })
                    else -> Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
>>>>>>> .merge_file_JbRizc
                }
                finish()
            }
        }

        viewModel.error.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btLogin.setOnClickListener {
            val usuarioLogin = binding.etUser.text.toString()
            val passwordLogin = binding.etPassw.text.toString()
            viewModel.login(usuarioLogin, passwordLogin)
        }

        Glide.with(this)
            //que poner para que sea un gif y no una imagen
            .asGif()
            .load(R.drawable.background2)
            .into(binding.IVBackground)
    }
}