package com.example.bixao10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.bixao10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val btnAddClient = findViewById<LinearLayout>(R.id.bt_add)
        btnAddClient.setOnClickListener{
            goToAddClient()
        }
        val btnBackup = findViewById<LinearLayout>(R.id.bt_backup)
        btnBackup.setOnClickListener{
            goToBackup()
        }
        val btnVerPending = findViewById<LinearLayout>(R.id.bt_pending)
        btnVerPending.setOnClickListener{
            goToSeePending()
        }
        val btnVerClients = findViewById<LinearLayout>(R.id.bt_list)
        btnVerClients.setOnClickListener{
            goToSeeClients()
        }
    }
    private fun goToAddClient(){
        val i = Intent(this, AgendarActivity::class.java)
        startActivity(i)
    }
    private fun goToSeeClients(){
        val intent = Intent(this, ClientsActivity::class.java)
        startActivity(intent)
    }
    private fun goToSeePending(){
        val intent = Intent(this, PendingActivity::class.java)
        startActivity(intent)
    }
    private fun goToBackup(){
        val intent = Intent(this, BackupActivity::class.java)
        startActivity(intent)
    }
}


