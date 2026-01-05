package shub39.kovert.core.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import shub39.kovert.core.data.database.DatabaseFactory
import shub39.kovert.core.data.datastore.DataStoreFactory

actual val platformModules: Module = module {
    singleOf(::DataStoreFactory)
    singleOf(::DatabaseFactory)
}