# ah-advert-api
Test restful api with Scala and akka-http

## Get sources
You should clone the [repo](https://github.com/fredfredfred/ah-advert-api) and install the latest postgresql database on your system. You can use these steps:
* clone repo: ```git clone https://github.com/fredfredfred/ah-advert-api.git```

## Install postgresql
* install postgresql: ```brew install postgresql``` (on MacOs)

## Create DBs
Either run ```scripts/dbCreate.sh ``` or 
* create DB: ```createdb advert```
* create test DB: ```createdb advert_test```

## Start database
* ```scripts/dbStart.sh```

## Compile
* ```sbt compile```

