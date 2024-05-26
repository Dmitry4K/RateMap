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
AddRelTag("noReady", $lineStyle = DashedLine())
AddElementTag("noReady", $bgColor="gray")

Person(user1, "Пользователь")
Person(user2, "Пользователь")
Person(user3, "Пользователь")

System_Boundary(app, "Приложение оценки районов города") {
    System_Boundary(front, "Интерфейс приложения") {       
        Container(android, "Android", "Android SDK", "Отображение рейтинговой карты города, проставление оценок районам")
        Container(ios, "IOS", "IOS SDK", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "noReady")
        Container(web, "Web сервис", "JavaScript", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "noReady")
        Lay_R(android, ios)
        Lay_R(ios, web)
    }
    System_Boundary(back, "Серверная часть приложения") {
        Container(java, "Java сервис", "Java(Kotlin)", "Rest сервисы", $tags = "microservice")
        ContainerDb(mongo, "База данных", "MongoDB", "Хранение данных о об оценках районов", $tags = "storage")
        ContainerDb(redis, "База данных/Кеш", "Redis", "Хранение данных о об оценках районов", $tags = "storage")
        Lay_L(java, mongo)
        Lay_R(java, redis)
    }
}

System_Boundary(ext, "Внешние системы и адаптеры") {
       Container(acian, "Адаптер данных", "HTTP", "")
       Container(adomclick, "Адаптер данных", "HTTP", "", $tags="noReady")
       Container(ayandex, "Адаптер данных", "HTTP", "", $tags="noReady")
       
       Container(cian, "Циан", "HTTP", "Сервис аренды и покупки помещений")
       Container(domclick, "Домклик", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
       Container(yandex, "Яндекс.Недвижимость", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
       Lay_R(cian, domclick)
       Lay_R(domclick, yandex)
       Lay_D(acian, cian)
       Lay_D(adomclick, domclick)
       Lay_D(ayandex, yandex)
}
Rel_D(user1, android, "Получение посчитанных оценок и передача проставленных оценок  на карте")
Rel_D(user2, ios, "", $tags="noReady")
Rel_D(user3, web, "", $tags="noReady")
Rel_D(android, java, "Получение посчитанных оценок и передача проставленных оценок  на карте")
Rel_D(ios, java, "", $tags="noReady")
Rel_D(web, java, "", $tags="noReady")
Rel_L(java, mongo, "Передача данных для хранения")
Rel_R(java, redis, "Кеширование данных")
Rel_D(java, acian, "Получение данных о стоимости недвижомости","HTTP", $tags="noReady")
Rel_D(java, adomclick, "","", $tags="noReady")
Rel_D(java, ayandex, "", $tags="noReady")
Rel_D(acian, cian, "Получение данных о стоимости недвижомости","HTTP")
Rel_D(adomclick, domclick, "")
Rel_D(ayandex, yandex, "")

@enduml
```

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

AddElementTag("microService", $shape=EightSidedShape(), $bgColor="CornflowerBlue", $fontColor="white", $legendText="microservice")
AddElementTag("storage", $shape=RoundedBoxShape(), $bgColor="lightSkyBlue", $fontColor="white")

Person(user, "Пользователь")

System_Boundary(app, "Приложение оценки районов города") {
    System_Boundary(server, "Серверная часть приложенрия") {
        Container(redis, "Кеш", "Redis", "Хранение подсчитанной карты", $tags = "microservice")
        Container(migrate, "Генератор тепловой карты", "Redis", "Хранение подсчитанной карты", $tags = "microservice")
        Container(back, "Сервер", "Java(Kotlin), Spring", "Контроллеры для получения и выгрузки тепловых карт", $tags = "microservice")
        ContainerDb(db, "База данных", "MongoDB", "Хранение гео-данных", $tags = "storage")
    }
    System_Boundary(front, "Интерфейс приложения") {
      Container(android, "Мобильное приложение", "Java(Kotlin), Android SDK, Yandex Map Kit", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "microService")
    }
}

System_Boundary(ext, "Открытые внешние системы для получения данных по районам", $tags = "noReady") {
   Container(cian, "Циан", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
   Container(domclick, "Домклик", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
   Container(yandex, "Яндекс.Недвижимость", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
}

Rel_R(user, android, "Просмотр тепловых карт")
Rel_R(android, back, "Получение тепловых карт, отправка оценок")
Rel_D(back, db, "Получение данных для построения тепловых карт")
Rel_R(back, ext, "Получение данных о стоимости недвижомости","HTTP")
Rel_R(migrate, db, "Генерация точек карты","JDBC")
Rel_D(back, redis, "Кеширование","TCP")
@enduml
```

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

AddElementTag("microService", $shape=EightSidedShape(), $bgColor="CornflowerBlue", $fontColor="white", $legendText="microservice")
AddElementTag("storage", $shape=RoundedBoxShape(), $bgColor="lightSkyBlue", $fontColor="white")

Person(user, "Пользователь")

System_Boundary(app, "Приложение оценки районов города") {
    System_Boundary(server, "Серверная часть приложенрия") {
        Container(redis, "Кеш", "Redis", "Хранение подсчитанной карты", $tags = "microservice")
        Container(migrate, "Генератор тепловой карты", "Redis", "Хранение подсчитанной карты", $tags = "microservice")
        Container(back, "Сервер", "Java(Kotlin), Spring", "Контроллеры для получения и выгрузки тепловых карт", $tags = "microservice")
        ContainerDb(db, "База данных", "MongoDB", "Хранение гео-данных", $tags = "storage")
    }
    System_Boundary(front, "Интерфейс приложения") {
      Container(android, "Мобильное приложение", "Java(Kotlin), Android SDK, Yandex Map Kit", "Отображение рейтинговой карты города, проставление оценок районам", $tags = "microService")
    }
}

System_Boundary(ext, "Открытые внешние системы для получения данных по районам", $tags = "noReady") {
   Container(cian, "Циан", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
   Container(domclick, "Домклик", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
   Container(yandex, "Яндекс.Недвижимость", "HTTP", "Сервис аренды и покупки помещений", $tags="noReady")
}

Rel_R(user, android, "Просмотр тепловых карт")
Rel_R(android, back, "Получение тепловых карт, отправка оценок")
Rel_D(back, db, "Получение данных для построения тепловых карт")
Rel_R(back, ext, "Получение данных о стоимости недвижомости","HTTP")
Rel_R(migrate, db, "Генерация точек карты","JDBC")
Rel_D(back, redis, "Кеширование","TCP")
@enduml
```


Deploy
```shell
docker build --no-cache . -t dmitry4k/ratemap:backend-v*.*.*
docker image push dmitry4k/ratemap:backend-v*.*.*
kubectl apply -f balancer.yml 
kubectl set image deployments/ratemap-backend ratemap-backend-container=docker.io/dmitry4k/ratemap:backend-v*.*.*
kubectl set env deployments/ratemap-backend MONGO_URL=<url>
kubectl set env deployments/ratemap-backend REDIS_PASS=<redis_pass>
kubectl set env deployments/ratemap-backend REDIS_HOST=<redis_host>
kubectl set env deployments/ratemap-backend REDIS_PORT=<redis_port>
kubectl exec -it <pod_name> -- env
```

./keytool.exe -importcert -alias mongo -file "C:\Users\Dmitry\.mongodb\root.crt" -keystore "C:\Users\Dmitry\.jdks\corretto-17.0.7\lib\security\cacerts"
