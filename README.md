# Образовательная платформа

Приложение на Spring Boot для образовательной платформы с базой данных PostgreSQL, развертыванием в Kubernetes и полным CI/CD пайплайном.

## Описание проекта

Образовательная платформа — это комплексная система управления обучением, которая позволяет преподавателям создавать и управлять курсами, студентам записываться на курсы и предоставляет инструменты для заданий, тестов и отзывов о курсах. Платформа имеет полноценное REST API, правильную аутентификацию и разработана для масштабирования с развертыванием в Kubernetes.

## Технологический стек

### Backend
- **Фреймворк**: Spring Boot 3.4.0
- **Язык**: Java 17
- **База данных**: PostgreSQL
- **ORM**: Spring Data JPA
- **Документация API**: Springdoc OpenAPI (Swagger)
- **Миграции БД**: Flyway

### Frontend
- **Клиент API**: REST API доступен через любой HTTP клиент
- **Документация**: Swagger UI по адресу `/swagger-ui.html`

### Инфраструктура
- **Контейнеризация**: Docker
- **Оркестрация**: Kubernetes
- **Система управления зависимостями**: Maven
- **CI/CD**: GitLab CI
- **Управление конфигурацией**: Helm

## Архитектура приложения

Проект использует чистую архитектуру с четким разделением на слои:

- **Контроллеры (controller)**: Обрабатывают HTTP запросы и возвращают ответы
- **DTO (dto)**: Объекты передачи данных между слоями
- **Модели (model)**: Сущности базы данных с аннотациями JPA
- **Репозитории (repository)**: Интерфейсы для доступа к данным с использованием Spring Data JPA
- **Сервисы (service)**: Бизнес-логика приложения
- **Исключения (exception)**: Кастомные исключения и обработчики ошибок

Приложение построено по принципу REST API с использованием Spring Boot 3.4.0 и Java 17. Все компоненты связаны через внедрение зависимостей Spring.

## Установка и запуск

### Предварительные требования

Перед началом убедитесь, что у вас установлены следующие компоненты:

- Java 17 или выше
- Maven 3.6.3 или выше
- Docker и Docker Compose
- PostgreSQL 14 (опционально, если не используется Docker)

### Локальный запуск

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/username/educationplatform.git
   cd educationplatform
   ```

2. Запустите приложение через Maven:
   ```bash
   mvn spring-boot:run
   ```

   Приложение будет доступно на http://localhost:8080

### Запуск с Docker

1. Соберите и запустите контейнеры:
   ```bash
   docker-compose up -d
   ```

2. Проверьте статус контейнеров:
   ```bash
   docker-compose ps
   ```

3. Приложение будет доступно на http://localhost:8080

4. Для остановки:
   ```bash
   docker-compose down
   ```

## API

API документировано с помощью Swagger/OpenAPI и доступно по адресу:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI спецификация: http://localhost:8080/api-docs

### Доступные эндпоинты

#### Курсы (`/api/courses`)
- `GET /api/courses` - Получить все курсы
- `GET /api/courses/{id}` - Получить курс по ID
- `GET /api/courses/teacher/{teacherId}` - Получить курсы преподавателя
- `GET /api/courses/category/{categoryId}` - Получить курсы по категории
- `GET /api/courses/search?keyword={keyword}` - Поиск курсов
- `POST /api/courses` - Создать новый курс
- `PUT /api/courses/{id}` - Обновить курс
- `DELETE /api/courses/{id}` - Удалить курс

#### Пользователи (`/api/users`)
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users/email/{email}` - Получить пользователя по email
- `GET /api/users/teachers` - Получить всех преподавателей
- `GET /api/users/students` - Получить всех студентов
- `POST /api/users` - Создать нового пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

#### Модули (`/api/modules`)
- `GET /api/modules` - Получить все модули
- `GET /api/modules/{id}` - Получить модуль по ID
- `GET /api/modules/course/{courseId}` - Получить модули курса
- `POST /api/modules` - Создать новый модуль
- `PUT /api/modules/{id}` - Обновить модуль
- `DELETE /api/modules/{id}` - Удалить модуль

