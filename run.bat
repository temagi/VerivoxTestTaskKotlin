call  docker run -it -v .:/app -w /app amazoncorretto:21 ./gradlew clean test
start /wait "" "build/reports/tests/test/index.html"
