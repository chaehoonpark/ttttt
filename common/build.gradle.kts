// common/build.gradle.kts

plugins {
    `java-library`
}

val mapstructVersion = "1.5.5.Final"
val lombokVersion = "1.18.32"

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.springframework.boot:spring-boot-starter-web")

    api("org.mapstruct:mapstruct:$mapstructVersion")
    api("jakarta.validation:jakarta.validation-api:3.0.2")
}
