package com.myvetpath.myvetpath.utils;

import android.net.Uri;


import com.google.gson.Gson;
import com.myvetpath.myvetpath.data.PlanetItem;

import java.util.ArrayList;

public class StarWarsUtils {
    private final static String BASE_API = "https://swapi.co/api/";
    private final static String PLANETS_API = "planets";


    /*
     * The below several classes are used only for JSON parsing with Gson.
     */
    //TODO: Add Nullable next to results
    static class StarWarsSearchPlanetsResults {
        Integer count;
        String next;
        ArrayList<PlanetItem> results;
    }


    public static String buildStarWarsURL(String category) { //todo add mvp item
//        if(category.equals("Films")){
            return Uri.parse(BASE_API).buildUpon().appendPath(PLANETS_API).build().toString();
//        }
//        else if(category.equals("mvp")) {
//            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(TODO:).build().tosString();
//        }
//        else{
//                return Uri.parse(BASE_API).buildUpon().build().toString();
//            }
        }



    //TODO: figure out how to add next pages to list

    public static ArrayList<PlanetItem> parsePlanetsJSON(String url){
        Gson gson = new Gson();
        //EntryRepository mEntryRepo = new EntryRepository();
        ArrayList<PlanetItem> allPlanets = new ArrayList<>();
        StarWarsSearchPlanetsResults planetsResults = gson.fromJson(url, StarWarsSearchPlanetsResults.class);
        if(planetsResults != null && planetsResults.results != null){
            planetsResults.results.get(planetsResults.results.size()-1).nextUrl = planetsResults.next;
          //  allPlanets.addAll(planetsResults.results);
            /*String next = planetsResults.next;
            if(next != null){
                mEntryRepo.loadCategory("Planets", planetsResults.next);
            }*/
            return planetsResults.results;
        } else {
            return null;
        }
    }

}
