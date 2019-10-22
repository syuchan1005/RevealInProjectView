plugins {
    id("org.jetbrains.intellij") version "0.4.10"
    java
}

group = "com.github.syuchan1005"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "IC-2019.2.3"

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    version("1.0.1")
    untilBuild(null)
    sinceBuild("173.0")
}
