package cz.ackee.testtask.core.di

import cz.ackee.testtask.core.data.api.RetrofitProvider
import cz.ackee.testtask.core.data.db.CharacterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule get() = module {
    single { RetrofitProvider.provide() }
    single { CharacterDatabase.instance(androidContext()) }
}
