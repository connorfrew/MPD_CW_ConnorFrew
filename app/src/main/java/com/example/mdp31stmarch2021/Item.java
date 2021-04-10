package com.example.mdp31stmarch2021;
/// NAME:Connor Frew  ////
/// Student Number: S1705548 ////

import java.io.Serializable;

public class Item implements Serializable, Comparable {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String lat;
    private String lon;
    private String magnitude;
    private String location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {

        this.description = description;
        String[] result = description.split(";");
        for(int i = 0; i <result.length; i++) {

            if(i==1){
                setLocation((result[1]));
            }
            else if (i==4){
                setMagnitude((result[4]));
            }
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLat() {return lat;}

    public void setLat(String lat) {this.lat = lat; }

    public String getLon() { return lon;}

    public void setLon(String lon) {this.lon = lon;}

    public String getLocation(){return location;}

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }


    @Override
    public String toString() {
        return new String ((new StringBuilder())
                .append(location)
                .append("\n")
                .append(magnitude));
    }

    @Override
    public int compareTo(Object o) {
       Item item = (Item)o;
       String quakeMagnitudeA = item.getMagnitude().substring(11);
       String quakeMagnitudeB = this.getMagnitude().substring(11);
       Float floatQuakeMagnitudeA = Float.parseFloat(quakeMagnitudeA);
       Float floatQuakeMagnitudeB = Float.parseFloat(quakeMagnitudeB);
       return floatQuakeMagnitudeA.compareTo(floatQuakeMagnitudeB);

    }
}
