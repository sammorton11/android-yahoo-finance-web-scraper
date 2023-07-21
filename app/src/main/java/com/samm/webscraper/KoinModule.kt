package com.samm.webscraper

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object KoinModule {
    val appModule = module {
        single<Repository> { RepositoryImpl() }
        viewModelOf(::ScraperViewModel)
    }
}