package com.samm.webscraper

data class StockState(
    val loading: Boolean = false,
    val data: Stock? = null,
    val error: String = ""
)
