import java.io.FileInputStream
import java.util.Properties

var keystorePropertiesFile = File("local-env.properties")
var prop = Properties()
prop.load(FileInputStream(keystorePropertiesFile))

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://developer.huawei.com/repo/") }
    }
}
dependencyResolutionManagement {
    println("Root project dir: ${prop.get("TL_uri")}")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven { url = uri("https://developer.huawei.com/repo/") }
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            name = prop.getProperty("TL_name").toString()
            url = uri(prop.getProperty("TL_uri").toString())
            credentials {
                username = prop.getProperty("TL_us").toString()
                password = prop.getProperty("TL_ps").toString()
            }
        }

        maven {//CLONE REPOS CREDENTIALS
            name = prop.getProperty("C_INE_name").toString()
            url =
                uri(prop.getProperty("C_INE_url").toString())
            credentials {
                username = prop.getProperty("C_INE_us").toString()
                password =
                    prop.getProperty("C_INE_ps").toString()
            }
        }

        maven {//CLONE REPOS CREDENTIALS
            name = prop.get("SDK_PAN_name")?.toString() ?: ""
            url = uri(prop.get("SDK_PAN_uri")?.toString() ?: "")
            credentials {
                username = prop.get("SDK_PAN_us")?.toString() ?: ""
                password = prop.get("SDK_PAN_ps")?.toString() ?: ""
            }
        }
    }
}

rootProject.name = "Pan"
include(":launchsdk")
include(":sdkPan")
