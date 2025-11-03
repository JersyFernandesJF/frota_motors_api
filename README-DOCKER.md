# Docker Setup for Frota Motors API

This project includes Docker configuration for easy development and deployment.

## Prerequisites

- Docker and Docker Compose installed on your machine

## Quick Start

### Using Docker Compose (Recommended)

1. **Build and start all services:**
   ```bash
   docker-compose up -d
   ```

2. **View logs:**
   ```bash
   docker-compose logs -f app
   ```

3. **Stop all services:**
   ```bash
   docker-compose down
   ```

4. **Stop and remove volumes (database data):**
   ```bash
   docker-compose down -v
   ```

### Using Dockerfile Only

1. **Build the image:**
   ```bash
   docker build -t frota-motors-api .
   ```

2. **Run the container:**
   ```bash
   docker run -p 9090:9090 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/frota_motors \
     -e SPRING_DATASOURCE_USERNAME=your_username \
     -e SPRING_DATASOURCE_PASSWORD=your_password \
     frota-motors-api
   ```

## Services

### Application
- **Port:** 9090
- **API Docs:** http://localhost:9090/swagger-ui.html
- **API Docs JSON:** http://localhost:9090/v3/api-docs

### PostgreSQL Database
- **Port:** 5432
- **Database:** frota_motors
- **Username:** frota_user
- **Password:** frota_password
- **Data Persistence:** Stored in Docker volume `postgres_data`

## Environment Variables

You can override default values by setting environment variables in `docker-compose.yml` or passing them when running the container:

- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SERVER_PORT` - Application port (default: 9090)
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - DDL mode (default: update)

## Development

### Rebuild after code changes:
```bash
docker-compose up -d --build
```

### Access database:
```bash
docker-compose exec postgres psql -U frota_user -d frota_motors
```

### View application logs:
```bash
docker-compose logs -f app
```

### Access container shell:
```bash
docker-compose exec app sh
```

## Production Considerations

For production deployment:

1. **Change default passwords** in `docker-compose.yml`
2. **Use environment variables** for sensitive data (don't hardcode passwords)
3. **Configure proper database** connection (use managed database service)
4. **Set up proper logging** and monitoring
5. **Use HTTPS** for API endpoints
6. **Configure resource limits** in docker-compose.yml
7. **Use Docker secrets** for sensitive configuration

## Troubleshooting

### Database connection issues:
- Ensure PostgreSQL container is healthy: `docker-compose ps`
- Check database logs: `docker-compose logs postgres`

### Application not starting:
- Check application logs: `docker-compose logs app`
- Verify environment variables are set correctly
- Ensure database is ready before application starts (healthcheck should handle this)

### Port conflicts:
- Change ports in `docker-compose.yml` if 9090 or 5432 are already in use

