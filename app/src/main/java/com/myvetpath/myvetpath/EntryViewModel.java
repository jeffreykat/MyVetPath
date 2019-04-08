package com.myvetpath.myvetpath;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import com.myvetpath.myvetpath.data.CategoryItem;
import com.myvetpath.myvetpath.data.EntryRepository;
import com.myvetpath.myvetpath.data.PlanetItem;
import com.myvetpath.myvetpath.data.Status;

import java.util.List;

/*
 * This is the ViewModel class for data. The ViewModel class uses a Repository class to actually perform data operations.
 */
public class EntryViewModel extends ViewModel {

    private LiveData<Status> mLoadingStatus;
    private EntryRepository mRepository;
    private String mCategory;

    private LiveData<List<CategoryItem>> mCategoryItems;

    private LiveData<List<PlanetItem>> mPlanet;


    public EntryViewModel() {
        mRepository = new EntryRepository();
        mLoadingStatus = mRepository.getLoadingStatus();
        mCategory = mRepository.getCategory();

        mPlanet = mRepository.getPlanets();

    }

    public LiveData<List<CategoryItem>> getCategoryItems(String category){
        if(category.equals("Planets")){

        }
        return mCategoryItems;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    public String getCategory(){
        return mCategory;
    }


    public LiveData<List<PlanetItem>> getPlanet(){return mPlanet;}



    public void loadCategoryItems(String category, String next){
        mRepository.loadCategory(category, next);
    }

}
