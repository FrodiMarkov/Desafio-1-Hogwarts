package com.example.desafio1

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio1.databinding.ActivityAdminBinding


class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar binding
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajustar padding Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        // Icono hamburguesa
        /*val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

        // Botones para cargar fragmentos
        binding.btnUsuarios.setOnClickListener {
            loadFragment(FragmentoUsuarios.newInstance())
        }

        /*binding.btnAsignaturas.setOnClickListener {
            loadFragment(FragmentoAsignaturas.newInstance())
        }*/

        // Menú lateral
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_usuarios -> loadFragment(FragmentoUsuarios.newInstance())
                //R.id.nav_asignaturas -> loadFragment(FragmentoAsignaturas.newInstance())
                R.id.nav_cerrar_sesion -> finish()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Ya no cargamos ningún fragmento inicial
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()
    }
}

