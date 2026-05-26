# REST API Contract — Job Recruitment Platform

Source of truth for the **Java Spring Boot** backend implementation. Derived from the frontend service layer (`src/services/*`) and finalized DB schema.

- **Base URL:** `http://localhost:8080/api`
- **Auth:** `Authorization: Bearer <JWT>` (HTTP 401 → frontend redirects to `/login`)
- **Content-Type:** `application/json` (except CV upload → `multipart/form-data`)
- **JSON convention:** camelCase
- **Roles:** `ADMIN`, `CANDIDATE`, `RECRUITER`, `PUBLIC` (no auth)

---

## 1. Common Response Envelope

All endpoints return:

```json
{
  "message": "Success message",
  "data": { },
  "errorCode": null
}
```

On error:

```json
{
  "message": "Invalid credentials",
  "data": null,
  "errorCode": "AUTH_INVALID_CREDENTIALS"
}
```

Suggested HTTP status codes: `200 OK`, `201 Created`, `204 No Content`, `400`, `401`, `403`, `404`, `409`, `422`, `500`.

## 2. Pagination Envelope

For all list endpoints that support paging, `data` is:

```json
{
  "content": [],
  "currentPage": 0,
  "totalPages": 1,
  "totalElements": 0,
  "pageSize": 10
}
```

Standard query params: `page` (0-indexed), `size` (default 10), `sort` (e.g. `createdAt,desc`).

## 3. Enums

