# Dota 2 Item Data - Backend
REST-сервер, предоставляющий доступ к информации о внутриигровых предметах

## Endpoints:

* `GET /items` получение ID всех сохранённых в БД предметов
* `GET /items/{itemId}` получение подробной информации о предмете
* `GET /grids` получение ID доступных видов предметов
* `GET /grids/{gridId}` получение всех категорий предметов (в том числе ID категорий и предметов) выбранного вида
* `GET /grids/{gridId}/{blockId}` получение подробной информации о всех предметах выбранного вида и категории
* `PUT /grids` обновление БД
* `GET /quiz` старт квиза по сборке предметов
* `POST /quiz/{itemId}` `body: List<itemId>` проверка ответа на квиз