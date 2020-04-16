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
//    compile("io.netty:netty-all:4.1.48.Final")
    compile("org.bouncycastle:bcpkix-jdk15on:1.65")
    compile("org.bidib.jbidib.org.qbang.rxtx:rxtxcomm:2.2")
    compile("com.barchart.udt:barchart-udt-bundle:2.3.0")
    compile("com.google.protobuf:protobuf-java:3.11.4")
    compile("org.jboss.marshalling:jboss-marshalling:2.0.9.Final")
    compile("com.jcraft:jzlib:1.1.3")
    compile("com.github.jponge:lzma-java:1.3")
    compile("com.ning:compress-lzf:1.0.4")
    compile("org.lz4:lz4-java:1.7.1")
    compile("org.eclipse.jetty.alpn:alpn-api:1.1.3.v20160715")
    compile("org.conscrypt:conscrypt-openjdk-uber:2.4.0")
    compile("log4j:log4j:1.2.17")
    compile("org.eclipse.jetty.npn:npn-api:1.1.1.v20141010")
    compile("com.oracle.substratevm:svm:19.2.1")
    compile("io.projectreactor:reactor-core:3.3.4.RELEASE")
    compile("com.fasterxml:aalto-xml:1.2.2")
    compile("io.projectreactor.tools:blockhound:1.0.3.RELEASE")
    compile("com.google.protobuf.nano:protobuf-javanano:3.1.0")
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