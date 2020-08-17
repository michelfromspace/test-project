# test-project

автотесты для API, который позвоялет создавать и удалять треугольники, получать их по отдельности и все вместе, а так же получать периметр или площадь отдельного треугольника

## Запуск тестов
* подготовить файл конфигурации **test.properties**, который необходимо разместить в **src/test/resources**, и указать там параметры:
```
api.hostname=http://your.service.uri
api.token=token
```
* выполнить команду **mvn clean test** для запуска тестов
* выполнить команду **mvn allure:serve** для запуска отчета по результатам теста
