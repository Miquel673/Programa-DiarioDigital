package com.example.diariodigital.data

import androidx.room.*
import com.example.diariodigital.modelos.Entrada

@Dao
interface EntradaDao {
    @Insert
    fun insertar(entrada: Entrada)

    @Query("SELECT * FROM Entrada ORDER BY id DESC")
    fun obtenerTodos(): List<Entrada>

    @Delete
    fun eliminar(entrada: Entrada)
}
