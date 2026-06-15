# Artsie Web Service

Backend service for a photography and video portfolio, built with Spring Boot 3.x and deployed to AWS ECS Fargate.

## Modules

| Module | Purpose |
|--------|---------|
| `artsie-domain` | JPA entities (Photo, Video, Album, Tag, AdminUser) and Spring Data repositories |
| `artsie-storage` | AWS S3 integration — upload, delete, presigned URL generation |
| `artsie-api` | Public REST endpoints for the gallery (read-only, no auth) |
| `artsie-admin` | Secured CMS endpoints for managing content (auth required) |
| `artsie-app` | Main Spring Boot application — assembles all modules, config, Dockerfile |

## Prerequisites

- Java 17+
- Maven 3.9+
- Docker (for containerized deployment)
- AWS account with S3 bucket and RDS PostgreSQL (for production)

## Local Development

```bash
# Build all modules
mvn clean install

# Run with H2 in-memory database (default profile)
cd artsie-app
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`. The H2 console is at `http://localhost:8080/h2-console`.

## Docker

```bash
# Build the image
docker build -t artsie-web-service .

# Run locally
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=host.docker.internal \
  -e DB_NAME=artsie \
  -e DB_USERNAME=artsie \
  -e DB_PASSWORD=yourpassword \
  -e S3_BUCKET_NAME=your-bucket \
  -e AWS_REGION=us-east-1 \
  artsie-web-service
```

## API Endpoints

### Public Gallery
- `GET /api/albums` — list published albums
- `GET /api/albums/{id}` — get album details
- `GET /api/albums/{id}/photos` — list photos in an album
- `GET /api/photos/{id}` — get a single photo
- `GET /api/photos?tag=nature` — list photos by tag

### Admin CMS (authenticated)
- `POST /api/admin/albums` — create album
- `PUT /api/admin/albums/{id}` — update album
- `DELETE /api/admin/albums/{id}` — delete album
- `POST /api/admin/photos` — upload photo (multipart)
- `DELETE /api/admin/photos/{id}` — delete photo
- `PATCH /api/admin/photos/{id}/publish` — toggle publish status
