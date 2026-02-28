# Репозиторий Android-клиента **Momentum**
**Momentum** — приватное приложение для самого близкого круга (примерно 2–10 человек), где фото, короткие видео и тёплые голосовые заметки собираются в живую хронологическую «летопись» дружбы и родства. Это не чат и не публичная соцсеть: контент не делается ради лайков, а сохраняется как история отношений, к которой можно вернуться через любой промежуток времени.

## Архитектура и ключевые решения

### Подход
- UI: Jetpack Compose, single-activity
- Архитектура: MVVM (UI -> ViewModel -> Repository)

### Слои и ответственность
- `ui/` — экраны, компоненты, тема. Не содержит работы с сетью/базой.
- `viewmodel/` — состояние экрана, обработка событий, вызов use-cases/repositories.
- `domain/` — бизнес-логика (use cases), модели домена.
- `data/` — репозитории, маппинг моделей, источники данных.
- `network/` — Ktor client, API, сериализация.
- `di/` — Hilt-модули и биндинги.

### Навигация
- Navigation Compose, один `NavHost` как корень.
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
