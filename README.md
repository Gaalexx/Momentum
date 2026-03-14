# Репозиторий Android-клиента **Momentum**
**Momentum** — приватное приложение для самого близкого круга (примерно 2–10 человек), где фото, короткие видео и тёплые голосовые заметки собираются в живую хронологическую «летопись» дружбы и родства. Это не чат и не публичная соцсеть: контент не делается ради лайков, а сохраняется как история отношений, к которой можно вернуться через любой промежуток времени.

## Архитектура и ключевые решения

### Подход
- Single-activity приложение на Jetpack Compose
- Архитектура: feature-first + state во `ViewModel`

### Слои и ответственность
- `MainActivity.kt`, `MomentumApp.kt` — точка входа, запуск Compose и Hilt.
- `navigation/` — маршруты, корневая навигация, выбор стартового экрана по состоянию сессии.
- `features/*/ui/` — экраны фич и пользовательские сценарии.
- `features/*/viewmodel/` — состояние экрана и обработка действий пользователя.
- `features/*/models/` — DTO, UI-state и локальные модели.
- `features/*/usecases/` — бизнес-сценарии там, где логика вынесена из `ViewModel`.
- `features/*/repo/`, `features/*/api/` — доступ к backend и подготовка данных внутри конкретной фичи.
- `data/` — общая auth/session-логика, регистрация, логин, восстановление сессии.
- `data/auth/*` — безопасное хранение токена через `DataStore` и `Android Keystore`.
- `network/` — `Ktor`-клиенты, S3 upload, сетевые модели.
- `di/` — Hilt-модули и сборка зависимостей.
- `ui/*` — общие UI-компоненты и тема.
- `assets/`, `res/` — ресурсы приложения и локальные mock-данные.

### Навигация
- `androidx.navigation3`, один корневой `NavDisplay`.
- Навигационные маршруты описаны в `navigation/`.
- Аргументы передаются через типизированные routes.

### Сеть и сериализация
- Ktor client
- JSON: kotlinx.serialization

### DI
- Hilt: ViewModel-injection + scoped dependencies (Singleton/Activity/VM).

### Медиа
- CameraX — съемка
- Coil/Landscapist — отображение изображений

## Ссылки
[Макеты в Figma](https://www.figma.com/design/Ea7SuLOEx3nSAsTS36PKHy/Zavoz?node-id=0-1&p=f&t=veO7JMIP3wd3y4WW-0) •
[ТЗ](https://docs.google.com/document/d/1Vdnet940qkdQ249nMprJ8Al9bJk9Qou2/edit)

## Команда разработчиков
Название: **Zavoz**

Состав:
  - Гайдуков Александр (Gaalexx) - менеджер/программист
  - Кондратенко Александр (abracadabrabrabra) - дизайнер/программист
  - Гусев Савелий (guse95) - программист
  - Заворотный Алексей (AlekseiZavorotnyi) - программист
