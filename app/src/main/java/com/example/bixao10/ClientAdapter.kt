package com.example.bixao10

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bixao10.databinding.ItemViewBinding
import com.example.bixao10.viewModel.ClientViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClientAdapter(
    private val clientList: List<ClientModel>,
    private val viewModel: ClientViewModel
) : RecyclerView.Adapter<ClientAdapter.ClientHolder>() {

    inner class ClientHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientHolder(binding)
    }
    override fun onBindViewHolder(holder: ClientHolder, position: Int) {
        val client = clientList[position]
        val firstLetter = client.name.substring(0, 1).uppercase(Locale.ROOT)
        val secondLetter = client.lastname.substring(0, 1).uppercase(Locale.ROOT)
        holder.binding.tvFirst.text = "$firstLetter$secondLetter"
        holder.binding.tvName.text = client.name
        holder.binding.tvLastName.text = client.lastname
        holder.binding.tvDate.text = client.date
        holder.binding.tvCurse.text = client.curse

        holder.binding.btDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.binding.root.context)
            builder.setTitle("Eliminar cliente.")
            builder.setMessage("¿Desea eliminar este cliente permanentemente?")
            builder.setPositiveButton("Sí"){_, _ ->
                viewModel.deleteClient(client)
            }
            builder.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }
        holder.binding.btEdit.setOnClickListener {
            val intent = Intent(holder.binding.root.context, AgendarActivity::class.java)
            ClientsActivity.isEdit = true
            ClientsActivity.clientSelect = client
            holder.binding.root.context.startActivity(intent)
        }
        holder.binding.btPay.setOnClickListener {
            // Obtener la fecha actual del cliente
            val currentClientDate = client.date

            // Convertir la fecha a un formato manejable (por ejemplo, yyyy-MM-dd)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.parse(currentClientDate)

            // Agregar un mes a la fecha actual
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.MONTH, 1)

            // Obtener la nueva fecha después de agregar un mes
            val newDate = calendar.time

            // Convertir la nueva fecha al formato original del cliente
            val newFormattedDate = sdf.format(newDate)

            // Actualizar la fecha del cliente
            client.date = newFormattedDate
            val builder = AlertDialog.Builder(holder.binding.root.context)
            builder.setTitle("Mensualidad.")
            builder.setMessage("¿Desea realizar el pago de un mes de este cliente?")
            builder.setPositiveButton("Sí"){_, _ ->
                viewModel.updateClient(client)
                notifyDataSetChanged()
                Toast.makeText(holder.binding.root.context, "Pago realizado con éxito.", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
    override fun getItemCount(): Int {
        return clientList.size
    }
}
