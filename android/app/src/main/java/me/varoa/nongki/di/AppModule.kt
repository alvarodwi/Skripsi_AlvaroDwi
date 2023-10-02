package me.varoa.nongki.di

import me.varoa.nongki.ui.screen.dataset.DatasetViewModel
import me.varoa.nongki.ui.screen.history.HistoryViewModel
import me.varoa.nongki.ui.screen.home.HomeViewModel
import me.varoa.nongki.ui.screen.result.ResultViewModel
import me.varoa.nongki.ui.screen.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::SearchViewModel)
        viewModelOf(::DatasetViewModel)
        viewModelOf(::HistoryViewModel)
        viewModelOf(::ResultViewModel)
    }
