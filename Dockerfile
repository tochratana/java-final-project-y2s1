FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy compiled classes
COPY out/production/skill-exchange-system /app

# Copy runtime libraries (PostgreSQL needed)
COPY libs /app/libs

# Run application
CMD ["java", "-cp", ".:libs/*", "PlatformApp"]
