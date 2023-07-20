package com.example.bixao10
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bixao10.databinding.ItemViewBinding
import com.example.bixao10.viewModel.ClientViewModel
import java.text.SimpleDateFormat
import java.util.*

class PendingAdapter(
    private val clientList: List<ClientModel>,
    private val viewModel: ClientViewModel
) : RecyclerView.Adapter<PendingAdapter.PendingHolder>() {

    inner class PendingHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingHolder, position: Int) {
        val client = clientList[position]
        val currentDate = Calendar.getInstance().time
        val clientDate = dateFormat.parse(client.date)
        val firstLetter = client.name.substring(0, 1).uppercase(Locale.ROOT)
        val secondLetter = client.lastname.substring(0, 1).uppercase(Locale.ROOT)
        if (clientDate != null) {
            if (clientDate.before(currentDate)) {
                holder.binding.tvFirst.text = "$firstLetter$secondLetter"
                holder.binding.tvName.text = client.name
                holder.binding.tvLastName.text = client.lastname
                holder.binding.tvDate.text = client.date
                holder.binding.tvCurse.text = client.curse

                holder.binding.btDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(holder.binding.root.context)
                    builder.setTitle("Eliminar cliente.")
                    builder.setMessage("¿Desea eliminar este cliente permanentemente?")
                    builder.setPositiveButton("Sí") { _, _ ->
                        viewModel.deleteClient(client)
                    }
                    builder.setNegativeButton("No") { dialog, _ ->
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
            } else {
                // Si la fecha no es menor a la fecha actual, ocultar la vista del cliente
                holder.itemView.layoutParams.height = 0
            }
        }
    }


    override fun getItemCount(): Int {
        return clientList.size
    }
}
