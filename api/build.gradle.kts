// api/build.gradle.kts

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val querydslDir = "build/generated/querydsl"
val mapstructVersion = "1.5.5.Final"

sourceSets {
    main {
        java {
            srcDir(querydslDir)
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":common"))
    implementation(project(":jpa"))

    runtimeOnly("com.h2database:h2")

    val lombokVersion = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // mapstruct annotation processor
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // Q-Type 생성을 위한 어노테이션 프로세서 설정 (가장 중요)
    annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // fixture-monkey
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.1.14")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-jakarta-validation:1.1.14")
}

// 컴파일 시 QueryDSL 클래스를 생성하도록 작업 설정
tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(file(querydslDir))
}

// clean 작업 시 생성된 디렉토리도 삭제 (권장)
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