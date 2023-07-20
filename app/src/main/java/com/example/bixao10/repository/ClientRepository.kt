package com.example.bixao10.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.bixao10.ClientModel
import com.example.bixao10.room.ClientDao


class ClientRepository constructor(
    private val ClientDao: ClientDao
)
{
    fun getAllClients(): LiveData<List<ClientModel>> = ClientDao.getClients().asLiveData()

    suspend fun saveClient(client: ClientModel){
        ClientDao.saveClient(client)
    }
    suspend fun updateClient(client: ClientModel){
        ClientDao.updateClient(client)
    }
    suspend fun deleteClient(client: ClientModel){
        ClientDao.deleteClient(client)
    }

}