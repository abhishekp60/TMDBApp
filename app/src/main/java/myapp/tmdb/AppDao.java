package myapp.tmdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     void addMovie(Movies movies);

@Query("select * from Movies")
     List<Movies> getMovies();

    @Query("select * from Movies order  by  release_date desc")
    List<Movies> getMoviesBYDate();

    @Query("select * from Movies order  by vote_average desc")
    List<Movies> getMoviesByRating();


    @Query("select * from Movies where id=:id")
    List<Movies> getMovieDetails(int id);
}
