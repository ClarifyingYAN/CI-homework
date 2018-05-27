package com.clary;

import java.util.ArrayList;
import java.util.List;

public class Ant {
    private List<Integer> restCity;
    private List<Integer> visitedCity;

    public Ant(int initCity, int cityCnt) {
        restCity = new ArrayList<>();
        visitedCity = new ArrayList<>();

        visitedCity.add(initCity);
        for (int i=0; i<cityCnt; ++i)
            if (initCity!=i)
                restCity.add(i);
    }

    public void visitCity(int city) {
        for (int i=0; i<restCity.size(); ++i) {
            if (restCity.get(i).equals(city)) {
                restCity.remove(i);
                break;
            }
        }
        visitedCity.add(city);
    }

    public List<Integer> getRestCity() {
        return restCity;
    }

    public List<Integer> getVisitedCity() {
        return visitedCity;
    }
}
