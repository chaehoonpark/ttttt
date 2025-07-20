# 📖 채용 정보 서비스 API

이 프로젝트는 **기업의 채용 공고**를 등록, 조회, 수정, 삭제할 수 있는 **RESTful API**를 제공합니다.  
**Spring Boot**를 기반으로 구축되었으며, 주요 기술 스택은 다음과 같습니다.

---

## 🔧 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.3.1
- **데이터베이스**:
   - H2 (개발용)
   - MySQL (운영용)
- **주요 라이브러리**:
   - Spring Data JPA & QueryDSL
   - Spring Web
   - Spring Boot Starter Test
   - Lombok, MapStruct
   - Fixture Monkey (테스트 객체 생성)
   - Redis (캐싱)

---

## ✨ 주요 기능

- **회사 관리**: 회사 정보 등록, 수정, 삭제, 조회 기능 제공
- **채용공고 관리**: 채용공고 등록, 수정, 삭제, 조회 기능 제공
- **검색 및 필터링**: 키워드, 지역, 고용 형태 등 다양한 조건으로 검색 및 필터링 가능
- **캐싱**: 반복 요청에 대한 응답 속도 향상을 위해 Redis 기반 캐싱 적용

---

## 📁 프로젝트 구조

```
├── api        // API 모듈 (Controller, Service, Repository 등)
├── common     // 공통 모듈 (도메인 객체, 응답 DTO 등)
├── jpa        // JPA 관련 설정 및 Entity 모듈
└── build.gradle.kts
```

---

## 🚀 실행 방법

### 1. 사전 준비
- Java 21 설치
- IDE (IntelliJ, Eclipse 등)
- (선택) Redis 서버 실행 (캐싱 기능 사용 시)

### 2. 프로젝트 클론
```bash
git clone {저장소_URL}
cd {프로젝트_폴더}
```

### 3. 프로젝트 빌드 및 실행

#### IDE에서 실행
- `api` 모듈의 `ApiApplication.java`의 `main` 메서드를 실행

#### Gradle로 실행
```bash
./gradlew build
java -jar api/build/libs/api-1.0.0-SNAPSHOT.jar
```

