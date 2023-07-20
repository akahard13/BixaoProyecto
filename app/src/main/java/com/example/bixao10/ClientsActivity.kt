package com.example.bixao10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bixao10.databinding.ActivityClientsBinding
import com.example.bixao10.viewModel.ClientViewModel

class ClientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientsBinding
    private lateinit var adapterClient: ClientAdapter
    private lateinit var viewModel: ClientViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.rvClients.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ClientViewModel::class.java]

        observeEvents()
        val btnGoHome = binding.btHome
        btnGoHome.setOnClickListener {
            goHome()
        }

        val btnAddClient = binding.btAdd
        btnAddClient.setOnClickListener {
            goToAddClient()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.bt_add) {
            val intent = Intent(this, AgendarActivity::class.java)
            isEdit = false
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun observeEvents() {
        viewModel.listClient.observe(this, Observer { list ->
            list?.let {
                adapterClient = ClientAdapter(it, viewModel)
                binding.rvClients.adapter = adapterClient
                adapterClient.notifyDataSetChanged()
            }
        })
    }
    private fun goToAddClient() {
        val intent = Intent(this, AgendarActivity::class.java)
        startActivity(intent)
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    companion object {
        var isEdit = false
        lateinit var clientSelect : ClientModel
    }
}
