docker run -it -v .:/app -w /app amazoncorretto:21 ./gradlew clean test

open build/reports/tests/test/index.html