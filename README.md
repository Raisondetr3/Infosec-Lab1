# Информационная безопасность. Лабораторная работа 1

## Описание проекта

Этот проект демонстрирует создание безопасного backend-приложения с помощью Java + Spring Boot, который включает:
- аутентификацию пользователей
- управление постами
- Защиту от таких атак, как SQL-инъекции, XSS-атаки, Broken Authentication

## API Endpoints

### POST `/auth/login`
Аутентификация пользователя.

**URL:** `http://localhost:8080/auth/login`

**Метод:** POST

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "testuser",
  "password": "SecurePass123!"
}
```

**Успешный ответ (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expirationTime": 1736766123456,
  "user": {
    "id": 1,
    "username": "testuser"
  }
}
```

**Пример вызова (curl):**
```bash
curl -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "SecurePass123!"
  }'
```

### 2. GET `/api/posts` - Получение всех постов
**URL:** `http://localhost:8080/api/data`

**Метод:** GET

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Успешный ответ (200 OK):**
```json
{
  "posts": [
    {
      "id": 1,
      "title": "Welcome to Secure API",
      "content": "This is a welcome post to demonstrate our secure API functionality...",
      "authorUsername": "testuser",
      "authorId": 1
    },
    {
      "id": 2,
      "title": "Spring Boot Security Guide",
      "content": "Key security practices: JWT authentication, bcrypt password hashing...",
      "authorUsername": "admin",
      "authorId": 2
    }
  ],
  "count": 15,
  "message": "Posts retrieved successfully"
}
```

**Пример вызова (curl):**
```bash
curl -X GET "http://localhost:8080/api/data" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json"
```

### 3. DELETE `/api/auth/logout` - Выход
**URL:** `http://localhost:8080/auth/logout`

**Метод:** DELETE

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Успешный ответ (200 OK):**
```json
{
  "message": "Successfully logged out",
  "timestamp": "1736766123456"
}
```

**Пример вызова (curl):**
```bash
curl -X DELETE "http://localhost:8080/auth/logout" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json"
```

## Реализованные меры защиты

### 1. Защита от SQL-инъекций

**Методы защиты:**
- **Параметризованные запросы**: Использование Spring Data JPA с автоматическим экранированием параметров
- **ORM**: Hibernate автоматически предотвращает SQL-инъекции

### 2. Защита от XSS

**Методы защиты:**
- **HTML экранирование**: `HtmlUtils.htmlEscape()` для всех пользовательских данных

### 3. Защита от нарушений аутентификации

**JWT (JSON Web Tokens) реализация:**

**Методы защиты:**
- **Хеширование паролей**: BCrypt
- **Безопасные JWT токены**: HMAC SHA-256 подпись

### 4. Контроль доступа

**Методы защиты:**
- **JWT токены**: Обязательная аутентификация для защищенных endpoint'ов
- - **Method-level security**: через цепочку фильтров

## Отчёты SAST/SCA

![](screenshots/SAST.png)
![](screenshots/Sync.png)


