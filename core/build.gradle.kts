plugins {
    id("com.kolts.gamedev.kotlin-library-conventions")
}

dependencies {
    api(libs.gdx)
    api(libs.bundles.ktx)
}

sourceSets {
    main {
        resources {
            srcDir("$rootDir/assets")
        }
    }
}
