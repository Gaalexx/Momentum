# Momentum

**Momentum** — Android-клиент приватной социальной сети для близкого круга людей. Приложение помогает сохранять фото, короткие видео и голосовые заметки в общей хронологической ленте, делиться ими с выбранными друзьями и возвращаться к истории отношений без механик публичной соцсети.

## Возможности

- Регистрация и вход по email/телефону с кодом подтверждения.
- Авторизация через VK ID и синхронизация токена VK SDK.
- Восстановление сессии при запуске, выход из аккаунта и удаление аккаунта.
- Съёмка фото, запись видео и запись голосовых заметок до 60 секунд.
- Предпросмотр и отправка медиа выбранным друзьям.
- Загрузка медиа и аватаров через backend-presigned S3 URL с индикацией прогресса.
- Лента публикаций, просмотр своих и чужих постов, скрытые публикации.
- Реакции на публикации и запрос расшифровки голосового контента.
- Профиль пользователя, редактирование данных и аватара.
- Список друзей, поиск, входящие заявки, принятие/отклонение заявок и удаление друзей.
- Настройки уведомлений, публикаций, реакций, заявок в друзья, темы и Premium-экрана.
- Push-уведомления через Firebase Cloud Messaging.
- Offline-экран при ошибке восстановления сессии из-за отсутствия сети.

## Архитектура

Проект организован по feature-first подходу, слой представления — MVVM.

```
com.project.momentum/
├── MainActivity.kt, MomentumApp.kt   точка входа, инициализация Hilt, VK SDK/VK ID, темы и Compose
├── MyFirebaseMessagingService.kt     приём push-уведомлений FCM
├── navigation/                       типизированные routes на androidx.navigation3,
│                                     корневой NavDisplay, стартовый экран по состоянию сессии
├── features/                         account, auth, cameracontentpager, contentcreation,
│   │                                 editingAccount, friends, offline, posts, settings
│   ├── */ui/                         Compose-экраны и пользовательские сценарии
│   ├── */viewmodel/                  состояние экранов, события UI и вызовы репозиториев
│   ├── */models/                     DTO, UI-state и локальные модели
│   ├── */repo/, */api/               работа с backend внутри конкретной фичи
│   └── */usecases/                   вынесенная бизнес-логика там, где она есть
├── data/                             регистрация, логин, восстановление сессии, общая auth-логика
│   └── auth/                         JWT в памяти, зашифрованное хранение refresh-данных
│                                     через DataStore и Android Keystore
├── network/                          Ktor-клиенты, DI-квалификаторы, S3 upload, сетевые модели
├── di/                               Hilt-модули приложения
└── ui/                               общая тема, дизайн-константы, переиспользуемые
                                      Compose-компоненты и кастомные Canvas-элементы
```

`app/src/main/res/` — ресурсы приложения, `app/src/main/assets/` — локальные mock-данные (`posts.json`, `users.json`).

## Технологии

**Сборка**

- Kotlin 2.4.10, JVM target 11
- Android Gradle Plugin 9.3.1
- Gradle Wrapper 9.7.0
- KSP 2.3.2
- Gradle Kotlin DSL + version catalog (`gradle/libs.versions.toml`)

**Приложение**

- Jetpack Compose (BOM 2026.08.00), Material 3, ConstraintLayout Compose, Lottie
- AndroidX Navigation 3
- Hilt (DI)
- Ktor Client, kotlinx.serialization, kotlinx.coroutines
- DataStore, Android Keystore
- CameraX, Media3 (ExoPlayer), Coil
- Firebase Analytics и Firebase Cloud Messaging
- VK ID / VK Android SDK

**SDK**

- `minSdk = 31`
- `targetSdk = 36`
- `compileSdk = 37`

## Конфигурация

Для сборки нужны локальные параметры в `local.properties`:

```properties
vkAppId=1234567
clientId=1234567
clientSecret=...
API_KEY="..."
```

Firebase настраивается через `app/google-services.json` (файл не хранится в репозитории).

## Сборка

Debug-сборка:

```bash
./gradlew assembleDebug
```

Release-сборка (нужен `app/keystore.jks` и секреты подписи; включены R8 и удаление неиспользуемых ресурсов):

```bash
./gradlew assembleRelease
```

`versionCode` на CI берётся из `GITHUB_RUN_NUMBER`, при локальной сборке равен 1.

## CI

`.github/workflows/build-apk.yml` — GitHub Actions: на push в `main` и по `workflow_dispatch` собирается подписанный release-APK.

Пайплайн восстанавливает `local.properties`, `google-services.json` и `keystore.jks` из секретов репозитория, публикует APK как артефакт (хранится 30 дней) и отправляет сборку в Telegram-канал команды. При падении сборки в тот же канал приходит уведомление со ссылкой на run.

Используемые секреты: `VK_APP_ID`, `VK_CLIENT_SECRET`, `API_KEY`, `GOOGLE_SERVICES_JSON_BASE64`, `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`, `TG_CHAT_ID`, `TG_BOT_TOKEN`.

## Ссылки

[Макеты в Figma](https://www.figma.com/design/Ea7SuLOEx3nSAsTS36PKHy/Zavoz?node-id=0-1&p=f&t=veO7JMIP3wd3y4WW-0)
[ТЗ](https://docs.google.com/document/d/1Vdnet940qkdQ249nMprJ8Al9bJk9Qou2/edit)

## Команда

Название команды: **Zavoz**

- Гайдуков Александр ([Gaalexx](https://github.com/Gaalexx)) — тимлид, Android-разработчик
- Кондратенко Александр ([abracadabrabrabra](https://github.com/abracadabrabrabra)) — дизайнер, Android-разработчик
- Гусев Савелий ([guse95](https://github.com/guse95)) — Android-разработчик
- Заворотный Алексей ([AlekseiZavorotnyi](https://github.com/AlekseiZavorotnyi)) — Android-разработчик
