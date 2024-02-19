plugins {
    id("com.kolts.gamedev.kotlin-library-conventions")
}

dependencies {
    api(libs.bundles.gdx.core)
    api(libs.bundles.ktx)
}

sourceSets {
    main {
        resources {
            srcDir("$rootDir/assets")
        }
    }
}
