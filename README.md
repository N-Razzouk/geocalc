Java Geocalc ![alt text](https://api.travis-ci.org/grumlimited/geocalc.svg?branch=master "build")
=======

Geocalc is a simple java library aimed at doing arithmetics with Earth coordinates. 
It is designed to be simple to embed in your existing applications and easy to use. Geocalc can:

1. Calculate the distance between two coordinates
2. Find a point at X distance from a standpoint, given a bearing
3. Calculate a rectangular area around a point
4. Determine whether a Point is contained within that area
5. Calculate the azimuth and final bearings between two points

This library is being used on [www.rentbarometer.com](http://www.rentbarometer.com).

This library implements in Java lots of ideas from [Movable-Type](http://www.movable-type.co.uk/scripts/latlong.html). Many thanks.

## Versions

### 0.5.3
* Changed constructors to default and private visibility
* Removed `get...()` keyword out of `EarthCalc` methods 
* `EarthCalc.getDistance()` is now `EarthCalc.gcdDistance()`
* Renamed `BoundingArea.isContainedWithin(...)` to `BoundingArea.contains(...)`

## Installing

### Download

    git clone git@github.com:grumlimited/geocalc.git
    
### Compile
    
    mvn clean install -DskipTests=true

You will need a JDK 1.8 and maven.
    
### Embed

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

#

    <dependency>
	    <groupId>com.github.grumlimited</groupId>
	    <artifactId>geocalc</artifactId>
	    <version>v0.5.2</version>
	</dependency>
	
Please refer to [jitpack.io/#grumlimited/geocalc/0.5.3](https://jitpack.io/#grumlimited/geocalc/0.5.3) for more information

## Usage

### Creating a Point

    //Kew, London
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);

### Converting between systems

Allows conversion of a coordinate between degrees, radians, D-M-s and GPS systems,

    double radians = degreeCoordinate.toRadianCoordinate().radians;
    
    double minutes = degreeCoordinate.toDMSCoordinate().minutes;
    double seconds = degreeCoordinate.toDMSCoordinate().seconds;
    double wholeDegrees = degreeCoordinate.toDMSCoordinate().wholeDegrees;
    
    minutes = degreeCoordinate.toGPSCoordinate().minutes;
    seconds = degreeCoordinate.toGPSCoordinate().seconds; // always 0
    wholeDegrees = degreeCoordinate.toGPSCoordinate().wholeDegrees;
    
back and forth

    Coordinate.fromDegrees(-46.5456)
        .toDMSCoordinate()
        .toGPSCoordinate()
        .toRadianCoordinate()
        .decimalDegrees // toGPSCoordinate() implied loss of precision

### Distance between 2 points

#### Spherical law of cosines

    //Kew, London
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);

    //Richmond, London
    lat = Coordinate.fromDegrees(51.4613418);
    lng = Coordinate.fromDegrees(-0.3035466);
    Point richmond = Point.at(lat, lng);

    double distance = EarthCalc.gcdDistance(richmond, kew); //in meters
    
#### Harvesine formula

    //Kew, London
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);

    //Richmond, London
    lat = Coordinate.fromDegrees(51.4613418);
    lng = Coordinate.fromDegrees(-0.3035466);
    Point richmond = Point.at(lat, lng);

    double distance = EarthCalc.harvesineDistance(richmond, kew); //in meters
    
#### Vincenty formula
    
    //Kew, London
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);

    //Richmond, London
    lat = Coordinate.fromDegrees(51.4613418);
    lng = Coordinate.fromDegrees(-0.3035466);
    Point richmond = Point.at(lat, lng);

    double distance = EarthCalc.vincentyDistance(richmond, kew); //in meters
    
    
### Finding a point at 'distance in meters away' from a standpoint, given a bearing

`otherPoint` will be 1000m away from Kew

    //Kew
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);
    
    //Distance away point, bearing is 45deg
    Point otherPoint = EarthCalc.pointRadialDistance(kew, 45, 1000);
    
### BoundingArea

#### Calculating a rectangular area (BoundingArea) around a point

This is useful when, having a reference point, and a large set of 
other points, you need to figure out which ones are at most, say, 3000 meters away.

While this only gives an approximation, it is several order of magnitude faster
than calculating the distances from each point in the set to the reference point.

      BoundingArea area = EarthCalc.boundingArea(kew, 3000);
      Point nw = area.getNorthWest();
      Point se = area.getSouthEast();
      
Now, given that rectangle delimited by 'nw' and 'se', you can determine which points in your set are within these boundaries.

#### Determining whether a Point is contained within a BoundingArea

Now say you have a BoundingArea,

      //somewhere in Europe, not sure where ;-)
      Point northEast = Point.at(Coordinate.fromDegrees(70), Coordinate.fromDegrees(145));
      Point southWest = Point.at(Coordinate.fromDegrees(50), Coordinate.fromDegrees(110));
      BoundingArea boundingArea = new BoundingArea(northEast, southWest);
      
you can determine whether a point is contained within that area using:
      
      Point point1 = Point.at(Coordinate.fromDegrees(60), Coordinate.fromDegrees(120));
      assertTrue(boundingArea.contains(point1)); //true
      
      Point point2 = Point.at(Coordinate.fromDegrees(45), Coordinate.fromDegrees(120));
      assertFalse(boundingArea.contains(point2)); //false

### Bearing between two points

#### Azimuth bearing - great circle path

    //Kew
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);
    
    //Richmond, London
    lat = Coordinate.fromDegrees(51.4613418);
    lng = Coordinate.fromDegrees(-0.3035466);
    Point richmond = Point.at(lat, lng);
    
    double bearing = EarthCalc.bearing(kew, richmond); //in decimal degrees

#### Azimuth bearing - Vincenty formula

    //Kew
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);
    
    //Richmond, London
    lat = Coordinate.fromDegrees(51.4613418);
    lng = Coordinate.fromDegrees(-0.3035466);
    Point richmond = Point.at(lat, lng);
    
    double bearing = EarthCalc.vincentyBearing(kew, richmond); //in decimal degrees
    
#### Final bearing - Vincenty formula

    //Kew
    Coordinate lat = Coordinate.fromDegrees(51.4843774);
    Coordinate lng = Coordinate.fromDegrees(-0.2912044);
    Point kew = Point.at(lat, lng);
    
    //Richmond, London
    lat = Coordinate.fromDegrees(51.4613418);
    lng = Coordinate.fromDegrees(-0.3035466);
    Point richmond = Point.at(lat, lng);
    
    double bearing = EarthCalc.vincentyFinalBearing(kew, richmond); //in decimal degrees
