package com.samm.webscraper

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.samm.webscraper.ui.theme.WebScraperTheme
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.koin.android.ext.android.get
import org.koin.core.Koin
import org.koin.core.context.startKoin

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
                    var isButtonClicked by remember { mutableStateOf(false) }
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
                                isButtonClicked = true
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


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebScraperTheme {
        Greeting("Android")
    }
}