# Momentum

**Momentum** - Android-клиент приватной социальной сети для близкого круга людей. Приложение помогает сохранять фото, короткие видео и голосовые заметки в общей хронологической ленте, делиться ими с выбранными друзьями и возвращаться к истории отношений без механик публичной соцсети.

Проект развивается как single-activity приложение на Kotlin и Jetpack Compose. Основной пользовательский сценарий построен вокруг камеры, записи аудио, галереи публикаций, профиля, друзей и настроек приватности.

## Возможности

- Регистрация и вход по email/телефону с кодом подтверждения.
- Авторизация через VK ID и синхронизация токена VK SDK.
- Восстановление сессии при запуске, выход из аккаунта и удаление аккаунта.
- Съемка фото, запись видео и запись голосовых заметок до 60 секунд.
- Предпросмотр и отправка медиа выбранным друзьям.
- Загрузка медиа и аватаров через backend-presigned S3 URL.
- Галерея публикаций, просмотр своих и чужих постов, скрытые публикации.
- Реакции на публикации и запрос расшифровки голосового контента.
- Профиль пользователя, редактирование данных и аватара.
- Список друзей, поиск, входящие заявки, принятие/отклонение заявок и удаление друзей.
- Настройки уведомлений, публикаций, реакций, заявок в друзья, темы и Premium-экрана.
- Push-уведомления через Firebase Cloud Messaging.
- Offline-экран при ошибке восстановления сессии из-за отсутствия сети.

## Архитектура

Проект организован по feature-first подходу:

- `MainActivity.kt`, `MomentumApp.kt` - точка входа, инициализация Hilt, VK SDK/VK ID, темы и Compose.
- `navigation/` - типизированные routes на `androidx.navigation3`, корневой `NavDisplay`, стартовый экран по состоянию сессии.
- `features/*/ui/` - Compose-экраны и пользовательские сценарии.
- `features/*/viewmodel/` - состояние экранов, события UI и вызовы репозиториев.
- `features/*/models/` - DTO, UI-state и локальные модели.
- `features/*/repo/`, `features/*/api/` - работа с backend внутри конкретных фич.
- `features/*/usecases/` - вынесенная бизнес-логика там, где она есть.
- `data/` - регистрация, логин, восстановление сессии и общая auth-логика.
- `data/auth/` - JWT в памяти, зашифрованное хранение refresh-данных через DataStore и Android Keystore.
- `network/` - Ktor-клиенты, DI-квалификаторы, S3 upload и сетевые модели.
- `di/` - Hilt-модули приложения.
- `ui/` - общая тема, дизайн-константы и переиспользуемые Compose-компоненты.
- `res/`, `assets/` - ресурсы приложения и локальные mock-данные.

## Технологии

- Kotlin 2.2.10, JVM 11
- Android Gradle Plugin 9.2.1
- Gradle Wrapper 9.4.1
- Jetpack Compose, Material 3
- AndroidX Navigation 3
- Hilt и KSP
- Ktor Client, kotlinx.serialization
- DataStore, Android Keystore
- CameraX, Media3, Coil
- Firebase Analytics и Firebase Cloud Messaging
- VK ID / VK Android SDK

SDK:

- `minSdk = 30`
- `targetSdk = 36`
- `compileSdk = 36`

## Конфигурация

Для сборки нужны локальные параметры в `local.properties`:

```properties
vkAppId=1234567
clientId=1234567
clientSecret=...
API_KEY="..."
```

`clientId` должен совпадать с `vkAppId`, иначе Gradle остановит сборку. `API_KEY` используется опционально: при отсутствии ключа сборка продолжится с пустым значением.

Firebase настроен через `app/google-services.json`. Backend URL сейчас задан в `app/build.gradle.kts` как `http://193.233.20.47/api/momentum/`, email checker - как `https://rapid-email-verifier.fly.dev/api/`.

## Сборка

```bash
./gradlew assembleDebug
```

## Ссылки

[Макеты в Figma](https://www.figma.com/design/Ea7SuLOEx3nSAsTS36PKHy/Zavoz?node-id=0-1&p=f&t=veO7JMIP3wd3y4WW-0)  
[ТЗ](https://docs.google.com/document/d/1Vdnet940qkdQ249nMprJ8Al9bJk9Qou2/edit)

## Команда

Название команды: **Zavoz**

- Гайдуков Александр (Gaalexx) - менеджер/программист
- Кондратенко Александр (abracadabrabrabra) - дизайнер/программист
- Гусев Савелий (guse95) - программист
- Заворотный Алексей (AlekseiZavorotnyi) - программист
