package com.example.popularmovieapp1;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieDAO movieDAO;
    private LiveData<List<Movie>> movieFavLiveList;
    private LiveData<List<Movie>> movieAllLiveList;
    private LiveData<List<Movie>> allMoviesLiveData;
    private MutableLiveData<String> filterMutableLiveData = new MutableLiveData<>();


    public MovieViewModel(@NonNull Application application) {
        super(application);

        movieDAO = MovieRoomDatabase.getInstance(application).MovieDao();
    }

    public LiveData<List<Movie>> getAllMovieData() {
        if (movieAllLiveList == null) {
            filterMutableLiveData.setValue("Popular");

            //using switchMap to implement output based on options selected
            movieAllLiveList = Transformations.switchMap(filterMutableLiveData, this::selectFilterOption);
        }
        return movieAllLiveList;
    }

    public void UpdateFilter(String newFilter) {
        filterMutableLiveData.setValue(newFilter);
    }

    //Defining cases for selecting options
    public LiveData<List<Movie>> selectFilterOption(String filter) {
        switch (filter) {
            case "Popular":
                return movieDAO.getPopularMoviesOrder();
            case "TopRated":
                return movieDAO.getTopRatedMoviesOrder();
            case "Favourite":
                return movieDAO.getAllFavouriteMovies();
            default:
                return null;
        }
    }

}
