# My Spring Modules

이 프로젝트는 자주 사용하는 Spring Boot 라이브러리들을 모듈화하여 관리하는 개인용 스타터 킷입니다. <br>
새로운 프로젝트를 시작할 때 이 저장소를 클론하여 `settings.gradle.kts`에 추가하면, 필요한 의존성을 빠르고 쉽게 가져올 수 있습니다.

## 📚 모듈 구성

* **`common`**: Mapstruct, Jakarta Validation, Lombok, Fixture Monkey 등 범용적인 유틸리티 라이브러리를 포함합니다.
* **`jpa`**: Spring Data JPA, QueryDSL 등 데이터 접근 계층에 필요한 라이브러리를 포함합니다.

## 🚀 사용 방법

새로운 Spring Boot 프로젝트에서 이 모듈들을 사용하려면 아래 단계를 따르세요.

### 1. 프로젝트 클론

먼저, 여러분의 메인 프로젝트 루트 디렉토리에서 이 저장소를 `git clone` 받습니다.

```bash
git clone https://github.com/xpmxf4/my-spring-starter.git
```

클론이 완료되면 프로젝트 구조는 아래와 같이 됩니다.

```
your-main-project/
├── my-spring-modules/  <-- 클론된 이 프로젝트
│   ├── common/
│   └── jpa/
├── src/
├── build.gradle.kts
└── settings.gradle.kts
```

### 2. `settings.gradle.kts` 설정

이제 여러분의 메인 프로젝트의 `settings.gradle.kts` 파일을 열고, `includeBuild`를 사용하여 클론한 프로젝트를 빌드에 포함시킵니다.

```kotlin
// your-main-project/settings.gradle.kts

// ... 기존 설정 ...

// 아래 코드를 파일 하단에 추가합니다.
includeBuild("my-spring-starter")
```

> **💡 Tip:** `my-spring-starter`는 클론된 폴더의 이름입니다. 만약 다른 이름으로 클론했다면 해당 이름을 사용해야 합니다.

### 3. `build.gradle.kts`에 의존성 추가

마지막으로, 메인 프로젝트의 `build.gradle.kts` 파일에 필요한 모듈을 `implementation`으로 추가합니다.

```kotlin
// your-main-project/build.gradle.kts

dependencies {
    // ... 다른 의존성들 ...

    // 필요한 모듈을 추가합니다.
    implementation(project(":common"))
    implementation(project(":jpa"))
}
```

이제 IDE에서 Gradle 설정을 새로고침하면, `common`과 `jpa` 모듈의 모든 라이브러리를 여러분의 메인 프로젝트에서 바로 사용할 수 있습니다.
# testt
# hiring
