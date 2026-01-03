package shub39.kovert.core.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import shub39.kovert.core.data.agents.ChatAgentFactory
import shub39.kovert.core.data.agents.MysteryMakerAgentFactory
import shub39.kovert.core.data.agents.tools.ChatAgentTools
import shub39.kovert.core.data.datastore.DataStoreFactory
import shub39.kovert.core.data.datastore.KovertDatastoreImpl
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.viewmodels.ChatScreenViewModel
import shub39.kovert.core.viewmodels.MainMenuViewModel

expect val platformModules: Module

val sharedModules = module {
    // db and datastore
    single { get<DataStoreFactory>().getPreferencesDataStore() }
    singleOf(::KovertDatastoreImpl).bind<KovertDatastore>()

    // agents
    singleOf(::ChatAgentTools)
    singleOf(::MysteryMakerAgentFactory)
    singleOf(::ChatAgentFactory)

    // viewmodels
    viewModelOf(::ChatScreenViewModel)
    viewModelOf(::MainMenuViewModel)
}