## Лабораторная работа 5. Разработка и развертывание Web-приложений

## Цель работы
Переход от Spring JDBC к ORM Hibernate и Spring Data JPA. Реализация слоистой архитектуры: entity, repository, service, app.

## Описание реализации
Скачал, установил и настроил Apache Tomcat 11. Настроил проект, так чтобы в результате сборки формировался WAR-файл. Реализовал Java-сервлет формирующий Web-страницу с информацией о заказах. Соберал приложение используя команду gradle war.

## UML-диаграмма классов проекта

```mermaid
classDiagram
    %% Сервлеты (Веб-слой)
    class OrderListServlet {
        <<Servlet>>
    }
    class OrderCreateServlet {
        <<Servlet>>
    }
    class ProductRestServlet {
        <<Servlet>>
    }

    %% Сущности (База данных)
    class Customer {
        <<Entity>>
    }
    class Order {
        <<Entity>>
    }
    class OrderDetail {
        <<Entity>>
    }
    class Product {
        <<Entity>>
    }
    class Category {
        <<Entity>>
    }

    %% Связи веб-слоя с данными
    OrderListServlet ..> Order : Запрос списка (GET)
    OrderCreateServlet ..> Order : Создание и запись (POST)
    ProductRestServlet ..> Product : Запрос JSON-данных (GET)

    %% Связи между сущностями (Бизнес-логика)
    Customer "1" --> "*" Order : размещает
    Order "1" *-- "*" OrderDetail : содержит позиции
    OrderDetail "*" --> "1" Product : ссылается на товар
    Category "1" --> "*" Product : содержит
