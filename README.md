# bookshelf
Book catalog service
# Описание
#### REST API для каталога книг.
 - Операции с ведением реестра авторов (CRUD).
 - Операции по добавлению книги в каталог.
 - Операции по редактированию книги.
 - Операции поиска книги по названию, автору (с возможностью сортировки)
 - Получение истории по изменениям книги.
 - Операции удаления.
#### Ограничения:
 - У книги должен быть минимум 1 автор.
 - Максимальная длина строковых полей - 255 символов.
 - Не может быть книги с одинаковым названием и годом.
#### Требования к сервису:
 - Сервис должен быть разработан на Java Spring. База данных PostgreSQL с поддержкой миграций Flyway. Maven, Hibernate.
 - Описание методов сервиса должно быть доступно в swagger.
 - К автору книги должна быть реализована возможность добавить фото (опционально).
 - Приложение разместить на github.
 # Документация
Описание методов сервиса доступно в swagger:  
```
http://localhost:8080/bookshelf/swagger-ui
