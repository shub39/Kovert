package shub39.kovert.core.app

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Routes: NavKey {
    @Serializable
    data object MainMenu: Routes, NavKey

    @Serializable
    data object ChatScreen: Routes, NavKey

    companion object {
        val config = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(MainMenu::class, MainMenu.serializer())
                    subclass(ChatScreen::class, ChatScreen.serializer())
                }
            }
        }
    }
}