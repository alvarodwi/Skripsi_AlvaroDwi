package me.varoa.nongki.di

import me.varoa.nongki.data.DatasetRepositoryImpl
import me.varoa.nongki.data.HistoryRepositoryImpl
import me.varoa.nongki.data.SearchRepositoryImpl
import me.varoa.nongki.domain.repository.DatasetRepository
import me.varoa.nongki.domain.repository.HistoryRepository
import me.varoa.nongki.domain.repository.SearchRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<DatasetRepository> { DatasetRepositoryImpl(get()) }
        single<HistoryRepository> { HistoryRepositoryImpl(get()) }
        single<SearchRepository> { SearchRepositoryImpl(get()) }
    }
