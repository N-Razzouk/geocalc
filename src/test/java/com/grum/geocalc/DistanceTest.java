/*
 * Copyright (c) 2012 Romain Gallet
 *
 * This file is part of Geocalc.
 *
 * Geocalc is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Geocalc is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Geocalc. If not, see http://www.gnu.org/licenses/.
 */

package com.grum.geocalc;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author rgallet
 */
public class DistanceTest {

    final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testSphericalLawOfCosinesDistance() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond
        lat = Coordinate.fromDegrees(51.4613418);
        lng = Coordinate.fromDegrees(-0.3035466);
        Point richmond = Point.at(lat, lng);

        assertEquals(2700.3261395525724, EarthCalc.gcdDistance(richmond, kew), 10E-3);
    }

    @Test
    public void testDistanceBnaToLax() {
        Coordinate lat = Coordinate.fromDegrees(36.12);
        Coordinate lng = Coordinate.fromDegrees(-86.97);
        Point BNA = Point.at(lat, lng);

        lat = Coordinate.fromDegrees(33.94);
        lng = Coordinate.fromDegrees(-118.40);
        Point LAX = Point.at(lat, lng);

        assertEquals(2859586.477757082, EarthCalc.gcdDistance(LAX, BNA), 10E-3);
    }

    @Test
    public void testDistanceToBuenosAires() {
        //Kew
        Coordinate lat = new DMSCoordinate(51, 29, 3.7572);
        Coordinate lng = new DMSCoordinate(0, 17, 28.3338);

        Point kew = Point.at(lat, lng);

        //Buenos Aires
        lat = new DMSCoordinate(-34, 36, 35.9994);
        lng = new DMSCoordinate(-58, 22, 11.9994);

        Point buenosAires = Point.at(lat, lng);

        assertEquals(11146, (int) (EarthCalc.gcdDistance(buenosAires, kew) / 1000)); //km
    }

    @Test
    public void testHarvesineDistanceToBuenosAires() {
        //Kew
        Coordinate lat = new DMSCoordinate(51, 29, 3.7572);
        Coordinate lng = new DMSCoordinate(0, 17, 28.3338);

        Point kew = Point.at(lat, lng);

        //Buenos Aires
        lat = new DMSCoordinate(-34, 36, 35.9994);
        lng = new DMSCoordinate(-58, 22, 11.9994);

        Point buenosAires = Point.at(lat, lng);

        assertEquals(11146, (int) (EarthCalc.harvesineDistance(buenosAires, kew) / 1000)); //km
    }

    @Test
    public void testVincentyDistanceToBuenosAires() {
        //Kew
        Coordinate lat = new DMSCoordinate(51, 29, 3.7572);
        Coordinate lng = new DMSCoordinate(0, 17, 28.3338);

        Point kew = Point.at(lat, lng);

        //Buenos Aires
        lat = new DMSCoordinate(-34, 36, 35.9994);
        lng = new DMSCoordinate(-58, 22, 11.9994);

        Point buenosAires = Point.at(lat, lng);

        assertEquals(11120, (int) (EarthCalc.vincentyDistance(buenosAires, kew) / 1000)); //km
    }

    @Test
    public void testSymmetricDistance() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond
        lat = Coordinate.fromDegrees(51.4613418);
        lng = Coordinate.fromDegrees(-0.3035466);
        Point richmond = Point.at(lat, lng);

        assertEquals(EarthCalc.gcdDistance(richmond, kew), EarthCalc.gcdDistance(kew, richmond), 10E-10);
    }

    @Test
    public void testZeroDistance() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        assertEquals(EarthCalc.gcdDistance(kew, kew), 0, 0);
    }

    @Test
    public void testBoundingAreaDistance() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        BoundingArea area = EarthCalc.boundingArea(kew, 3000);

        double northEastDistance = EarthCalc.gcdDistance(kew, area.getNorthEast());
        logger.info("North East => " + northEastDistance);
        assertEquals(3000d, northEastDistance, 1E-3);

        double southWestDistance = EarthCalc.gcdDistance(kew, area.getSouthWest());
        logger.info("South West => " + southWestDistance);
        assertEquals(3000d, southWestDistance, 1E-3);

        Point northWest = area.getNorthWest();
        double northWestDistance = EarthCalc.gcdDistance(kew, northWest);
        logger.info("North West => " + northWestDistance);
        assertEquals(3000d, northWestDistance, 2);

        Point southEast = area.getSouthEast();
        double southEastDistance = EarthCalc.gcdDistance(kew, southEast);
        logger.info("South East => " + southEastDistance);
        assertEquals(3000d, southEastDistance, 2);

        Point middleNorth = Point.at(Coordinate.fromDegrees(area.getNorthEast().latitude),
                Coordinate.fromDegrees((area.getSouthWest().longitude + area.getNorthEast().longitude) / 2));
        double middleNorthDistance = EarthCalc.gcdDistance(kew, middleNorth);
        logger.info("Middle North => " + middleNorthDistance);
        assertEquals(2120d, middleNorthDistance, 1);

        Point middleSouth = Point.at(Coordinate.fromDegrees(area.getSouthWest().latitude),
                Coordinate.fromDegrees((area.getSouthWest().longitude + area.getNorthEast().longitude) / 2));
        double middleSouthDistance = EarthCalc.gcdDistance(kew, middleSouth);
        logger.info("Middle South => " + middleSouthDistance);
        assertEquals(2120d, middleSouthDistance, 2);

        Point middleWest = Point.at(Coordinate.fromDegrees((area.getNorthEast().latitude + area.getSouthWest().latitude) / 2),
                Coordinate.fromDegrees(area.getNorthEast().longitude));
        double middleWestDistance = EarthCalc.gcdDistance(kew, middleWest);
        logger.info("Middle West => " + middleWestDistance);
        assertEquals(2120d, middleWestDistance, 3);

        Point middleEast = Point.at(Coordinate.fromDegrees((area.getNorthEast().latitude + area.getSouthWest().latitude) / 2),
                Coordinate.fromDegrees(area.getSouthWest().longitude));
        double middleEastDistance = EarthCalc.gcdDistance(kew, middleEast);
        logger.info("Middle East => " + middleEastDistance);
        assertEquals(2120d, middleEastDistance, 1);
    }

    @Test
    public void testBoundingAreaNorthPole() {
        //North Pole
        Coordinate lat = Coordinate.fromDegrees(90d);
        Coordinate lng = Coordinate.fromDegrees(0);
        Point northPole = Point.at(lat, lng);

        BoundingArea area = EarthCalc.boundingArea(northPole, 10000);
        logger.info("North East => " + area.getNorthEast());
        logger.info("South West => " + area.getSouthWest());

        assertEquals(89.91006798056583d, area.getNorthEast().getLatitude(), 1);
        assertEquals(90d, area.getNorthEast().getLongitude(), 1);

        assertEquals(89.91006798056583d, area.getSouthEast().getLatitude(), 1);
        assertEquals(90d, area.getSouthEast().getLongitude(), 1);
    }

    @Test
    public void testBoundingAreaNextToLondon() {
        //North Pole
        Coordinate lat = Coordinate.fromDegrees(51.5085452);
        Coordinate lng = Coordinate.fromDegrees(-0.1997387000000117);
        Point northPole = Point.at(lat, lng);

        BoundingArea area = EarthCalc.boundingArea(northPole, 5);
        logger.info("North East => " + area.getNorthEast());
        logger.info("South West => " + area.getSouthWest());

        assertEquals(51.508576995759306d, area.getNorthEast().getLatitude(), 1);
        assertEquals(-0.19968761404347382d, area.getNorthEast().getLongitude(), 1);

        assertEquals(51.50851340421851d, area.getSouthEast().getLatitude(), 1);
        assertEquals(-0.19968761404347382d, area.getSouthEast().getLongitude(), 1);
    }

    @Test
    public void testPointRadialDistanceZero() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        Point sameKew = EarthCalc.pointRadialDistance(kew, 45, 0);
        assertEquals(lat.getDecimalDegrees(), sameKew.latitude, 1E-10);
        assertEquals(lng.getDecimalDegrees(), sameKew.longitude, 1E-10);

        sameKew = EarthCalc.pointRadialDistance(kew, 90, 0);
        assertEquals(lat.getDecimalDegrees(), sameKew.latitude, 1E-10);
        assertEquals(lng.getDecimalDegrees(), sameKew.longitude, 1E-10);

        sameKew = EarthCalc.pointRadialDistance(kew, 180, 0);
        assertEquals(lat.getDecimalDegrees(), sameKew.latitude, 1E-10);
        assertEquals(lng.getDecimalDegrees(), sameKew.longitude, 1E-10);
    }

    @Test
    public void testPointRadialDistance() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond
        lat = Coordinate.fromDegrees(51.4613418);
        lng = Coordinate.fromDegrees(-0.3035466);
        Point richmond = Point.at(lat, lng);

        double distance = EarthCalc.gcdDistance(kew, richmond);
        double bearing = EarthCalc.bearing(kew, richmond);

        Point allegedRichmond = EarthCalc.pointRadialDistance(kew, bearing, distance);

        assertEquals(richmond.latitude, allegedRichmond.latitude, 10E-5);
        assertEquals(richmond.longitude, allegedRichmond.longitude, 10E-5);
    }

    @Test
    public void testBearing() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond, London
        lat = Coordinate.fromDegrees(51.4613418);
        lng = Coordinate.fromDegrees(-0.3035466);
        Point richmond = Point.at(lat, lng);

        System.out.println(EarthCalc.bearing(kew, richmond));
        assertEquals(EarthCalc.bearing(kew, richmond), 198.4604614570758D, 10E-5);
    }

    @Test
    public void testBearingGitHubIssue3() {
        /**
         *
         * https://github.com/grumlimited/geocalc/issues/3
         * standpoint is 31.194326398628462:121.42127048962534
         * forepoint is 31.194353394639606:121.4212814985147
         *
         * bearing is 340.76940494442715
         *
         * but the correct result is 19.213575108209017
         */

        Coordinate lat = Coordinate.fromDegrees(31.194326398628462);
        Coordinate lng = Coordinate.fromDegrees(121.42127048962534);
        Point standpoint = Point.at(lat, lng);

        lat = Coordinate.fromDegrees(31.194353394639606);
        lng = Coordinate.fromDegrees(121.4212814985147);
        Point forepoint = Point.at(lat, lng);

        /**
         * http://www.movable-type.co.uk/scripts/latlong.html
         * returns a bearing of 019°13′50″
         * and not 19.213575108209017
         */
        DMSCoordinate d = new DMSCoordinate(19, 13, 50);
        assertEquals(EarthCalc.bearing(standpoint, forepoint), new DMSCoordinate(19, 13, 50).toDegreeCoordinate().getDecimalDegrees(), 10E-5);
    }

    @Test
    public void testVincentyBearing() {
        //Kew
        Coordinate lat = Coordinate.fromDegrees(51.4843774);
        Coordinate lng = Coordinate.fromDegrees(-0.2912044);
        Point kew = Point.at(lat, lng);

        //Richmond, London
        lat = Coordinate.fromDegrees(51.4613418);
        lng = Coordinate.fromDegrees(-0.3035466);
        Point richmond = Point.at(lat, lng);

        //comparing to results from ttp://www.movable-type.co.uk/scripts/latlong.html
        assertEquals(EarthCalc.vincentyBearing(kew, richmond), new DMSCoordinate(198, 30, 19.58).getDecimalDegrees(), 10E-5);
        assertEquals(EarthCalc.getVincentyFinalBearing(kew, richmond), new DMSCoordinate(198, 29, 44.82).getDecimalDegrees(), 10E-5);
    }
}
