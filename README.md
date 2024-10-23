# Spring 6 Rest MVC

## Section 11: MySQL with Spring Boot

Adds _MySQL_ to store the _Beer_ in a _Real Database_.

In contrast to the original project, this version uses _Testcontainers_ and removes _H2_.
So you have to install _Docker Desktop_ to start and test the application.

Branches

- [71.use-testcontainers](https://github.com/sosercodes/spring-6-rest-mvc/tree/71.use-testcontainers)
- [75.schema-script-generation](https://github.com/sosercodes/spring-6-rest-mvc/tree/75.schema-script-generation)

## Section 12: Flyway Migrations

Uses Flyway to update/change the database if an _Entity_ changes.

_Flyway Scripts_ follow the a naming schema `V1__init-mysql-database.sql`.
Spring Boot searches for them in a folder `/src/main/recourses/csvdata/db/migration'.

Branches

- [76.flyway-dependencies](https://github.com/sosercodes/spring-6-rest-mvc/tree/76.flyway-dependencies)

## Section 13: CSV File Uploads

Doesn't _Upload_ Files but adds lots of _Data_ to our App using a _CSV File_ `/src/main/recourses/csvdata/beers.csv` which will be .

Adds `BeerCsvService` and `BeerCsvServiceImplTest`, which loads the CSV file and tests if it works correctly.

Branches

- [79-beer-csv-data](https://github.com/springframeworkguru/spring-6-rest-mvc/tree/79-beer-csv-data)
- [85-beer-csv-fix-integration-tests](https://github.com/springframeworkguru/spring-6-rest-mvc/tree/85-beer-csv-fix-integration-tests)

## Section 14: Query Parameters with Spring MVC

Adds _Query Parameters_ to the `BeerController`. Get all `Beer` with `lager` in the name.

```bash
curl -s "localhost:8080/api/v1/beer?beerName=lager" | jq
```

Branch

- [89.refactor-service-conditional-logic](https://github.com/sosercodes/spring-6-rest-mvc/tree/89.refactor-service-conditional-logic).


## Section 15: Paging and Sorting

Branch 

- [96.refactor-spring-data-jpa-repositories](https://github.com/sosercodes/spring-6-rest-mvc/tree/96.refactor-spring-data-jpa-repositories).
- [97.add-sort-parameter](https://github.com/sosercodes/spring-6-rest-mvc/tree/97.add-sort-parameter)

Use the following `curl` command to get a _Beer_.

```bash
curl -s "localhost:8080/api/v1/beer?pageNumber=1&pageSize=1" | jq
{
  "content": [
    {
      "id": "38ffc38d-f083-4ba3-8fba-9f0acb389f2d",
      "version": 0,
      "beerName": "#001 Golden Amber Lager",
      "beerStyle": "PILSNER",
      "upc": "2382",
      "quantityOnHand": 2382,
      "price": 10.00,
      "createdDate": "2024-10-16T20:37:33.303497",
      "updateDate": "2024-10-16T20:37:33.303497"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "last": false,
  "totalPages": 2413,
  "totalElements": 2413,
  "first": true,
  "size": 1,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 1,
  "empty": false
}
```

References

- [Mkyong - Spring Data JPA Paging and Sorting example](https://mkyong.com/spring-boot/spring-data-jpa-paging-and-sorting-example/)
- [BezKoder - Spring Boot Pagination & Filter example | Spring JPA, Pageable](https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/)
- [Dan Vega - Spring Data JPA Pagination - Youtube](https://www.youtube.com/watch?v=oq-c3D67WqM)
- [Dan Vega - Spring Data JPA Pagination](https://www.danvega.dev/blog/spring-data-jpa-pagination)

## Section 16: Database Relationship Mappings

- Adds new Migration Script, that adds _BeerOrder_ and _BeerOrderLine_.
- Creates new Entities `BeerOrder` and `BeerOrderLine` and updates `Customer`.
- Updates `BeerOrderLine` and adds One-To-Many Relationships.
- Creates `BeerOrderRepository` and `BeerOrderRepositoryTest`.
- Creates a new entity `Category` and a Many-To-Many Mapping to `Beer`.
- Adds `BeerOrderShipment` and One-To-One Relationship from `BeerOrder` to `BeerOrderShipment`. But don't do it like he does! Use Unidirectional Mapping. See Referneces!

Branches

- [102.create-beer-order-repository](https://github.com/sosercodes/spring-6-rest-mvc/tree/102.create-beer-order-repository)
- [105.many-to-many](https://github.com/sosercodes/spring-6-rest-mvc/tree/105.many-to-many) from `Beer` to `Category`
- [107.one-to-one-bi-directional](https://github.com/sosercodes/spring-6-rest-mvc/tree/107.one-to-one-bi-directional) from `Beer` to `Category`
- 

References

- [Thorben Janssen - Hibernate Tip: How to Share the Primary Key in a One-to-One Association](https://www.youtube.com/watch?v=t9jdfQqmTVU).
- [The best way to map a @OneToOne relationship with JPA and Hibernate](https://www.youtube.com/watch?v=GRV69QNSdVg)

