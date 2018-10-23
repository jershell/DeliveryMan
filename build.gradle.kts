import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

buildscript {
    repositories {
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.71")
        classpath("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:0.6.2")
        classpath("com.github.jengelman.gradle.plugins:shadow:4.0.1")
    }
}

plugins {
    id("com.github.johnrengelman.shadow") version "2.0.2"

    application
    kotlin("jvm") version "1.2.71"
}

apply(plugin = "kotlin")
apply(plugin = "kotlinx-serialization")
apply(plugin = "com.github.johnrengelman.shadow")


group = "cadia.tech"
version = "0.0.1"

//—————————————————————————————————————————————————————————————————————————————————————————————————
// SOURCE SETS.
//—————————————————————————————————————————————————————————————————————————————————————————————————
kotlin.sourceSets {
    val srcAppPath = File("${project.rootDir}/src/app")
    val srcResourcesPath = File("${project.rootDir}/src/app/resources")
    val newSourceSets = kotlin.sourceSets["main"].kotlin.srcDirs
    val newResourceSets = kotlin.sourceSets["main"].resources.srcDirs

    newSourceSets.add(srcAppPath)
    newResourceSets.add(srcResourcesPath)

    kotlin.sourceSets["main"]
            .kotlin
            .setSrcDirs(newSourceSets)


    kotlin.sourceSets["main"]
            .resources
            .setSrcDirs(newResourceSets)
}

application {
    mainClassName = "tech.cadia.MainKt"
}

repositories {
    maven(url = "https://kotlin.bintray.com/kotlinx")
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib", "1.2.71"))
    compile("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", "0.6.2")
    compile("io.vertx", "vertx-core", "3.5.4")
    compile("io.vertx", "vertx-lang-kotlin", "3.5.4")
    compile("io.vertx", "vertx-mail-client", "3.5.4")
    compile("io.vertx", "vertx-web", "3.5.4")
    compile("io.github.microutils","kotlin-logging", "1.6.10")
    compile("org.slf4j", "slf4j-simple", "1.7.25")
    compile("org.jetbrains.kotlinx","kotlinx-html-jvm","0.6.11")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    baseName = "app"
    classifier = ""
    version = ""
}

tasks.create("hello") {
    doFirst {
        println("foo")
        println(kotlin.sourceSets["main"].resources.srcDirs)
        println(kotlin.sourceSets["main"].kotlin.srcDirs)
    }
    doLast {
        println("bar")
    }
}