### 4. 애플리케이션 접속
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`

---

## 📋 API 명세서

---

### 🏢 회사 (Company)

| 기능       | HTTP Method | URL                                | 설명                                    |
|------------|-------------|------------------------------------|-----------------------------------------|
| 회사 등록  | POST        | `/api/v1/companies`                | 새로운 회사를 등록합니다.               |
| 회사 수정  | PATCH       | `/api/v1/companies/{companyId}`    | 기존 회사의 정보를 수정합니다.          |
| 회사 삭제  | DELETE      | `/api/v1/companies/{companyId}`    | 회사를 삭제합니다.                      |
| 단건 조회  | GET         | `/api/v1/companies/{companyId}`    | 특정 회사의 상세 정보를 조회합니다.     |
| 목록 조회  | GET         | `/api/v1/companies`                | 회사 목록을 검색 조건에 따라 조회합니다. |

#### DTO (Data Transfer Objects)

**1. 회사 등록: `RegisterCompanyApiRequest` (POST /api/v1/companies)**

| 필드명       | 타입     | 설명        | 필수 여부 |
|--------------|----------|-------------|-----------|
| name         | String   | 회사명      | Y         |
| industry     | String   | 산업군      | Y         |
| description  | String   | 회사 설명   | N         |
| employeeCount| String   | 직원 수     | N         |
| foundedYear  | Integer  | 설립 연도   | Y         |
| logoUrl      | String   | 로고 URL    | N         |
| websiteUrl   | String   | 웹사이트 URL| N         |
| address      | String   | 주소        | Y         |

**2. 회사 수정: `ModifyCompanyApiRequest` (PATCH /api/v1/companies/{companyId})**  
`RegisterCompanyApiRequest`와 동일하며, 수정할 필드만 포함 가능.

**3. 회사 목록 조회: `LoadCompaniesApiRequest` (GET /api/v1/companies)**

| Query Parameter | 타입    | 설명                          | 필수 여부 | 기본값 |
|-----------------|---------|-------------------------------|-----------|--------|
| keyword         | String  | 회사명 또는 산업군 검색 키워드 | N         | -      |
| page            | Integer | 페이지 번호                   | N         | 0      |
| size            | Integer | 페이지 당 항목 수              | N         | 10     |

---

### 💼 채용공고 (Job)

| 기능         | HTTP Method | URL                          | 설명                                    |
|--------------|-------------|------------------------------|-----------------------------------------|
| 채용공고 등록| POST        | `/api/v1/jobs`               | 새로운 채용공고를 등록합니다.           |
| 채용공고 수정| PATCH       | `/api/v1/jobs/{jobId}`       | 기존 채용공고의 정보를 수정합니다.      |
| 채용공고 삭제| DELETE      | `/api/v1/jobs/{jobId}`       | 채용공고를 삭제합니다.                  |
| 단건 조회    | GET         | `/api/v1/jobs/{jobId}`       | 특정 채용공고의 상세 정보를 조회합니다. |
| 목록 조회    | GET         | `/api/v1/jobs`               | 채용공고 목록을 검색 조건에 따라 조회.  |

#### DTO (Data Transfer Objects)

**1. 채용공고 등록: `RegisterJobApiRequest` (POST /api/v1/jobs)**

| 필드명         | 타입           | 설명                                                | 필수 여부 |
|----------------|----------------|-----------------------------------------------------|-----------|
| companyId      | Long           | 회사 ID                                            | Y         |
| title          | String         | 공고 제목                                          | Y         |
| description    | String         | 공고 설명                                          | N         |
| employmentType | EmploymentType | 고용 형태 (FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP) | Y    |
| experienceLevel| ExperienceLevel| 경력 수준 (ENTRY, JUNIOR, MID_LEVEL, SENIOR, EXPERT)| Y         |
| educationLevel | EducationLevel | 학력 수준 (HIGH_SCHOOL, ASSOCIATE, BACHELOR, MASTER, DOCTORATE) | Y |
| salaryMin      | Integer        | 최소 급여                                          | N         |
| salaryMax      | Integer        | 최대 급여                                          | N         |
| city           | CityEnum       | 근무 도시 (Enum)                                   | Y         |
| district       | DistrictEnum   | 근무 지역 (Enum)                                   | Y         |
| deadline       | LocalDateTime  | 마감일 (예: 2025-12-31T23:59:59)                   | Y         |
| postedAt       | LocalDateTime  | 등록일 (예: 2025-07-21T10:00:00)                   | Y         |
| requirements   | String         | 요구사항                                          | N         |
| benefits       | String         | 복리후생                                          | N         |

**2. 채용공고 수정: `ModifyJobApiRequest` (PATCH /api/v1/jobs/{jobId})**  
`RegisterJobApiRequest`와 동일하며, 수정하려는 필드만 포함 가능.

**3. 채용공고 목록 조회: `LoadJobsApiRequest` (GET /api/v1/jobs)**

| Query Parameter | 타입           | 설명              | 필수 여부 | 기본값 |
|-----------------|----------------|-------------------|-----------|--------|
| keyword         | String         | 제목/내용 검색 키워드 | N       | -      |
| city            | CityEnum       | 근무 도시 (Enum)  | N         | -      |
| district        | DistrictEnum   | 근무 지역 (Enum)  | N         | -      |
| employmentType  | EmploymentType | 고용 형태 (Enum)  | N         | -      |
| experienceLevel | ExperienceLevel| 경력 수준 (Enum)  | N         | -      |
| educationLevel  | EducationLevel | 학력 수준 (Enum)  | N         | -      |
| page            | Integer        | 페이지 번호       | N         | 0      |
| size            | Integer        | 페이지 당 항목 수 | N         | 10     |

---
