# Компонентная архитектура
<!-- Состав и взаимосвязи компонентов системы между собой и внешними системами с указанием протоколов, ключевые технологии, используемые для реализации компонентов.
Диаграмма контейнеров C4 и текстовое описание. 
-->
## Компонентная диаграмма

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

AddElementTag("microService", $shape=EightSidedShape(), $bgColor="CornflowerBlue", $fontColor="white", $legendText="microservice")
AddElementTag("storage", $shape=RoundedBoxShape(), $bgColor="lightSkyBlue", $fontColor="white")

Person(user1, "Пользователь")
Person(user, "Пользователь")
Person(user2, "Пользователь")

System_Boundary(ext, "Открытые внешние системы для получения данных по районам") {
       Container(cian, "Циан", "HTTP", "Сервис аренды и покупки помещений")
       Container(domclick, "Домклик", "HTTP", "Сервис аренды и покупки помещений")
       Container(yandex, "Яндекс.Недвижимость", "HTTP", "Сервис аренды и покупки помещений")
}

System_Boundary(app, "Приложение оценки районов города") {
    System_Boundary(front, "Интерфейс приложения") {
       Container(ios, "IOS", "IOS SDK", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "microService")
       Container(web, "Web сервис", "JavaScript", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "microService")
       Container(android, "Android", "Android SDK", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "microService")
    }
    System_Boundary(server, "Серверная часть приложенрия") {
        System_Boundary(k8s, "K8S") {
            System_Boundary(java_deployment, "Java сервисы") {
                Container(back1, "Java сервис", "Java(Kotlin)", "Rest сервисы", $tags = "microservice")
                Container(back, "Java сервис", "Java(Kotlin)", "Rest сервисы", $tags = "microservice")
                Container(back2, "Java сервис", "Java(Kotlin)", "Rest сервисы", $tags = "microservice")
            }
            System_Boundary(monogoDb_deployment, "MonogoDb экземпляры") {
                ContainerDb(db1, "База данных", "MongoDB", "Хранение данных о об оценках районов", $tags = "storage")
                ContainerDb(db, "База данных", "MongoDB", "Хранение данных о об оценках районов", $tags = "storage")
                ContainerDb(db2, "База данных", "MongoDB", "Хранение данных о об оценках районов", $tags = "storage")
            }
        }
    }
}

Rel_D(user, web, "Получение информации о районах и их рейтингах, проставление оценки району")
Rel_D(web, back, "Получение посчитанных оценок и передача проставленных оценок  на карте")
Rel_D(back, db, "Передача данных для хранения")
Rel_R(back, ext, "Получение данных о стоимости недвижомости","HTTP")
@enduml
```
