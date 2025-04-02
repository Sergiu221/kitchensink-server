plugins {
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
}

group = "kitchensink"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Spring Boot DevTools
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Hibernate Validator
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    // Jackson Dataformat XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1")

    //JoinFaces basic starter

    //PrimeFaces
    implementation("org.joinfaces:primefaces-spring-boot-starter:5.4.0")
    // Testcontainers core dependency
    testImplementation("org.testcontainers:testcontainers:1.19.0")

    // JUnit 5 Integration for Testcontainers
    testImplementation("org.testcontainers:junit-jupiter:1.19.0")

    // Testcontainers MariaDB Module
    testImplementation("org.testcontainers:mongodb:1.19.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}