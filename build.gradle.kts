// my-spring-starter/build.gradle.kts

plugins {
    id("org.springframework.boot") version "3.3.1" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
    java
}

// 모든 하위 프로젝트에 공통으로 적용될 설정입니다.
allprojects {
    group = "com.hiring"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}