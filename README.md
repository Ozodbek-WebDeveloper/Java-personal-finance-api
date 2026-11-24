# Personal Finance API

Spring Boot va PostgreSQL asosidagi shaxsiy moliya backend API. Foydalanuvchilar ro'yxatdan o'tish, login, kategoriyalar va tranzaksiyalar bilan ishlashi hamda dashboard statistikasi bilan tanishishi mumkin.

## Texnologiyalar

- Java 25
- Spring Boot 4.1.2
- Spring Web
- Spring Security va JWT
- Spring Data JPA
- PostgreSQL
- Liquibase
- Springdoc OpenAPI / Swagger UI
- Maven

## Asosiy imkoniyatlar

- Ro'yxatdan o'tish va login
- Access va refresh token bilan autentifikatsiya
- Refresh tokenni qayta almashtirish va eskisini bekor qilish
- Daromad va xarajat kategoriyalarini boshqarish
- Tranzaksiyalarni qo'shish, ko'rish, tahrirlash va o'chirish
- Kun/davr bo'yicha dashboard summary
- Kategoriya bo'yicha chiqimlar statistikasi
- Oylik va oxirgi tranzaksiyalarni ko'rish

## Talablar

- JDK 25
- Maven (bundled Maven Wrapper bilan ishga tushsa bo'ladi)
- PostgreSQL 5432 yoki mos port
- `JWT_SECRET` environment variable

## Ma'lumotlar bazasini tayyorlash

PostgreSQL ichida bitta baza yarating:

```sql
CREATE DATABASE personal_finance;
```

`src/main/resources/application.properties` bo'yicha default ulanish:

```properties
jdbc:postgresql://localhost:5433/personal_finance
username=postgres
password=admin
```

DB sozlamalari mos kelmasa `application.properties` ichida ularni o'zgartiring. Dastur ishga tushganda Liquibase migrationlarni avtomatik bajaradi.

## JWT secretni sozlash

Windows PowerShell:

```powershell
$env:JWT_SECRET="uzun-va-tasodifiy-xavfsiz-secret"
```

Linux/macOS:

```bash
export JWT_SECRET="uzun-va-tasodifiy-xavfsiz-secret"
```

`application.yml` ichidagi `jwt.secret` `${JWT_SECRET}` orqali yuklanadi.

## Loyihani ishga tushirish

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

API default manzil:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI hujjati:

```text
http://localhost:8080/v3/api-docs
```

## Autentifikatsiya

`POST /api/v1/auth/register` va `POST /api/v1/auth/login` javobidagi `accessToken`ni himoyalangan endpointlarda headerda yuboring:

```http
Authorization: Bearer <accessToken>
```

Register:

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "firstName": "Ali",
  "phone": "+998901234567",
  "password": "secret123"
}
```

Login:

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "phone": "+998901234567",
  "password": "secret123"
}
```

Login javobida `accessToken` va `refreshToken` qaytadi.

Refresh:

```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "<refreshToken>"
}
```

Muvaffaqiyatli javob yangi `accessToken` va yangi `refreshToken` qaytaradi. Ishlatilgan refresh token bekor qilinadi, shuning uchun endpointga har safar eng so'nggi refresh token yuborilishi kerak.

## API endpointlari

### Autentifikatsiya

| Method | Endpoint | Auth | Tavsif |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Yo'q | Foydalanuvchi yaratish |
| POST | `/api/v1/auth/login` | Yo'q | Login qilish |
| POST | `/api/v1/auth/refresh` | Yo'q | Tokenlarni yangilash |
| GET | `/api/v1/auth/me` | Ha | Joriy foydalanuvchi |

### Kategoriyalar

| Method | Endpoint | Body/query | Tavsif |
|---|---|---|---|
| POST | `/api/v1/category` | Category body | Kategoriya yaratish |
| GET | `/api/v1/category` | `?type=INCOME` yoki `?type=EXPENSE` | Kategoriyalar ro'yxati |
| GET | `/api/v1/category/{id}` | Path id | Bitta kategoriya |
| PUT | `/api/v1/category/{id}` | Category body | Kategoriyani tahrirlash |
| DELETE | `/api/v1/category/{id}` | Path id | Kategoriyani o'chirish |

Category body namunasi:

```json
{
  "name": "Oziq-ovqat",
  "type": "EXPENSE"
}
```

### Tranzaksiyalar

| Method | Endpoint | Tavsif |
|---|---|---|
| POST | `/api/v1/transaction` | Tranzaksiya yaratish |
| GET | `/api/v1/transaction` | Barcha tranzaksiyalar |
| GET | `/api/v1/transaction/{id}` | Bitta tranzaksiya |
| PUT | `/api/v1/transaction/{id}` | Tranzaksiyani tahrirlash |
| DELETE | `/api/v1/transaction/{id}` | Tranzaksiyani o'chirish |
| GET | `/api/v1/category/{id}/transactions` | Kategoriyaga tegishli tranzaksiyalar |

Transaction body namunasi:

```json
{
  "title": "Bozor xaridi",
  "amount": 125000,
  "transactionDate": "2026-11-24",
  "type": "EXPENSE",
  "categoryId": 1
}
```

### Dashboard

| Method | Endpoint | Query parametrlari | Tavsif |
|---|---|---|---|
| GET | `/api/v1/dashboard/summary` | `from=2026-11-01&to=2026-11-30` | Daromad va xarajat summary |
| GET | `/api/v1/dashboard/category-summary` | `type=EXPENSE&from=...&to=...` | Kategoriyalar bo'yicha summa |
| GET | `/api/v1/dashboard/monthly` | `month=2026-11` | Bir oylik tranzaksiyalar |
| GET | `/api/v1/dashboard/recent-transactions` | Yo'q | Oxirgi tranzaksiyalar |

## Enum qiymatlar

`TransactionType`:

- `INCOME`
- `EXPENSE`

## Test

```powershell
.\mvnw.cmd test
```

Yoki Linux/macOS:

```bash
./mvnw test
```

## Xatoliklar formati

Validation xatoliklarida JSON maydon nomlari bilan xabarlar qaytadi:

```json
{
  "refreshToken": "refreshToken majburiy!"
}
```

Autentifikatsiya yoki resource xatoliklari `401`, `404`, `409` kabi HTTP statuslar orqali qaytadi.
