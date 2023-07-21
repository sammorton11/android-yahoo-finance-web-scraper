package com.samm.webscraper

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class RepositoryImpl: Repository {

    override fun scrapeData(stockSymbol: String): Stock {
        val url = "https://finance.yahoo.com/quote/$stockSymbol"
        var stockMarketChange: String? = null
        var stockPercentChange: String? = null
        var stockPrice: String? = null

        try {
            val document: Document = Jsoup.connect(url).get()
            val finStreamers = document.select("fin-streamer[data-symbol='$stockSymbol']")

            val stockPriceElement = finStreamers.first { it.attr("data-field") == "regularMarketPrice" }
            stockMarketChange = finStreamers.firstOrNull { it.attr("data-field") == "regularMarketChange" }?.text()
            stockPercentChange = finStreamers.firstOrNull { it.attr("data-field") == "regularMarketChangePercent" }?.text()

            stockPrice = stockPriceElement.text()
        } catch (e: Exception) {
            Log.e("YahooFinanceScraper", "Error scraping data for $stockSymbol: ${e.message}")
        }
        return Stock(stockSymbol, stockMarketChange, stockPercentChange, stockPrice)
    }
}