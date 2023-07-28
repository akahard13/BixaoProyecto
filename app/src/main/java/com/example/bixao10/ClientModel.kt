package com.example.bixao10
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import java.util.Date

@Entity(tableName = "clients")
data class ClientModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "lastname") var lastname: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "curse") var curse: String,
    @ColumnInfo(name = "historial") var historial: String = Gson().toJson(PaymentHistory())
){
    // Función para inicializar el historial con el primer pago usando la fecha de registro
    fun initializePaymentHistory(registrationDate: String) {
        val firstPayment = Payment(registrationDate) // El monto inicial puede ser 0.0 o cualquier otro valor predeterminado
        val initialHistory = PaymentHistory(listOf(firstPayment) as MutableList<Payment>)
        historial = Gson().toJson(initialHistory)
    }
}

