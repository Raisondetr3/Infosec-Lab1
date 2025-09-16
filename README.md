# Информационная безопасность. Лабораторная работа 1

## Описание проекта

Этот проект демонстрирует создание безопасного backend-приложения с помощью Java + Spring Boot, который включает:
- аутентификацию пользователей
- управление постами
- Защиту от таких атак, как SQL-инъекции, XSS-атаки, Broken Authentication

## API Endpoints

### Аутентификация

#### POST `/api/auth/register`
Регистрация нового пользователя.

**Request Body:**
```json
{
  "username": "newuser",
  "password": "SecurePass123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expirationTime": 1736766123456,
  "user": {
    "id": 1,
    "username": "newuser"
  }
}
```

#### POST `/api/auth/login`
Аутентификация пользователя.

**Request Body:**
```json
{
  "username": "testuser",
  "password": "TestPass123!"
}
```

**Response (200 OK):**
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

#### PUT `/api/auth/profile`
Обновление профиля пользователя (требует аутентификации).

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Request Body:**
```json
{
  "username": "newusername",
  "newPassword": "NewSecurePass123!",
  "currentPassword": "TestPass123!"
}
```

**Response (200 OK):**
```json
{
  "user": {
    "id": 1,
    "username": "newusername"
  },
  "message": "Profile updated successfully"
}
```

### Управление постами

#### GET `/api/posts`
Получение всех постов (требует аутентификации).

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response (200 OK):**
```json
{
  "posts": [
    {
      "id": 1,
      "title": "Welcome to Secure API",
      "content": "This is a welcome post...",
      "authorUsername": "testuser",
      "authorId": 1
    }
  ],
  "count": 15,
  "message": "Posts retrieved successfully"
}
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
- **Проверка силы паролей**: Минимум 8 символов + сложность
- **Защита от Timing Attacks**: Одинаковое время выполнения

### 4. Контроль доступа

**Методы защиты:**
- **JWT токены**: Обязательная аутентификация для защищенных endpoint'ов
- - **Method-level security**: через цепочку фильтров

## Отчёт SAST

##


