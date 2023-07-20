package com.example.bixao10
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "clients")
data class ClientModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "lastname") var lastname: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "curse") var curse: String
)

