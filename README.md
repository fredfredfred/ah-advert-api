# ah-advert-api
This is just a test implementation of a very simple restful api with Scala and akka-http which allows you
to create, read update and delete car adverts. The data is stored using slick and postgres. The adverts look like this:

      {
        "id": 3,
        "title": "Audi A4 Avant",
        "mileage": 37000,
        "price": 11750,
        "fuel": "GASOLINE",
        "new": false,
        "firstRegistration": "2012-12-25"
      }


## Get sources
Clone the [repo](https://github.com/fredfredfred/ah-advert-api) 
    
    git clone https://github.com/fredfredfred/ah-advert-api.git

## Prerequisites
You must have Java > 1.8, Scala > 2.11.8, Sbt > 13.11, Postgres > 9.6.1 (see next step) installed.

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
    
## Test
    sbt test
    
## Run
Make sure port 9000 is not used or configure it in application.conf (http.port).

    sbt run

## API
    GET     /advert                 Gets a list of all adverts
    GET     /advert?sort=price&order=asc  
                                    Gets a list of all adverts sorted by price ascending
                                      sort: id (default) | title | fuel | price | mileage | firstRegistration
                                      order: asc (default) | desc
    GET     /advert/:id             Gets the advert with the identifier :id
    POST    /advert                 Creates a new advert and returns the id
    PUT     /advert/:id             Updates the advert with the identifier :id if found
                                      or creates a new one and returns the id (like POST)
    DELETE  /advert/:id             Deletes the advert with the identifier :id
## Test with curl
GET     /advert
 
    curl -X GET -H "Content-Type: application/json" -H "Cache-Control: no-cache" "http://localhost:9000/advert"
return code on success: 200    
    
GET     /advert?sort=price&asc

return code on success: 200

GET     /advert/1

    curl -X GET -H "Content-Type: application/json" -H "Cache-Control: no-cache" "http://localhost:9000/advert/1"
return code on success: 200

POST    /advert

The id is not relevant and will be generated

    curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{
        "id": 3,
        "title": "Audi A4 Avant",
        "mileage": 37000,
        "price": 11750,
        "fuel": "GASOLINE",
        "new": false,
        "firstRegistration": "2012-12-25"
      }' "http://localhost:9000/advert"
return code on success: 201


PUT     /advert/1

    curl -X PUT -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{
      "mileage": 1,
      "price": 1,
      "fuel": "GASOLINE",
      "id": 1,
      "new": true,
      "firstRegistration": "2016-12-01",
      "title": "Title 1111"
    }' "http://localhost:9000/advert/1"
return code on success: 204, 201 (on creation)

DELETE  /advert/1

    curl -X DELETE -H "Content-Type: application/json" -H "Cache-Control: no-cache" "http://localhost:9000/advert/1"
return code on success: 204

## Known limitations
* The find all adverts really returns all of them. In a production scenario this should be
 somehow limited, for example with a paged access.
* There is no authentication => Should be secured in some way in production
* On create the returned data is a plain string instead of JSON containing the id of the created advert