plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.70")
}

apply {
    plugin("idea")
    plugin("org.jetbrains.intellij")
    plugin("kotlin")
    plugin("java")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "com.github.syuchan1005"

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    // version = "IC-2019.2.3"
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    version("1.0.2")
    untilBuild(null)
    sinceBuild("173.0")
}