#### Уроки (`/api/lessons`)
- `GET /api/lessons` - Получить все уроки
- `GET /api/lessons/{id}` - Получить урок по ID
- `GET /api/lessons/module/{moduleId}` - Получить уроки модуля
- `POST /api/lessons` - Создать новый урок
- `PUT /api/lessons/{id}` - Обновить урок
- `DELETE /api/lessons/{id}` - Удалить урок

#### Задания (`/api/assignments`)
- `GET /api/assignments` - Получить все задания
- `GET /api/assignments/{id}` - Получить задание по ID
- `GET /api/assignments/lesson/{lessonId}` - Получить задания урока
- `POST /api/assignments` - Создать новое задание
- `PUT /api/assignments/{id}` - Обновить задание
- `DELETE /api/assignments/{id}` - Удалить задание

#### Решения заданий (`/api/submissions`)
- `POST /api/submissions` - Отправить решение задания
- `PUT /api/submissions/{id}/grade` - Оценить решение
- `GET /api/submissions/{id}` - Получить решение по ID
- `GET /api/submissions/assignment/{assignmentId}` - Получить решения по заданию
- `GET /api/submissions/student/{studentId}` - Получить решения студента
- `GET /api/submissions/assignment/{assignmentId}/average` - Получить средний балл по заданию
- `DELETE /api/submissions/{id}` - Удалить решение

#### Тесты (`/api/quizzes`)
- `POST /api/quizzes/submit` - Отправить результат теста
- `GET /api/quizzes/submissions/{id}` - Получить результат теста по ID
- `GET /api/quizzes/{quizId}/submissions` - Получить результаты теста
- `GET /api/quizzes/student/{studentId}/submissions` - Получить результаты тестов студента
- `GET /api/quizzes/{quizId}/average` - Получить средний балл по тесту
- `GET /api/quizzes/{quizId}/max` - Получить максимальный балл по тесту

#### Категории (`/api/categories`)
- `GET /api/categories` - Получить все категории
- `GET /api/categories/{id}` - Получить категорию по ID
- `GET /api/categories/name/{name}` - Получить категорию по названию
- `POST /api/categories` - Создать новую категорию
- `PUT /api/categories/{id}` - Обновить категорию
- `DELETE /api/categories/{id}` - Удалить категорию

#### Теги (`/api/tags`)
- `GET /api/tags` - Получить все теги
- `GET /api/tags/{id}` - Получить тег по ID
- `GET /api/tags/name/{name}` - Получить тег по названию
- `POST /api/tags` - Создать новый тег
- `PUT /api/tags/{id}` - Обновить тег
- `DELETE /api/tags/{id}` - Удалить тег

#### Записи на курсы (`/api/enrollments`)
- `POST /api/enrollments/enroll` - Записать студента на курс
- `DELETE /api/enrollments/{id}` - Отписать студента от курса
- `PUT /api/enrollments/{id}/status` - Обновить статус записи
- `GET /api/enrollments/student/{studentId}` - Получить записи студента
- `GET /api/enrollments/course/{courseId}` - Получить записи на курс
- `GET /api/enrollments/check?studentId={studentId}&courseId={courseId}` - Проверить запись студента на курс

#### Отзывы о курсах (`/api/course-reviews`)
- `GET /api/course-reviews` - Получить все отзывы
- `GET /api/course-reviews/{id}` - Получить отзыв по ID
- `GET /api/course-reviews/course/{courseId}` - Получить отзывы по курсу
- `GET /api/course-reviews/student/{studentId}` - Получить отзывы студента
- `POST /api/course-reviews` - Создать новый отзыв
- `PUT /api/course-reviews/{id}` - Обновить отзыв
- `DELETE /api/course-reviews/{id}` - Удалить отзыв

#### Профили (`/api/profiles`)
- `GET /api/profiles/user/{userId}` - Получить профиль пользователя
- `GET /api/profiles/{id}` - Получить профиль по ID
- `POST /api/profiles` - Создать профиль
- `PUT /api/profiles/{id}` - Обновить профиль
- `DELETE /api/profiles/{id}` - Удалить профиль

