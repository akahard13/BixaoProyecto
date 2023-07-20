package com.example.bixao10

import SpinnerAdapter
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.bixao10.ClientsActivity.Companion.clientSelect
import com.example.bixao10.databinding.ActivityAgendarBinding
import com.example.bixao10.viewModel.ClientViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AgendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgendarBinding
    private lateinit var viewModel: ClientViewModel
    private val selectedCalendar = Calendar.getInstance()
    private var selectedOption: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar)
        binding = ActivityAgendarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val etName = findViewById<EditText>(R.id.et_name)
        val etLastname = findViewById<EditText>(R.id.et_last_name)
        val etDate = findViewById<EditText>(R.id.et_date)
        val btnNext = findViewById<Button>(R.id.btn_next)
        val cvresume = findViewById<CardView>(R.id.cv_resume)
        val cvPrincipal = findViewById<CardView>(R.id.cv_Principal)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val items = arrayOf("Jiu-Jitsu", "Jiu-jitsu2", "Jiu-Jitsu3", "Jiu-Jitsu4")
        val adapter = SpinnerAdapter(this, items)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOption = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó ninguna opción, dejar selectedOption como null
            }
        }
        if (selectedOption == null) {
            selectedOption = spinner.getItemAtPosition(0).toString()
        }



        btnNext.setOnClickListener {
            if (etName.text.toString().length < 3) {
                etName.error = "Este campo es obligatorio"
            } else if (etLastname.text.toString().length < 3) {
                etLastname.error = "Este campo es obligatorio"
            } else if (etDate.text.toString().length < 3) {
                etDate.error = "Este campo es obligatorio"
                Snackbar.make(
                    etDate,
                    "Debe seleccionar una fecha para la cita",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                showClientData()
                cvPrincipal.visibility = View.GONE
                cvresume.visibility = View.VISIBLE
            }
        }
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ClientViewModel::class.java]
        if(ClientsActivity.isEdit){
            binding.etName.setText(clientSelect.name.toString())
            binding.etLastName.setText(clientSelect.lastname.toString())
            binding.etDate.setText(clientSelect.date.toString())
            val selectedOption = clientSelect.curse // Obtener la opción almacenada en el cliente seleccionado
            val adapter = binding.spinner.adapter as SpinnerAdapter
            val position = adapter.getPosition(selectedOption) // Obtener la posición de la opción seleccionada en el adaptador
            binding.spinner.setSelection(position)
        }
        binding.btnSave.setOnClickListener {
            GlobalScope.launch {
                if(ClientsActivity.isEdit)
                {
                    update()
                    ClientsActivity.isEdit = false
                }
                else {
                    save()
                }

            }
        }
    }
    override fun onBackPressed() {
        val cvResumen = findViewById<CardView>(R.id.cv_resume)
        val cvPrincipal = findViewById<CardView>(R.id.cv_Principal)
        if(cvResumen.visibility == View.VISIBLE){
            cvResumen.visibility = View.GONE
            cvPrincipal.visibility=View.VISIBLE
        }else if(cvPrincipal.visibility == View.VISIBLE){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿Está seguro que desea salir?")
            builder.setMessage("Si abandonas el formulario perderá toda la informacion ingresada.")
            builder.setPositiveButton("Salir"){_, _ ->
                finish()
            }
            builder.setNegativeButton("Continuar"){dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

    }
    fun update(){
        clientSelect.name = binding.tvName.text.toString()
        clientSelect.lastname = binding.tvLastName.text.toString()
        clientSelect.date = binding.tvDate.text.toString()
        clientSelect.curse = selectedOption.toString()
        viewModel.updateClient(clientSelect)

        this.finish()

    }
    private suspend fun save() {
        var newClient = ClientModel(0,
            binding.etName.text.toString(), binding.etLastName.text.toString(),
            binding.etDate.text.toString(),
            selectedOption.toString()
        )
        viewModel.insertClient(newClient)
        this.finish()

    }
    fun onClickScheduleDate(v: View?) {
        val etScheduleDate = findViewById<EditText>(R.id.et_date)
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)
        val listener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            selectedCalendar.set(y, m, d)
            val displayMonth = m + 2 // Sumar 1 al valor del mes
            etScheduleDate.setText("$y-$displayMonth-$d")
            etScheduleDate.error = null
        }
        DatePickerDialog(this, listener, year, month, dayOfMonth).show()
    }
    private fun showClientData(){
        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvLastname = findViewById<TextView>(R.id.tv_last_name)
        val tvDate= findViewById<TextView>(R.id.tv_date)
        val tvCurse = findViewById<TextView>(R.id.tv_curse)
        val etName = findViewById<EditText>(R.id.et_name)
        val etLastname = findViewById<EditText>(R.id.et_last_name)
        val etDate = findViewById<EditText>(R.id.et_date)

        tvName.text = etName.text.toString()
        tvLastname.text = etLastname.text.toString()
        tvDate.text = etDate.text.toString()
        tvCurse.text = selectedOption.toString()
    }

}
