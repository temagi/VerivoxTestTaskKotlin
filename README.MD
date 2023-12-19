## How to run:
```agsl
gradle clean test
```

## Bug:

As I know, German postcodes should be in range 01067 – 99998
I decide to check numbers our of range and the code 99999 returns an error.
But the code 01066 returns a success (Dresden, as well as 01067) - https://service.verivox.de/geo/latestv2/cities/01066/.
I'n not sure, but it looks like a bug, because at least for the streets request it returns an error - https://service.verivox.de/geo/latestv2/cities/01066/Dresden/streets

## TODO:

- fix CI config
- add reporting tool
- add docker configuration
- prettify code
- ~~prettify model~~
- ~~update readme~~
- ~~add linter~~