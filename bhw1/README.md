## Developer Setup

### General

Do not forget to setup nasa apikey in properties file

### Liquibase

All configurations located in **liquibase** folder<br/>
To create tables run below command:

```shell
mvn liquibase:update
```

To rollback changes run below command:

```shell
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

### Docker

Creates postgress db:

```shell
docker compose up
```