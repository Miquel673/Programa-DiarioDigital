package com.miguel.diariodigital

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diariodigital.modelos.Entrada


class EntradaAdapter(private var entradas: List<Entrada>) :
    RecyclerView.Adapter<EntradaAdapter.ViewHolder>() {

    fun actualizarDatos(nuevasEntradas: List<Entrada>) {
        entradas = nuevasEntradas
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val contenido: TextView = view.findViewById(R.id.txtContenido)
        val fecha: TextView = view.findViewById(R.id.txtFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrada, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entrada = entradas[position]
        holder.titulo.text = entrada.titulo
        holder.contenido.text = entrada.contenido
        holder.fecha.text = entrada.fecha
    }

    override fun getItemCount(): Int = entradas.size
}
