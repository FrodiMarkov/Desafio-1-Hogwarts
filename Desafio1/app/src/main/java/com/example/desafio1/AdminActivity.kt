package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.desafio1.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.btnUsuarios.setOnClickListener {
            navController.navigate(R.id.fragmentoUsuarios3)
        }

        binding.btnAsignaturas.setOnClickListener {
            navController.navigate(R.id.fragmentoAsignatura)
        }

        binding.btnCrearUsuario.setOnClickListener {
            val intent = Intent(this, CrearUsuarioActivity::class.java)
            startActivity(intent)
        }

        binding.btnCrearAsignatura.setOnClickListener {
            val intent = Intent(this, CrearAsignaturaActivity::class.java)
            startActivity(intent)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_usuarios -> navController.navigate(R.id.fragmentoUsuarios3)
                R.id.nav_asignaturas -> navController.navigate(R.id.fragmentoAsignatura)
                R.id.nav_cerrar_sesion -> finish()
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}
