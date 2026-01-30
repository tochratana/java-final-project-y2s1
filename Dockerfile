FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy compiled classes from IntelliJ output
COPY out/production/skill-exchange-system /app

# Run your main class (NO .java, NO .class)
CMD ["java", "PlatformApp"]
