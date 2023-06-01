# Emaill Processing Service

This service receive and process transaction's emails. Checkout API documentation at: http://localhost:8085/swagger-ui/index.html

## Requirements

- Java 17
- MySQL 8.0
- Docker (optional)

## Environment Variables

It is important to ensure that the environment variables are properly configured before running the API. If you run automated tests, you must configure the variables for that setup.

How to configure environment variables in IntelliJ?
Below, you will find a list of the required environment variables to configure and run the API:

#### `DATABASE_URL`\*

The URL of the MySQL database to which the API will connect. It should follow the following format and replace the corresponding values: `jdbc:mysql://{db_host}:{db_port:3306}/{db_name}`

#### `DATABASE_USERNAME`\*

The username used to authenticate the database connection.

#### `DATABASE_PASSWORD`\*

The password used to authenticate the database connection.

### `MAIL_USER`\*

Email service user.

### `MAIL_PASSWORD`\*

Email application password.

#### `PORT`

The port on which the server will run. By default, it uses port `8085`.

#### `ALLOWED_ORIGINS`

A list of authorized hosts to make requests to the API. By default, the value `http://localhost:4200` is used to allow requests from the web client. Multiple hosts can be specified, separated by commas.

#### `SHOW_SQL`

Indicates whether Hibernate should display the SQL queries generated in the console. By default, it uses the value `false`.


##### Note: \* required values.

## Docker compose for local development

Use docker to create a MySQL server as a container. To do this, run the following command in the root directory of the project:

```shell
$ docker-compose up
```

This will create a MySQL server with the following configuration:

- ### Container information
  | Mysql    | Value                    |
  | -------- | ------------------------ |
  | Host     | localhost                |
  | Port     | 3306                     |
  | User     | user                     |
  | Password | pass                     |
  | Database | email_processing_service |

Note:

- Use `Ctl + C` to stop the container.
