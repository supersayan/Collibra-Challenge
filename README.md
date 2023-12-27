# Collibra Challenge #

Spring Boot application for RESTful API managing assets using MySQL for persistence.

<details>
<summary>Assessment Details</summary>

Consider a metadata system that persists “asset” information. A sample format of some assets:
```
Asset A (contains)

    Asset B (contains)

        Asset D (contains)

            Asset F

            Asset G

        Asset E (contains)

            Asset H

            Asset I

    Asset C (contains)

        Asset J (contains)

            Asset K

            Asset L
```

Requirements:

Create a RESTful microservice with endpoints that allows you to manage assets and perform promotion of those assets. This microservice should be capable of the following:

* Create, Read, Update, Delete assets.
* We would need to be able to provide a way to “promote” an asset. When an asset is promoted:
  * A new system is informed of the promotion.
  * The assets that are nested under that asset and its ancestors are also promoted.
  * The promoted assets are marked as promoted.
* Follow REST standards
* Logging
* JUnit tested code
  * Full coverage is not necessary. Do enough to show you can write thoughtful/thorough test cases.

Nice to Haves:

Although not required the following would be nice to have in your solution to express your ability level of technologies/practices we abide by amongst our team:

* Containerized solution
* Spring based application
* Use OpenAPI, one of the following approaches:
  * API First, use OpenAPI doc to generate code stubs. This approach is currently used by all of Collibra.
  * Code First, use OpenAPI annotations in code to generate OpenAPI doc.
* Use of persistent RDBMS or NOSQL store
* Use of an eventing platform
</details>

---

For interview questions see [Interview.md](Interview.md).

To run in a container, complete with database, with docker installed:
```
docker compose build
docker compose up
```
Then query `localhost:6868/asset`

You can also open the API docs in `http://localhost:6868/swagger-ui/index.html#/` to view Swagger UI, or `http://localhost:6868/api-docs` in JSON.

To stop:
```
docker compose down -v
```

- Getting an asset: `GET /asset/{id}`
- Get all assets: `GET /asset`
- Create an asset: `POST /asset` + request body `{id, name, isPromoted, parentId}`
- Update an asset: `PUT /asset`
- Delete an asset: `DELETE /asset`

See `AssetService.java` for CRUD and promotion logic.
`AssetServiceTest.java` doesn't work because of issues mocking the repository and so I commented the tests out. Ideally we have tests checking for success and failure + edge cases for each route.

I included RabbitMQ stubs for eventing but it isn't setup.

Todos: 

- [x] Read
- [x] Create
- [x] Swagger UI
- [x] Set parent

- Test Update
- Test Delete
- Test Promotion
- Custom exceptions
- Unit test coverage
- RabbitMQ
