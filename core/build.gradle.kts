import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvm()
    androidLibrary {
        namespace = "shub39.kovert.core"
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt())
        }
        minSdk {
            version = release(libs.versions.android.minSdk.get().toInt())
        }
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.material3)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.koog)
            implementation(libs.kotlinx.datetime)
            implementation(libs.hypnoticcanvas)
            implementation(libs.materialkolor)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
    }
}

buildkonfig {
    packageName = "shub39.kovert"

    val properties = Properties().apply {
        load(project.rootProject.file("local.properties").reader())
    }
    val apiKey: String = properties.getProperty("apiUrl")

    require(apiKey.isNotEmpty()) {
        "Register your api key from developer and place it in local.properties as `apiUrl`"
    }

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "OLLAMA_API_URL",
            apiKey
        )
    }
}