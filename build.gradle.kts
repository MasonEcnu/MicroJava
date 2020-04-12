import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.3.31"

    var string_boot_version: String by extra
    string_boot_version = "2.2.6.RELEASE"

    repositories {
        maven {
            url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
        }
//        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

plugins {
    groovy
    java
}

group = "com.mason"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
}

val kotlin_version: String by extra
val string_boot_version: String by extra

repositories {
    maven {
        url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
    }
//        mavenCentral()
}

dependencies {
    compile("org.codehaus.groovy:groovy-all:2.3.11")
    compile("com.google.guava:guava:28.2-jre")
    compile("org.apache.commons:commons-lang3:3.10")
    compile("com.alibaba:fastjson:1.2.68")
    compile("org.springframework.boot:spring-boot-starter:$string_boot_version")
    compile("org.springframework.boot:spring-boot-starter-web:$string_boot_version")
    compile(kotlin("stdlib-jdk8", kotlin_version))
    compile(kotlin("reflect", kotlin_version))
    testCompile("junit", "junit", "4.12")
    testCompile("org.springframework.boot:spring-boot-starter-test:$string_boot_version")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}