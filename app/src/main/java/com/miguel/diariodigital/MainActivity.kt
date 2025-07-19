package com.miguel.diariodigital

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL



class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var adapter: EntradaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplicar tema antes de cargar la vista
        lifecycleScope.launch {
            val isDark = ThemeHelper.getThemePreference(this@MainActivity).first()
            ThemeHelper.applyTheme(isDark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mostrar clima
        val climaTextView = findViewById<TextView>(R.id.tvClima)

        lifecycleScope.launch {
            val clima = obtenerClimaCiudad("Bogotá")
            climaTextView.text = clima
        }


        // Botón para cambiar el tema
        val btnCambiarTema = findViewById<Button>(R.id.btnCambiarTema)
        btnCambiarTema.setOnClickListener {
            lifecycleScope.launch {
                val current = ThemeHelper.getThemePreference(this@MainActivity).first()
                ThemeHelper.saveThemePreference(this@MainActivity, !current)
                recreate()
            }
        }

        // Inicializar base de datos y RecyclerView
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "diario.db")
            .allowMainThreadQueries()
            .build()

        recyclerView = findViewById(R.id.recyclerViewEntradas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = EntradaAdapter(emptyList())
        recyclerView.adapter = adapter

        // Botón para agregar nueva entrada
        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            startActivity(Intent(this, CrearEntradaActivity::class.java))
        }

        cargarEntradas()
    }

    suspend fun obtenerClimaCiudad(ciudad: String): String = withContext(Dispatchers.IO) {
        try {
            val apiKey = "a674c2e95ac326e98b5a347b84ab481a"
            val url = URL("https://api.openweathermap.org/data/2.5/weather?q=${ciudad}&appid=$apiKey&units=metric&lang=es")

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                connect()

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext "Error de conexión ($responseCode)"
                }

                val response = inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)

                val main = json.getJSONObject("main")
                val temp = main.getDouble("temp").toInt()
                val weather = json.getJSONArray("weather").getJSONObject(0)
                val descripcion = weather.getString("description")

                return@withContext "Clima actual en $ciudad: $temp°C, $descripcion"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error al obtener clima: ${e.localizedMessage ?: "desconocido"}"
        }
    }

    override fun onResume() {
        super.onResume()
        cargarEntradas()
    }

    private fun cargarEntradas() {
        val entradas = db.entradaDao().obtenerTodos()
        adapter.actualizarDatos(entradas)
    }

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
