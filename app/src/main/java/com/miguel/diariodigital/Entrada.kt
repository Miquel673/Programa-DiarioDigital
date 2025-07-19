package com.example.diariodigital.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entrada(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val contenido: String,
    val fecha: String
)
