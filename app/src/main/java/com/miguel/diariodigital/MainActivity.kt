package com.miguel.diariodigital

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var adapter: EntradaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplicar tema antes de cargar vista
        lifecycleScope.launch {
            val isDark = ThemeHelper.getThemePreference(this@MainActivity).first()
            ThemeHelper.applyTheme(isDark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón para cambiar el tema (modo claro/oscuro)
        val btnCambiarTema = findViewById<Button>(R.id.btnCambiarTema)
        btnCambiarTema.setOnClickListener {
            lifecycleScope.launch {
                val current = ThemeHelper.getThemePreference(this@MainActivity).first()
                ThemeHelper.saveThemePreference(this@MainActivity, !current)
                recreate()
            }
        }

        // Inicializar base de datos y recycler
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "diario.db")
            .allowMainThreadQueries()
            .build()

        recyclerView = findViewById(R.id.recyclerViewEntradas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = EntradaAdapter(emptyList())
        recyclerView.adapter = adapter

        // Botón para crear nueva entrada
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

    // Menú de opciones en la barra superior
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                lifecycleScope.launch {
                    val current = ThemeHelper.getThemePreference(this@MainActivity).first()
                    ThemeHelper.saveThemePreference(this@MainActivity, !current)
                    recreate()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
