plugins {
    id("com.kolts.gamedev.kotlin-application-conventions")
}

dependencies {
    implementation(project(":core"))

    api(libs.gdx.backend.lwjgl3)
    api(libs.gdx.platform) { artifact { classifier = "natives-desktop" } }
    api(libs.gdx.box2d.platform) { artifact { classifier = "natives-desktop" } }
}

application {
    mainClass.set("com.kolts.gamedev.app.DesktopLauncherKt")
}
