plugins {
    alias(libs.plugins.android.fusedlibrary)
    `maven-publish`
}

@Suppress("UnstableApiUsage")
androidFusedLibrary {
    namespace = "com.github.sceneren.ttvod"
    minSdk = 24

    // If aarMetadata is not explicitly specified,
    // aar metadata will be generated based on dependencies.
    aarMetadata {
        minCompileSdk = 24
        minCompileSdkExtension = 1
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.sceneren"
            artifactId = "ttVod"
            version = "0.0.3"
            from(components["fusedLibraryComponent"])
        }
    }
    repositories {
        maven {
            name = "myLocalRepo"
            url = uri(layout.buildDirectory.dir("myLocalRepo"))
        }
    }
}


dependencies {
    include(project(":vod-playerkit:vod-player"))
    include(project(":vod-playerkit:vod-player-utils"))
    include(project(":vod-playerkit:vod-player-volcengine"))
}