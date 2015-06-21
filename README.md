# pfxplorer
An API for exploring Pitch F/X data

## Setup
This API uses [spring-boot](http://projects.spring.io/spring-boot/) for minimal configuration.  No manual library, classpath, or web server setup is required.

### Requirements
- Java 7
- MySQL 5

A sample database [database.sql.gz](https://github.com/jparish9/pfxplorer/blob/master/database.sql.gz) is included for immediate use, which includes pitchers and all pitches from July through September 2014.  Simply unzip it and run `source database.sql` at the mysql prompt.

## First Run

A pre-built jar is supplied in the root directory.  It can also be rebuilt from source using `gradle build`, in which case the jar can be found under `/build/libs`.

Run `java -jar pfxplorer-1.0.0.jar` from a command prompt in the project root directory.  After a few seconds of startup configuration messages, a successful startup will display a message such as
```
[main] com.jdp.pfxplorer.Application: Started Application in 6.085 seconds (JVM running for 6.511)
```
and the application is up and running.  A webserver should now be running on `http://localhost:8080`.

## Endpoints

The following endpoints are supported.

- `/pitchers/search?name=` - search for a pitcher by any part of their first or last name.  Matching pitchers are returned with their MLB IDs, which are needed for all other endpoints.
- `/pitchers/[ID]` - return summary statistics about pitches thrown by the pitcher with the given MLB ID.
- `/pitchers/[ID]/[pitch_type]` - return detailed statistics about all pitches thrown by the pitcher with the given MLB ID and specific pitch type.  Pitch types are two-letter abbreviations such as `FF` for a four-seam fastball.  A list of all pitch types can be found at [fangraphs.com](http://www.fangraphs.com/library/pitch-type-abbreviations-classifications/).

Both pitch statistics endpoints support the following optional query parameters to filter what pitches are reported on:
- `year` - filter pitches to only those thrown in a specific year, e.g. `year=2012`
- `month` - filter pitches to only those thrown in a specific month, e.g. `month=4` for April.
- `count` - filter pitches to only those thrown on a specific count, e.g. `count=3-2`.
- `balls` - filter pitches to only those thrown on counts with a specific number of balls, e.g. `balls=3`.  If `count` parameter is supplied this will be ignored.
- `strikes` - filter pitches to only those thrown on counts with a specific number of strikes, e.g. `strikes=2`.  If `count` parameter is supplied this will be ignored.
- `inning` - filter pitches to only those thrown in a specific inning, e.g. `inning=8`.

#### Examples

- [/pitchers/search?name=perkins](http://localhost:8080/pitchers/search?name=perkins) searches for pitchers whose name contains `perkins`.
- [/pitchers/450282](http://localhost:8080/pitchers/450282) returns summary statistics on all pitches thrown by Glen Perkins.
- [/pitchers/450282/FF?year=2014&count=3-2](http://localhost:8080/pitchers/450282/FF?year=2014&count=3-2) returns detailed statistics on four-seam fastballs thrown by Glen Perkins in 2014 when the count was 3-2.

## Importing additional data

The application supports two command line modes for importing additional data.

As new players join the MLB, they can be imported with a simple CSV format where the first two columns are their MLB ID and full name.  To import new players on application startup, run:
```
java -jar pfxplorer-1.0.0.jar loadpitchers [filename]
```
If no `filename` is supplied, the player CSV will be downloaded from [crunchtimebaseball.com](http://crunchtimebaseball.com/baseball_map.html).  This will remove all existing players before importing from the spreadsheet.

To import additional Pitch F/X data, this command line mode is supported:
```
java -jar pfxplorer-1.0.0.jar loadpfx [year] [month]
```
If no `month` is specified, all regular season pitches will be imported for the whole year.
If no `month` or `year` is supported, all regular season pitches from 2008-2015 will be imported.
This import is non-destructive and does duplicate checking, so it is safe to run over the same date range multiple times.
