import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.3.72"

    var spring_boot_version: String by extra
    spring_boot_version = "2.2.6.RELEASE"

    var spring_version: String by extra
    spring_version = "5.2.5.RELEASE"

    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
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
val spring_boot_version: String by extra
val spring_version: String by extra

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
//        mavenCentral()
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:2.3.11")
    implementation("com.google.guava:guava:28.2-jre")
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("com.alibaba:fastjson:1.2.68")
//    implementation("io.netty:netty-all:4.1.48.Final")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.65")
    implementation("org.bidib.jbidib.org.qbang.rxtx:rxtxcomm:2.2")
    implementation("com.barchart.udt:barchart-udt-bundle:2.3.0")
    implementation("com.google.protobuf:protobuf-java:3.11.4")
    implementation("org.jboss.marshalling:jboss-marshalling:2.0.9.Final")
    implementation("com.jcraft:jzlib:1.1.3")
    implementation("com.github.jponge:lzma-java:1.3")
    implementation("com.ning:compress-lzf:1.0.4")
    implementation("org.lz4:lz4-java:1.7.1")
    implementation("org.eclipse.jetty.alpn:alpn-api:1.1.3.v20160715")
    implementation("org.conscrypt:conscrypt-openjdk-uber:2.4.0")
    implementation("log4j:log4j:1.2.17")
    implementation("org.eclipse.jetty.npn:npn-api:1.1.1.v20141010")
    implementation("com.oracle.substratevm:svm:19.2.1")
    implementation("io.projectreactor:reactor-core:3.3.4.RELEASE")
    implementation("com.fasterxml:aalto-xml:1.2.2")
    implementation("io.projectreactor.tools:blockhound:1.0.3.RELEASE")
    implementation("com.google.protobuf.nano:protobuf-javanano:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter:$spring_boot_version")
    implementation("org.springframework.boot:spring-boot-starter-web:$spring_boot_version")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:$spring_boot_version")
    implementation("org.springframework.integration:spring-integration-redis:$spring_version")
    implementation(kotlin("stdlib-jdk8", kotlin_version))
    implementation(kotlin("reflect", kotlin_version))
    testImplementation("junit", "junit", "4.12")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$spring_boot_version")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.withType<JavaCompile> {
    options.isFork = true
    options.encoding = "UTF-8"
    options.forkOptions.executable = "javac"
    options.compilerArgs.addAll(listOf("-XDignore.symbol.file"))
}