#### Статистика (`/api/statistics`)
- `GET /api/statistics` - Получить общую статистику

API включает эндпоинты для управления пользователями, курсами, модулями, уроками, заданиями, тестами и другими сущностями системы. Все эндпоинты используют стандартные HTTP методы и возвращают JSON-ответы.

### Обработка ошибок

API возвращает стандартизированные ответы об ошибках в формате:

```json
{
  "timestamp": "2025-12-29T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Entity not found with id: 999",
  "path": "/api/courses/999"
}
```

Для ошибок валидации:

```json
{
  "timestamp": "2025-12-29T10:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Validation failed for one or more fields",
  "path": "/api/courses",
  "errors": {
    "title": "must not be null",
    "description": "must not be empty"
  }
}
```

## База данных

Проект использует PostgreSQL в качестве основной базы данных. Миграции базы данных управляются с помощью Flyway и находятся в `src/main/resources/db/migration/`.

### Основные таблицы

- **users**: Пользователи системы (студенты и преподаватели)
- **courses**: Курсы с информацией о названии, описании, преподавателе и категории
- **modules**: Модули курса, содержащие уроки
- **lessons**: Уроки с контентом, видео и ресурсами
- **assignments**: Задания для студентов
- **submissions**: Решения студентов на задания
- **quizzes**: Тесты с вопросами и вариантами ответов
- **quiz_submissions**: Результаты прохождения тестов
- **categories**: Категории курсов
- **tags**: Теги для курсов
- **enrollments**: Записи студентов на курсы
- **course_reviews**: Отзывы студентов о курсах

## CI/CD пайплайн

Проект использует GitLab CI/CD для автоматизированного тестирования, сборки и развертывания. Пайплайн настроен в `.gitlab-ci.yml` и включает следующие стадии:

1. **Сборка**: Компиляция приложения с помощью Maven
2. **Тестирование**: Запуск юнит и интеграционных тестов
3. **Пакетирование**: Сборка Docker образа и загрузка в реестр
4. **Развертывание**: Развертывание в окружения Kubernetes

### Окружения развертывания

- **Dev**: Автоматическое развертывание при каждом коммите в ветку main
- **Staging**: Ручное развертывание для тестирования
- **Production**: Ручное развертывание с дополнительными требованиями

## Тестирование

Проект включает комплексную систему тестирования:

### Запуск тестов

1. Запустите все тесты:
   ```bash
   mvn test
   ```

2. Для запуска интеграционных тестов:
   ```bash
   mvn verify -Dskip.unit.tests
   ```

### Типы тестов

- **Юнит-тесты**: Проверяют отдельные компоненты (сущности, сервисы)
- **Интеграционные тесты**: Проверяют взаимодействие компонентов и работу с базой данных
- **Репозиторные тесты**: Проверяют работу репозиториев с базой данных

Все тесты проходят успешно как локально, так и в CI/CD пайплайне на GitLab.

## Развертывание в Kubernetes

Проект включает Helm чарт для развертывания в Kubernetes, расположенный в `helm/educationplatform/`.

### Установка чарта

1. Добавьте репозиторий Helm:
   ```bash
   helm repo add educationplatform ./helm/educationplatform
   ```

2. Установите чарт:
   ```bash
   helm install educationplatform ./helm/educationplatform \\
     --namespace educationplatform-dev \\
     --create-namespace
   ```

3. Проверьте статус релиза:
   ```bash
   helm status educationplatform -n educationplatform-dev
   ```

### Обновление релиза

```bash
helm upgrade educationplatform ./helm/educationplatform \\
  --namespace educationplatform-dev \\
  --set image.tag=latest
```

## Дополнительная информация

### Конфигурация

Основная конфигурация приложения находится в `src/main/resources/application.properties`. Для разных окружений можно использовать профили Spring.

### Docker образ

Docker образ собирается на основе Eclipse Temurin 17 и использует многоступенчатую сборку для оптимизации размера образа.

#