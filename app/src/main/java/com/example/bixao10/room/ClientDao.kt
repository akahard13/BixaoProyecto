package com.example.bixao10.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.bixao10.ClientModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * from clients")
    fun getClients(): Flow<List<ClientModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveClient(clientModel: ClientModel)

    @Update
    fun updateClient(clientModel: ClientModel)

    @Query("DELETE FROM clients")
    suspend fun deleteAllClients()

    @Delete
    suspend fun deleteClient(clientModel: ClientModel)

    @Query("SELECT * FROM clients WHERE date = :tomorrowDate")
    fun getPendingPaymentClients(tomorrowDate: String): List<ClientModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(clients: List<ClientModel>)
    @Query("SELECT * FROM clients")
    fun getClientsCSV(): List<ClientModel>
}
