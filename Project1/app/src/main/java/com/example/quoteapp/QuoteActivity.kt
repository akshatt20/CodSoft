package com.example.quoteapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.Calendar
import java.util.Date
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable



class QuoteActivity : AppCompatActivity() {
    private val quotes = listOf(
        Quote("The best way to predict the future is to invent it.", "Alan Kay"),
        // ... (other quotes remain the same)
        Quote("The best revenge is massive success.", "Frank Sinatra")
    )

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var shareButton: Button
    private lateinit var refreshButton: Button
    private lateinit var favoriteButton: Button
    private lateinit var viewFavoritesButton: Button
    private var lastRefreshDate: Date? = null
    private var currentQuote: Quote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)

        quoteTextView = findViewById(R.id.quoteTextView)
        authorTextView = findViewById(R.id.authorTextView)
        refreshButton = findViewById(R.id.refreshButton)
        shareButton = findViewById(R.id.shareButton)
        favoriteButton = findViewById(R.id.favoriteButton)
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton)

        refreshButton.setOnClickListener { refreshQuote() }
        shareButton.setOnClickListener { shareQuote() }
        favoriteButton.setOnClickListener { toggleFavorite() }
        viewFavoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        dailyDisplayQuote()
    }

    private fun getRandomQuote(): Quote = quotes.random()

    private fun shouldRefreshQuote(): Boolean {
        val currentDate = Calendar.getInstance().time
        return lastRefreshDate == null || currentDate.after(lastRefreshDate)
    }

    private fun getDailyQuote(): Quote {
        return if (shouldRefreshQuote()) {
            val newQuote = getRandomQuote()
            lastRefreshDate = Calendar.getInstance().time
            currentQuote = newQuote
            newQuote
        } else {
            currentQuote ?: getRandomQuote()
        }
    }

    private fun displayQuote(quote: Quote) {
        quoteTextView.text = "\"${quote.text}\""
        authorTextView.text = "- ${quote.author}"
        updateFavoriteButtonText()
    }

    private fun refreshQuote() {
        val newQuote = getRandomQuote()
        currentQuote = newQuote
        lastRefreshDate = Calendar.getInstance().time
        displayQuote(newQuote)
    }

    private fun dailyDisplayQuote() {
        val quote = getDailyQuote()
        displayQuote(quote)
    }

    private fun shareQuote() {
        currentQuote?.let { quote ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Inspirational Quote")
                putExtra(Intent.EXTRA_TEXT, "\"${quote.text}\"\n- ${quote.author}")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Quote"))
        }
    }

    private fun toggleFavorite() {
        currentQuote?.let { quote ->
            val sharedPref = getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE)
            val favoriteQuotes = getFavoriteQuotes(sharedPref)

            if (favoriteQuotes.any { it.text == quote.text }) {
                favoriteQuotes.removeAll { it.text == quote.text }
            } else {
                favoriteQuotes.add(quote)
            }

            with(sharedPref.edit()) {
                putString("favorites", Json.encodeToString(favoriteQuotes))
                apply()
            }
            updateFavoriteButtonText()
        }
    }

    private fun getFavoriteQuotes(sharedPref: SharedPreferences): MutableList<Quote> {
        val favoritesJson = sharedPref.getString("favorites", "[]")
        return Json.decodeFromString(favoritesJson ?: "[]")
    }

    private fun updateFavoriteButtonText() {
        currentQuote?.let { quote ->
            val sharedPref = getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE)
            val favoriteQuotes = getFavoriteQuotes(sharedPref)
            favoriteButton.text = if (favoriteQuotes.any { it.text == quote.text }) "Remove from Favorites" else "Add to Favorites"
        }
    }
}