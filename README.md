# Task 2 - Airports and stations

## Description

The goal of this task is to code a REST API project that expose the following APIs:
Given an airport, it will find all the closest observation stations
Given an observation station, it will find all the closest airports
Here is an example of the APIs we want to expose:

`GET /api/fabrick/v1.0/airports/{airportId}/stations?closestBy={value?0.0}`

`[
{
"id": "KAFF",
"site": "Air Force Academy Arfld",
"state": "CO",
"country": "US",
"latitude": 38.971,
"longitude": -104.816,
"elevation:: 2003
},
...
]`

`GET /api/fabrick/v1.0/stations/{stationId}/airports?closestBy={value?0.0}`

`[
{
"id": "KDEN",
"name: "DENVER/DENVER_INTL",
"state": "CO",
"country": "US",
"latitude": 39.8617,
"longitude": -104.6732,
"elevation": 1656.6
},
...
]`

You can rely on the official Aviation Weather Center Open API; have a look at the APIs Airport Info and Station Info

## Constraints

The query parameter closestBy is optional, by default 0.0
airportId and stationId are the ICAO Airport Codes
Please note that closestBy should be used as the bounding box modifier, so for example if I send closestBy=1.0
and I have latitude=2.0 and longitude=2.0, the bounding box should be (1.0, 1.0, 3.0, 3.0)

## Bonus points

Implement unit testing using some mocking stub framework
Implement a local cache or database layer; please note that the database layer should be bootstrapped by the
application (we don't want to deal with external systems)