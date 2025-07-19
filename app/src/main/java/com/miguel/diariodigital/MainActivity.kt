package com.miguel.diariodigital

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.miguel.diariodigital.AppDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var adapter: EntradaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "diario.db")
            .allowMainThreadQueries()
            .build()

        recyclerView = findViewById(R.id.recyclerViewEntradas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = EntradaAdapter(emptyList())
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            startActivity(Intent(this, CrearEntradaActivity::class.java))
        }

        cargarEntradas()
    }

    override fun onResume() {
        super.onResume()
        cargarEntradas()
    }

    private fun cargarEntradas() {
        val entradas = db.entradaDao().obtenerTodos()
        adapter.actualizarDatos(entradas)
    }
}
