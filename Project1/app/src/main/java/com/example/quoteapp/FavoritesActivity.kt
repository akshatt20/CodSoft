package com.example.quoteapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val recyclerView: RecyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE)
        val favoriteQuotes = getFavoriteQuotes(sharedPref)

        val adapter = FavoriteQuotesAdapter(favoriteQuotes)
        recyclerView.adapter = adapter
    }

    private fun getFavoriteQuotes(sharedPref: SharedPreferences): List<Quote> {
        val favoritesJson = sharedPref.getString("favorites", "[]")
        return Json.decodeFromString(favoritesJson ?: "[]")
    }
}