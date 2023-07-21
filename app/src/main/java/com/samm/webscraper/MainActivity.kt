package com.samm.webscraper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samm.webscraper.ui.theme.WebScraperTheme
import org.koin.android.ext.android.get
import org.koin.core.context.startKoin

/*
    Todo:
        - Have the scraper run in the background and scrape data when the app is closed
        - Save scraped data to a "history" table in a database - mongodb or sqlite idc.
 */

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            modules(KoinModule.appModule)
        }

        setContent {
            val viewModel : ScraperViewModel = get()
            WebScraperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state = viewModel.state.collectAsState().value
                    var text by remember { mutableStateOf("") }

                    val symbol = state?.data?.symbol
                    val marketChange = state?.data?.stockMarketChange ?: "Not found"
                    val price = state?.data?.stockPrice ?: "Not found"
                    val percentChange = state?.data?.stockPercentChange ?: "Not found"
                    
                    Column(Modifier.padding(15.dp)) {
                        Text(
                            text = "Yahoo Stock Scraper",
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        OutlinedTextField(value = text, onValueChange = { text = it })
                        Button(
                            onClick = {
                                viewModel.getData(text)
                            },
                            modifier = Modifier.padding(top = 15.dp)
                        ) {
                            Text(text = "Get Data") 
                        }

                        Spacer(modifier = Modifier.padding(bottom = 15.dp))
                        when {
                            state?.loading == true -> {
                                CircularProgressIndicator()
                            }
                            state?.data != null -> {
                                Text(text = "Stock Price at close for $symbol: \n $$price  $marketChange  $percentChange")
                            }
                            state?.error?.isNotBlank() == true -> {
                                Text(text = state.error)
                            }
                        }
                    }
                }
            }
        }
    }
}