package com.miguel.diariodigital

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diariodigital.modelos.Entrada
import com.example.diariodigital.data.EntradaDao

@Database(entities = [Entrada::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entradaDao(): EntradaDao
}
