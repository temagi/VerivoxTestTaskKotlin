## Requirements:

For local run required:
* Java 11+ (with proper set JAVA_HOME)
* Gradle 8+

or for run in container 
* Docker

## How to run:

### Local run:
```agsl
./gradle clean test
```

### Build docker image and run in container:
```agsl
docker build -t verivox-test .
docker run verivox-test
```

### Run in docker container:
```agsl
docker run -it -v .:/app -w /app amazoncorretto:21 ./gradlew clean test
```

### Run tests via script (docker required):
Script will execute tests and open HTML test report in browser.
#### Windows:
```agsl
run.bat
```

#### Linux / MacOS
```agsl
./run.sh
```

### Test report
Test report available in `build/reports/tests/test/index.html`

## Bug:

As I know, German postcodes should be in range `01067 – 99998`
I decide to check numbers our of range and the code `99999` returns an error, as expected
But the code `01066` returns a success (Dresden, as well as `01067`) - https://service.verivox.de/geo/latestv2/cities/01066/.
I'm not sure, but it looks like a bug, because at least for the streets request it returns an error - https://service.verivox.de/geo/latestv2/cities/01066/Dresden/streets
