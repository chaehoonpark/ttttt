// api/build.gradle.kts

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val querydslDir = "build/generated/querydsl"

sourceSets {
    main {
        java {
            srcDir(querydslDir)
        }
    }
}

dependencies {
    // === 버전 관리 ===
    val lombokVersion = "1.18.32"
    val mapstructVersion = "1.5.5.Final"
    val querydslVersion = "5.1.0"
    val fixtureMonkeyVersion = "1.1.14"
    val jakartaValidationVersion = "3.0.2"
    val embeddedRedisVersion = "0.6"

    // === Spring Boot & 프로젝트 모듈 ===
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation(project(":jpa")) // 내부 jpa 모듈 의존

    // === 데이터베이스 & 캐시 ===
    runtimeOnly("com.h2database:h2") // H2 데이터베이스
    implementation("com.github.kstyrc:embedded-redis:$embeddedRedisVersion") // 내장 Redis

    // === 유효성 검사 ===
    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidationVersion")

    // === 개발 편의 도구 (Lombok, Mapstruct) ===
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // === QueryDSL Q-Type 생성을 위한 어노테이션 프로세서 ===
    annotationProcessor("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")

    // === 테스트 ===
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa") // 테스트 시 JPA 컨텍스트 구성용
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Fixture Monkey (테스트 객체 생성)
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:$fixtureMonkeyVersion")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-jakarta-validation:$fixtureMonkeyVersion")
}

// 컴파일 시 QueryDSL 클래스를 생성하도록 작업 설정
tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(file(querydslDir))
}

// clean 작업 시 생성된 디렉토리도 삭제
tasks.named<Delete>("clean") {
    delete(file(querydslDir))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel") {}

// 이 모듈만 실행 가능한 jar로 만듭니다.
tasks.bootJar { enabled = true }
tasks.jar { enabled = false }