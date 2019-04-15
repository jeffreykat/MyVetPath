package com.myvetpath.myvetpath;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import com.myvetpath.myvetpath.data.CategoryItem;
import com.myvetpath.myvetpath.data.EntryRepository;
import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.PlanetItem;
import com.myvetpath.myvetpath.data.ReplyTable;
import com.myvetpath.myvetpath.data.ReportTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.Status;
import com.myvetpath.myvetpath.data.SubmissionTable;
import com.myvetpath.myvetpath.data.UserTable;

import java.util.List;

/*
 * This should be used to make actual queries using the API within activities
 * This is the ViewModel class for data. The ViewModel class uses a Repository class to actually perform data operations.
 */
public class EntryViewModel extends ViewModel {

    private LiveData<Status> mLoadingStatus;
    private EntryRepository mRepository;
    private String mCategory; //the category is the query type (ex: "submissions", "users")

    private LiveData<List<CategoryItem>> mCategoryItems;

    private LiveData<List<PlanetItem>> mPlanet; //this is mostly just to test that we can use an API, won't actually be used
    private LiveData<List<GroupTable>> mGroup;
    private LiveData<List<PatientTable>> mPatient;
    private LiveData<List<PictureTable>> mPicture;
    private LiveData<List<ReplyTable>> mReply;
    private LiveData<List<ReportTable>> mReport;
    private LiveData<List<SampleTable>> mSample;
    private LiveData<List<SubmissionTable>> mSubmission;
    private LiveData<List<UserTable>> mUser;


    public EntryViewModel() {
        mRepository = new EntryRepository();
        mLoadingStatus = mRepository.getLoadingStatus();
        mCategory = mRepository.getCategory();

        mPlanet = mRepository.getPlanets();

        mGroup = mRepository.getGroups();
        mPatient = mRepository.getPatients();
        mPicture = mRepository.getPictures();
        mReply = mRepository.getReplies();
        mReport = mRepository.getReports();
        mSample = mRepository.getSamples();
        mSubmission = mRepository.getSubmissions();
        mUser = mRepository.getUsers();


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


    //This will tell the app what kind of API query to make. Category should be the route (such as "users" or "submissions"), next is only used
    //if the API returns paginated results. If it doesn't, then it can be null
    public void loadCategoryItems(String category, String next){
        mRepository.loadCategory(category, next);
    }

    public LiveData<List<GroupTable>> getGroup() {
        return mGroup;
    }

    public LiveData<List<PatientTable>> getPatient() {
        return mPatient;
    }

    public LiveData<List<PictureTable>> getPicture() {
        return mPicture;
    }

    public LiveData<List<ReplyTable>> getReply() {
        return mReply;
    }

    public LiveData<List<ReportTable>> getReport() {
        return mReport;
    }

    public LiveData<List<SampleTable>> getSample() {
        return mSample;
    }

    public LiveData<List<SubmissionTable>> getSubmission() {
        return mSubmission;
    }

    public LiveData<List<UserTable>> getUser() {
        return mUser;
    }
}
