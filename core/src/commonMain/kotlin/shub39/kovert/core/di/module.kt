package shub39.kovert.core.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import shub39.kovert.core.data.agents.MysteryMakerAgentHandler
import shub39.kovert.core.data.agents.tools.SnackBarTools
import shub39.kovert.core.viewmodels.ChatScreenViewModel

val modules = module {
    singleOf(::SnackBarTools)
    singleOf(::MysteryMakerAgentHandler)

    viewModelOf(::ChatScreenViewModel)
}