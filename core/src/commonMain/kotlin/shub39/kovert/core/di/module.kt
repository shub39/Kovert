package shub39.kovert.core.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import shub39.kovert.core.data.agents.ChatAgentHandler
import shub39.kovert.core.data.agents.ChatAgentToolsImpl
import shub39.kovert.core.data.agents.MysteryFactory
import shub39.kovert.core.data.database.DatabaseFactory
import shub39.kovert.core.data.database.MysteryDatabase
import shub39.kovert.core.data.datastore.DataStoreFactory
import shub39.kovert.core.data.datastore.KovertDatastoreImpl
import shub39.kovert.core.domain.ChatAgentTools
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.viewmodels.ChatScreenViewModel
import shub39.kovert.core.viewmodels.MainMenuViewModel

expect val platformModules: Module

val sharedModules = module {
    // db and datastore
    single { get<DataStoreFactory>().getPreferencesDataStore() }
    singleOf(::KovertDatastoreImpl).bind<KovertDatastore>()
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<MysteryDatabase>().mysteryDataDao }

    // agents
    singleOf(::ChatAgentHandler)
    singleOf(::ChatAgentToolsImpl).bind<ChatAgentTools>()
    singleOf(::MysteryFactory)
    singleOf(::ChatAgentHandler)

    // viewmodels
    viewModelOf(::ChatScreenViewModel)
    viewModelOf(::MainMenuViewModel)
}