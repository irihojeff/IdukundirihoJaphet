# Use a base Java image
FROM openjdk:17-slim

# Set your project ID as an environment variable
ENV PROJECT_ID="27066"

# Create and set the working directory
WORKDIR /app

# Copy your repository files into the Docker image
COPY . /app/

# Compile your Java code
RUN find . -name "*.java" -exec javac {} \;

# Include a command to display your project ID when the container runs
CMD ["sh", "-c", "echo Project ID: $PROJECT_ID && ls -la"]