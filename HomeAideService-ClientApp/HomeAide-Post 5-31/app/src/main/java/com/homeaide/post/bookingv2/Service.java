package com.homeaide.post.bookingv2;

public class Service {
    private String id;
    private String name;
    private String type;
    private double ratePerHour;

    public Service(){
        this.id = "";
        this.name = "";
        this.type = "";
        this.ratePerHour = 0;
    }

    public Service(String id, String name, String type, double ratePerHour){
        this.id = id;
        this.name = name;
        this.type = type;
        this.ratePerHour = ratePerHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    @Override
    public boolean equals(Object o){
        try {
            Service s = (Service) o;
            return (this.getId().equals(s.getId()));
        } catch (Exception e){
            return (this == o);
        }
    }

    @Override
    public String toString(){
        return String.format("%s - %s",type,name);
    }
}
