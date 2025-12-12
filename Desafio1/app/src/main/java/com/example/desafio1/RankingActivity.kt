package com.example.desafio1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio1.ViewModel.RankingViewModel
import com.example.desafio1.adapter.CasaAdapter
import com.example.desafio1.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {
    private lateinit var viewModel: RankingViewModel
    private lateinit var binding: ActivityRankingBinding
    private lateinit var adapter: CasaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewModel = ViewModelProvider(this).get(RankingViewModel::class.java)

        adapter = CasaAdapter(emptyList())
        binding.recyclerViewRanking.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRanking.adapter = adapter


        viewModel.casasRanking.observe(this) { casas ->
            adapter.updateData(casas)
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        binding.buttonVolver.setOnClickListener {
            finish()
        }
    }
}