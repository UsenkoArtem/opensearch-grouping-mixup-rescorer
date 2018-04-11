import org.elasticsearch.gradle.VersionProperties

buildscript {
    val esVersion = project.file("es.version")
            .readLines()
            .first()

    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.elasticsearch.gradle:build-tools:$esVersion")
    }
}

plugins {
    java
    idea
}

apply {
    plugin("elasticsearch.esplugin")
}

val pluginVersion = project.file("project.version")
        .readLines()
        .first()
        .toUpperCase()
        .let { ver ->
            if (hasProperty("release")) {
                ver.removeSuffix("-SNAPSHOT")
            } else {
                ver
            }
        }

configure<org.elasticsearch.gradle.plugin.PluginPropertiesExtension> {
    name = "rescore-grouping-mixup"
    description = "Adds rescorer for mixing up search hits inside their groups."
    classname = "company.evo.elasticsearch.plugin.GroupingMixupPlugin"
    version = pluginVersion
}

project.setProperty("licenseFile", project.rootProject.file("LICENSE.txt"))
project.setProperty("noticeFile", project.rootProject.file("NOTICE.txt"))

val versions = VersionProperties.getVersions() as Map<String, String>
project.version = "$pluginVersion-es${versions["elasticsearch"]}"