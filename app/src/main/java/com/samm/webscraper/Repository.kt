package com.samm.webscraper

interface Repository {
    fun scrapeData(stockSymbol: String): Stock
}