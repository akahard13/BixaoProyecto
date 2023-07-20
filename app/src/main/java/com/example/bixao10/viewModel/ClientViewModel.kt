package com.example.bixao10.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bixao10.ClientModel
import com.example.bixao10.repository.ClientRepository
import com.example.bixao10.room.RoomDabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientViewModel(application: Application) : AndroidViewModel(application) {
    val listClient  : LiveData<List<ClientModel>>
    private val repository : ClientRepository

    init {
        val clientDao = RoomDabase.getDatabase(application).ClientDao()
        repository = ClientRepository(clientDao)
        listClient = repository.getAllClients()
    }
    fun updateClient(client: ClientModel)=
        viewModelScope.launch(Dispatchers.IO){repository.updateClient(client)}
    fun insertClient(client: ClientModel) =
        viewModelScope.launch(Dispatchers.IO) { repository.saveClient(client) }
    fun deleteClient(client: ClientModel)=
        viewModelScope.launch(Dispatchers.IO){repository.deleteClient(client)}
}