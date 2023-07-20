package com.example.bixao10

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bixao10.room.ClientDao
import com.example.bixao10.room.RoomDabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

class BackupActivity : AppCompatActivity() {

    private lateinit var exportLauncher: ActivityResultLauncher<Intent>
    private lateinit var importLauncher: ActivityResultLauncher<Intent>

    private val EXPORT_REQUEST_CODE = 100
    private val IMPORT_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        exportLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    exportToCSV(uri)
                }
            }
        }

        importLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    importFromCSV(uri)
                }
            }
        }
    }

    fun exportDatabase(view: View) {
        val exportIntent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        exportIntent.addCategory(Intent.CATEGORY_OPENABLE)
        exportIntent.type = "text/csv"
        exportIntent.putExtra(Intent.EXTRA_TITLE, "clients_backup_${getCurrentDateTime()}.csv")
        exportLauncher.launch(exportIntent)
    }
    private fun exportToCSV(uri: Uri) {
        val database = RoomDabase.getDatabase(this)
        val clientDao: ClientDao = database.ClientDao()

        lifecycleScope.launch {
            try {
                // Get the list of clients from the database
                val clientsList = withContext(Dispatchers.IO) {
                    clientDao.getClientsCSV()
                }

                if (clientsList.isNotEmpty()) {
                    val outputStream = contentResolver.openOutputStream(uri)
                    val writer = BufferedWriter(OutputStreamWriter(outputStream))

                    // Write header
                    writer.write("Name,Lastname,Date,Curse\n")

                    // Write data for each client
                    for (clientData in clientsList) {
                        writer.write("${clientData.name},${clientData.lastname},${clientData.date},${clientData.curse}\n")
                    }

                    writer.flush()
                    writer.close()

                    Toast.makeText(this@BackupActivity, "Datos exportados correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BackupActivity, "No hay datos para exportar.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@BackupActivity, "Error al exportar los datos.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun importDatabase(view: View) {
        val importIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        importIntent.addCategory(Intent.CATEGORY_OPENABLE)
        importIntent.type = "*/*"
        importLauncher.launch(importIntent)
    }

    private fun importFromCSV(uri: Uri) {
        val database = RoomDabase.getDatabase(this)
        val clientDao: ClientDao = database.ClientDao()

        val inputStream = contentResolver.openInputStream(uri)
        val reader = inputStream?.bufferedReader()

        if (reader != null) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val clients = mutableListOf<ClientModel>()

                        // Skip the header row
                        reader.readLine()

                        // Read data for each client
                        var line: String? = reader.readLine()
                        while (line != null) {
                            val data = line.split(",")
                            if (data.size == 4) { // Assuming 4 columns in CSV (Name,Lastname,Date,Curse)
                                val name = data[0]
                                val lastname = data[1]
                                val date = data[2]
                                val curse = data[3]

                                val client = ClientModel(0, name, lastname, date, curse) // Set ID to 0 (auto-generated)
                                clients.add(client)

                                line = reader.readLine()
                            }
                        }

                        // Insert clients to the database
                        if (clients.isNotEmpty()) {
                            clientDao.insertAll(clients)
                            Toast.makeText(this@BackupActivity, "Datos importados correctamente.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@BackupActivity, "No se encontraron datos válidos en el archivo.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@BackupActivity, "Error al importar los datos.", Toast.LENGTH_SHORT).show()
                    } finally {
                        reader.close()
                    }
                }
            }
        } else {
            Toast.makeText(this@BackupActivity, "Error al abrir el archivo CSV.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return sdf.format(Date())
    }
}
