package com.miguel.diariodigital

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.diariodigital.modelos.Entrada
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrearEntradaActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_entrada)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "diario.db")
            .allowMainThreadQueries()
            .build()

        val tituloInput = findViewById<EditText>(R.id.inputTitulo)
        val contenidoInput = findViewById<EditText>(R.id.inputContenido)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val titulo = tituloInput.text.toString()
            val contenido = contenidoInput.text.toString()
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            val entrada = Entrada(titulo = titulo, contenido = contenido, fecha = fecha)
            db.entradaDao().insertar(entrada)

            finish()
        }
    }
}