| Enum | Values |
|---|---|
| `UserRole` | `CANDIDATE`, `RECRUITER`, `ADMIN` |
| `UserStatus` | `ACTIVE`, `INACTIVE`, `BLOCKED` |
| `Gender` | `MALE`, `FEMALE`, `OTHER` |
| `EmploymentType` | `FULL_TIME`, `PART_TIME`, `INTERNSHIP`, `FREELANCE`, `REMOTE` |
| `JobPostStatus` | `DRAFT`, `OPEN`, `CLOSED`, `HIDDEN` |
| `ApprovalStatus` | `PENDING`, `APPROVED`, `REJECTED` |
| `ApplicationStatus` | `PENDING`, `REVIEWING`, `INTERVIEWED`, `ACCEPTED`, `REJECTED` |
| `SkillLevel` | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` |

---

# Module: Auth (`authApi`)

### POST `/auth/register`
- **Role:** PUBLIC
- **Request body:**
  ```json
  { "email": "user@example.com", "password": "secret", "fullName": "Nguyễn Văn A", "role": "CANDIDATE" }
  ```
- **Response `data`:** `{ "user": User, "token": "jwt..." }`
- **Used by:** `src/pages/Register.tsx`
- **Notes:** Validate unique email. Hash password (BCrypt). Issue JWT. Auto-create empty `candidate_profiles` row when role=`CANDIDATE`.

### POST `/auth/login`
- **Role:** PUBLIC
- **Request body:** `{ "email": "...", "password": "..." }`
- **Response `data`:** `{ "user": User, "token": "jwt..." }`
- **Used by:** `src/pages/Login.tsx`, `AuthContext`
- **Notes:** Reject if `status != ACTIVE` with `errorCode: USER_BLOCKED`.

### GET `/auth/me`
- **Role:** Any authenticated
- **Response `data`:** `User`
- **Used by:** `AuthContext` (session restore)
- **Notes:** Resolve from JWT. Return 401 if invalid/expired.

---

# Module: Public Jobs (`jobApi`)

### GET `/jobs`
- **Role:** PUBLIC
- **Query params:** `keyword`, `location`, `type` (EmploymentType), `level`, `salaryMin`, `industry`, `categoryId`, `companyId`, `featured`, `page`, `size`, `sort`
- **Response `data`:** `Page<Job>` (paginated envelope) — frontend also tolerates a flat `Job[]`
- **Used by:** `src/pages/Jobs.tsx`, `src/pages/Home.tsx`, `src/pages/CompanyDetail.tsx`
- **Notes:** Only return `status=OPEN` AND `approvalStatus=APPROVED`. Hydrate `company`, `category`, `skills` (resolved names from `job_post_skills`).

### GET `/jobs/{id}`
- **Role:** PUBLIC
- **Response `data`:** `Job`
- **Used by:** `src/pages/JobDetail.tsx`
- **Notes:** Increment `viewCount`. Hydrate company + skills + category.

### GET `/jobs/categories`
- **Role:** PUBLIC
- **Response `data`:** `JobCategory[]`
- **Used by:** Filters in Jobs page, Recruiter create job.

---

# Module: Companies (`companyApi`)

### GET `/companies`
- **Role:** PUBLIC
- **Query params:** `page`, `size`, `sort`, `industry?`, `keyword?`
- **Response `data`:** `Page<Company>` or `Company[]`
- **Used by:** `src/pages/Companies.tsx`

### GET `/companies/{id}`
- **Role:** PUBLIC
- **Response `data`:** `Company` (include `jobCount`)
- **Used by:** `src/pages/CompanyDetail.tsx`

---

# Module: Candidate Profile (`candidateApi`)

### GET `/candidate/profile`
- **Role:** CANDIDATE
- **Response `data`:** `CandidateProfile`
- **Used by:** `CandidateProfile.tsx`, `CandidateDashboard.tsx`
- **Notes:** Resolve candidate from JWT (`userId`).

### PUT `/candidate/profile`
- **Role:** CANDIDATE
- **Request body:** `Partial<CandidateProfile>` (fullName, phone, dateOfBirth, gender, address, currentPosition, yearsOfExperience, educationLevel, bio, expectedSalaryMin, expectedSalaryMax, preferredLocation, avatarUrl)
- **Response `data`:** `CandidateProfile`
- **Used by:** `CandidateProfile.tsx`

---

# Module: Candidate CVs (`candidateCvApi`)

### GET `/candidate/cvs`
- **Role:** CANDIDATE
- **Response `data`:** `CV[]`
- **Used by:** `CandidateCV.tsx`, application flow on `JobDetail.tsx`

### POST `/candidate/cvs`
- **Role:** CANDIDATE
- **Request:** `multipart/form-data` — fields: `file` (binary), `title` (string)
- **Response `data`:** `CV` (with `id`, `originalFileName`, `fileUrl`, `fileSize` in bytes, `fileType`, `isDefault=false`, `createdAt`)
- **Used by:** `CandidateCV.tsx`
- **Notes:** Store file in S3 / local disk. Limit size (e.g. 10 MB). Allow PDF/DOC/DOCX.

### PATCH `/candidate/cvs/{id}/default`
- **Role:** CANDIDATE
- **Response `data`:** `null`
- **Notes:** Set this CV `isDefault=true`, unset others for same candidate (transactional).

### DELETE `/candidate/cvs/{id}`
- **Role:** CANDIDATE
- **Response:** `204 No Content` (envelope with `data: null`)
- **Notes:** Reject if CV is referenced by an existing application (or soft-delete).

---

# Module: Applications (`applicationApi`)

### POST `/candidate/applications`
- **Role:** CANDIDATE
- **Request body:** `{ "jobPostId": "j1", "cvId": "cv1", "coverLetter": "..." }`
- **Response `data`:** `Application`
- **Used by:** `JobDetail.tsx`
- **Notes:** Reject duplicates `(candidateId, jobPostId)` with `409`. Default `status=PENDING`. Create notification for recruiter.

### GET `/candidate/applications`
- **Role:** CANDIDATE
- **Query params:** `status?`, `page?`, `size?`
- **Response `data`:** `Application[]` (hydrate `job` + `company`)
- **Used by:** `CandidateApplications.tsx`

### GET `/recruiter/applications`
- **Role:** RECRUITER
- **Query params:** `jobPostId?`, `status?`, `page?`, `size?`
- **Response `data`:** `Application[]` (hydrate `candidate` + `cv` + `job`)
- **Used by:** `RecruiterApplications.tsx`
- **Notes:** Scope to jobs owned by current recruiter's company.

### PATCH `/recruiter/applications/{id}/status`
- **Role:** RECRUITER
- **Request body:** `{ "status": "REVIEWING" | "INTERVIEWED" | "ACCEPTED" | "REJECTED" }`
- **Response `data`:** `null`
- **Used by:** `RecruiterApplications.tsx`
- **Notes:** Validate ownership. Emit notification to candidate.

---

# Module: Saved Jobs (`savedJobApi`)

### GET `/candidate/saved-jobs`
- **Role:** CANDIDATE
- **Response `data`:** `Job[]`
- **Used by:** `CandidateSaved.tsx`

### POST `/candidate/saved-jobs/{jobPostId}`
- **Role:** CANDIDATE
- **Response `data`:** `null`
- **Notes:** Idempotent (no error if already saved).

### DELETE `/candidate/saved-jobs/{jobPostId}`
- **Role:** CANDIDATE
- **Response `data`:** `null`

---

# Module: Job Recommendations (`recommendationApi`)

### GET `/candidate/recommendations`
- **Role:** CANDIDATE
- **Query params:** `limit?` (default 10)
- **Response `data`:** `JobRecommendation[]` (each contains `score`, `reason`, hydrated `job`)
- **Used by:** `CandidateRecommendations.tsx`, `CandidateDashboard.tsx`
- **Notes:** Match candidate skills/experience/preferred location/salary against `job_posts`. Persist results in `job_recommendations` (recompute nightly or on profile update).

---

# Module: Recruiter (`recruiterApi`)

### GET `/recruiter/company`
- **Role:** RECRUITER
- **Response `data`:** `Company`
- **Used by:** `RecruiterCompany.tsx`, `RecruiterDashboard.tsx`

### PUT `/recruiter/company`
- **Role:** RECRUITER
- **Request body:** `Partial<Company>`
- **Response `data`:** `Company`
- **Used by:** `RecruiterCompany.tsx`

### GET `/recruiter/jobs`
- **Role:** RECRUITER
- **Query params:** `status?`, `page?`, `size?`
- **Response `data`:** `Job[]` (or paged)
- **Used by:** `RecruiterJobs.tsx`, `RecruiterDashboard.tsx`

### GET `/recruiter/jobs/{id}`
- **Role:** RECRUITER
- **Response `data`:** `Job`

### POST `/recruiter/jobs`
- **Role:** RECRUITER
- **Request body:**
  ```json
  {
    "title": "Senior Java Developer",
    "categoryId": "cat1",
    "description": "...",
    "requirements": ["..."],
    "benefits": ["..."],
    "location": "Hà Nội",
    "employmentType": "FULL_TIME",
    "experienceLevel": "Senior",
    "salaryMin": 20000000,
    "salaryMax": 40000000,
    "quantity": 2,
    "deadline": "2026-12-31T00:00:00Z",
    "skills": ["Java", "Spring Boot"]
  }
  ```
- **Response `data`:** `Job` (created with `status=OPEN`, `approvalStatus=PENDING`)
- **Used by:** `RecruiterCreateJob.tsx`
- **Notes:** Persist `job_post_skills`. `companyId` resolved from current recruiter.

### PUT `/recruiter/jobs/{id}`
- **Role:** RECRUITER
- **Request body:** `Partial<Job>`
- **Response `data`:** `Job`
- **Notes:** Validate ownership. If significant changes, optionally reset `approvalStatus=PENDING`.

### DELETE `/recruiter/jobs/{id}`
- **Role:** RECRUITER
- **Response `data`:** `null`
- **Notes:** Soft delete recommended (set `status=HIDDEN`) to preserve applications.

---

# Module: Admin (`adminApi`)

### GET `/admin/users`
- **Role:** ADMIN
- **Query params:** `role?`, `status?`, `keyword?`, `page?`, `size?`
- **Response `data`:** `User[]` (or paged)
- **Used by:** `AdminUsers.tsx`

### PATCH `/admin/users/{id}/status`
- **Role:** ADMIN
- **Request body:** `{ "status": "ACTIVE" | "INACTIVE" | "BLOCKED" }`
- **Response `data`:** `null`
- **Used by:** `AdminUsers.tsx`

### GET `/admin/jobs`
- **Role:** ADMIN
- **Query params:** `approvalStatus?`, `status?`, `keyword?`, `page?`, `size?`
- **Response `data`:** `Job[]` (or paged) — include all statuses
- **Used by:** `AdminJobs.tsx`

### PATCH `/admin/jobs/{id}/approval`
- **Role:** ADMIN
- **Request body:** `{ "approvalStatus": "APPROVED" | "REJECTED", "reason"?: "..." }`
- **Response `data`:** `null`
- **Used by:** `AdminJobs.tsx`
- **Notes:** Notify the recruiter. Only `APPROVED` jobs are visible publicly.

### GET `/admin/companies`
- **Role:** ADMIN
- **Query params:** `keyword?`, `page?`, `size?`
- **Response `data`:** `Company[]`
- **Used by:** `AdminCompanies.tsx`

### GET `/admin/analytics`
- **Role:** ADMIN
- **Response `data`:**
  ```json
  {
    "totalUsers": 12480,
    "totalJobs": 320,
    "totalCompanies": 84,
    "totalApplications": 3210,
    "monthly": [{ "month": "T1", "jobs": 120, "applications": 540 }],
    "industries": [{ "name": "CNTT", "value": 420 }]
  }
  ```
- **Used by:** `AdminDashboard.tsx`, `AdminReports.tsx`

---

# 4. DTO Reference (camelCase JSON)

### User
```json
{
  "id": "u1", "email": "a@b.com", "role": "CANDIDATE", "status": "ACTIVE",
  "fullName": "Nguyễn Văn A", "avatar": "https://...", "phone": "0901234567",
  "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-02T00:00:00Z"
}
```

### CandidateProfile
```json
{
  "id": "cp1", "userId": "u1", "fullName": "Nguyễn Văn A", "phone": "0901234567",
  "avatarUrl": "...", "dateOfBirth": "2000-05-10", "gender": "MALE",
  "address": "Hà Nội", "currentPosition": "Java Dev", "yearsOfExperience": 2,
  "educationLevel": "Đại học", "bio": "...", "expectedSalaryMin": 15000000,
  "expectedSalaryMax": 25000000, "preferredLocation": "Hà Nội",
  "createdAt": "...", "updatedAt": "..."
}
```

### Company
```json
{
  "id": "c1", "recruiterId": "u2", "name": "FPT Software", "phone": "...",
  "logoUrl": "...", "bannerUrl": "...", "website": "https://fpt.com",
  "description": "...", "industry": "CNTT", "address": "Hà Nội",
  "companySize": "1000-5000", "jobCount": 15,
  "createdAt": "...", "updatedAt": "..."
}
```

### Job (job_posts)
```json
{
  "id": "j1", "companyId": "c1", "categoryId": "cat1",
  "title": "Senior Java Developer", "description": "...",
  "requirements": ["..."], "benefits": ["..."],
  "location": "Hà Nội", "employmentType": "FULL_TIME",
  "experienceLevel": "Senior", "salaryMin": 20000000, "salaryMax": 40000000,
  "quantity": 2, "deadline": "2026-12-31T00:00:00Z",
  "status": "OPEN", "approvalStatus": "APPROVED", "viewCount": 120,
  "createdAt": "...", "updatedAt": "...",
  "company": { }, "category": { }, "skills": ["Java", "Spring Boot"]
}
```

### CV
```json
{
  "id": "cv1", "candidateId": "u1", "title": "CV Java Developer",
  "originalFileName": "cv.pdf", "fileUrl": "https://...",
  "fileType": "application/pdf", "fileSize": 184320,
  "summary": "...", "isDefault": true,
  "createdAt": "...", "updatedAt": "..."
}
```

### Application
```json
{
  "id": "a1", "jobPostId": "j1", "candidateId": "u1", "cvId": "cv1",
  "coverLetter": "...", "status": "PENDING",
  "appliedAt": "...", "updatedAt": "...",
  "job": { }, "candidate": { }
}
```

### JobRecommendation
```json
{
  "id": "r1", "candidateId": "u1", "jobPostId": "j1",
  "score": 0.92, "reason": "Khớp 5/6 kỹ năng",
  "recommendedAt": "...", "job": { }
}
```

### JobCategory / Skill / Notification / SavedJob
Match the DB schema 1:1 (camelCase fields).

---

# 5. Spring Boot Implementation Notes

- **Security:** Spring Security + JWT filter. Role-based with `@PreAuthorize("hasRole('CANDIDATE')")` etc. Map `UserRole` enum to Spring authorities (`ROLE_CANDIDATE`, ...).
- **Global exception handler:** `@RestControllerAdvice` returning the common `ApiResponse` envelope with `errorCode`.
- **Pagination:** Convert Spring `Page<T>` → custom envelope (`currentPage`, `totalPages`, `totalElements`, `pageSize`, `content`). Provide a `PageMapper` utility.
- **Validation:** `@Valid` + Jakarta Bean Validation on all request DTOs.
- **File upload:** `MultipartFile` for `POST /candidate/cvs`. Store on disk/S3. Persist URL only.
- **Ownership checks:** Every recruiter/candidate-scoped endpoint must verify the resource belongs to the current user (service-layer guard).
- **Approval workflow:** Public `/jobs` must filter `status=OPEN AND approvalStatus=APPROVED`. Admin and recruiter-owner endpoints bypass this filter.
- **Notifications:** On status changes (job approval, application status), insert a row in `notifications` (future endpoints can expose `/notifications`).
- **CORS:** Allow `http://localhost:5173` (Vite) for dev.
- **Date format:** ISO-8601 UTC (`2026-12-31T00:00:00Z`). Use `Instant`/`OffsetDateTime`.
- **Frontend toggle:** Set `VITE_USE_MOCK=false` to point the SPA at this API.
