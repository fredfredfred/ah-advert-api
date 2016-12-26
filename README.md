# ah-advert-api
Test restful api with Scala and akka-http

## Get sources
Clone the [repo](https://github.com/fredfredfred/ah-advert-api) 
    
    git clone https://github.com/fredfredfred/ah-advert-api.git

## Install postgresql
Install the latest postgresql database. I used version 9.6.1. On MacOS you can use homebrew:

    brew install postgres

## Create DBs
Either run ```scripts/dbCreate.sh ``` or 
* create DB: ```createdb advert```
* create test DB: ```createdb advert_test```

## Start database
    scripts/dbStart.sh

## Create schema
    sbt flywayMigrate

## Compile
    sbt compile
    
## Run
Make sure port 9000 is not used!

    sbt run


## Known limitations
* The find all adverts really returns all of them. In a production scenario this should be
 somehow limited, for example with a paged access.
* There is no authentication => Should be secured in some way in production
