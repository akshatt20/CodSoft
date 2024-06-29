package com.example.quoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteQuotesAdapter(private val quotes: List<Quote>) :
    RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quoteText: TextView = view.findViewById(R.id.quoteText)
        val authorText: TextView = view.findViewById(R.id.authorText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_quote_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.quoteText.text = "\"${quote.text}\""
        holder.authorText.text = "- ${quote.author}"
    }

    override fun getItemCount() = quotes.size
}