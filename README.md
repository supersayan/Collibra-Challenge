# Collibra Challenge #

Spring Boot application for RESTful API managing assets using MySQL for persistence.

Work in progress.

To run in a container, complete with database, with docker installed:
```
docker compose build
docker compose pull
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

See `AssetService.java` for CRUD and promotion logic. The API doesn't actually work but the code is there. For promotion I assume that promoting descendents and ancestors doesn't recurse forever to protect from a loop. `AssetServiceTest.java` also doesn't work because of issues mocking the repository and so I commented the tests out. Ideally we have tests checking for success and failure + edge cases for each route.

I also wasn't able to get the Swagger UI working despite including the dependency for it and trying whatever I could find online, but I did write the proposed schema in GraphQL at `./src/main/resources/graphql/schema.graphqls`.

I attempted to include RabbitMQ for eventing but it isn't properly implemented. I assume when "A new system is informed of the promotion" that would involve sending a RabbitMQ message to another system.

Todos: 
- [x] Read
- [x] Create
- Test Update
- Test Delete
- Test Promotion
- [x] Swagger UI
- Custom exceptions
- Unit test coverage
- RabbitMQ
