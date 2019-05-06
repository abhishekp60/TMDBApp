package myapp.tmdb;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MovieListActivity extends AppCompatActivity {
ImageView imageView;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    public String poster_url = "https://image.tmdb.org/t/p/w500_and_h282_face";
String LOGIN_URL="https://api.themoviedb.org/3/list/1?api_key=094939ec5a0d40a9cc9d58910f17d452&language=en-US";
public static AppDatabase appDatabase;
    byte[] imageBytes;
    Bitmap bitmap;
    ArrayList posterPathList;
    Movies movies;
    JSONObject jsonObject1;
    Bitmap bitmapD = null;
     boolean    network_connected1=false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        try {
            appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Movies").allowMainThreadQueries().build();
//imageView=(ImageView)findViewById(R.id.imageView);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            posterPathList= new ArrayList();

            ConnectivityManager cm1 = (ConnectivityManager) getBaseContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork1 = cm1.getActiveNetworkInfo();
               network_connected1 = activeNetwork1 != null
                    && activeNetwork1.isConnectedOrConnecting();
if(network_connected1) {
     progressDialog = new ProgressDialog(MovieListActivity.this);
    progressDialog.setMessage("loading");
    progressDialog.show();
    fetchMovieData();
}else{
    customAlert();
}
        }catch (Exception e){
            Log.i("LOGINNNNN", "error 11=" + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm1 = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork1 = cm1.getActiveNetworkInfo();
        network_connected1 = activeNetwork1 != null
                && activeNetwork1.isConnectedOrConnecting();
        if(network_connected1){
            fetchMovieData();
        }else{

        }

    }

    public void fetchMovieData(){
    StringRequest stringRequest = new StringRequest(Request.Method.GET, LOGIN_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                         movies = new Movies();
                        for(int i=0;i<jsonArray.length();i++){
                             jsonObject1 = jsonArray.getJSONObject(i);
                            movies.setId(jsonObject1.getInt("id"));
                            movies.setVote_average(jsonObject1.getInt("vote_average"));
                            movies.setTitle(jsonObject1.getString("title"));
                            movies.setOverview(jsonObject1.getString("overview"));
                            movies.setPopularity(jsonObject1.getString("popularity"));

                            movies.setRelease_date(jsonObject1.getString("release_date"));
                            movies.setPoster_path("https://image.tmdb.org/t/p/w500_and_h282_face"+jsonObject1.getString("poster_path"));
                            movies.setBackdrop_path("https://image.tmdb.org/t/p/w500_and_h282_face"+jsonObject1.getString("backdrop_path"));
                            posterPathList.add("https://image.tmdb.org/t/p/w500_and_h282_face"+jsonObject1.getString("poster_path"));


                            appDatabase.appDao().addMovie(movies);

                            new DownloadImage().execute("https://image.tmdb.org/t/p/w500_and_h282_face"+jsonObject1.getString("poster_path"));
                        }


                       downloadPosters();

                    }catch (Exception e){
                        Log.i("LOGINNNNN", "error=" + e.getMessage());

                    }

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  progressDialog.dismiss();

                 //   Log.e("LOGINNNNN ERROR 1","ERROR MESSSWAGEE="+error.toString());
                 //   Toast.makeText(getApplicationContext(), "Internet Connection is not available"+error.getMessage(), Toast.LENGTH_LONG).show();
                }


            }) ;


    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmapD = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmapD;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
try {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmapD.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    imageBytes = baos.toByteArray();
    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

    //    imageView.setImageBitmap(bitmap);
    movies.setPoster(imageBytes);
    appDatabase.appDao().addMovie(movies);

    Log.i("GGGG", "byte=" + imageBytes);

}catch (Exception e){
    Log.i("GGGG", "error=" + e.getMessage());
}

        }
    }

public void downloadPosters(){
try{

MovieListAdapter movieListAdapter = new MovieListAdapter(MovieListActivity.this,"n");
recyclerView.setAdapter(movieListAdapter);
    progressDialog.dismiss();
}catch (Exception e){
    progressDialog.dismiss();
    Log.i("LOGINNNNN", "error 3333=" + e.getMessage());
}
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                // do something
                showAlert();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void showAlert(){
        String[] listItems = {"Date","Rating"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MovieListActivity.this);
       // AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialog));
        mBuilder.setTitle("Sort By");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //  mResult.setText(listItems[i]);
                if(i==0){
                    MovieListAdapter movieListAdapter = new MovieListAdapter(MovieListActivity.this,"d");
                    recyclerView.setAdapter(movieListAdapter);
                }else if(i ==1){
                    MovieListAdapter movieListAdapter = new MovieListAdapter(MovieListActivity.this,"r");
                    recyclerView.setAdapter(movieListAdapter);
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public void customAlert(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MovieListActivity.this);
        View view = LayoutInflater.from(MovieListActivity.this).inflate(R.layout.custom_layout, null);

        TextView title = (TextView) view.findViewById(R.id.title);

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
        title.setText("No Internet!");
        imageButton.setImageResource(R.drawable.wifi);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setView(view);
        builder.show();
    }

}
