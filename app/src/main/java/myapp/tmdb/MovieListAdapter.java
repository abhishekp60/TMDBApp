package myapp.tmdb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    static List<Movies> moviesList;
    private ArrayList<Movies> dataSet;
    static Context c;
String sort_by;
    public MovieListAdapter(Context context,String sort_by){
        this.sort_by=sort_by;
        c=context;
        if(sort_by.equals("n")) {
            moviesList = MovieListActivity.appDatabase.appDao().getMovies();
        }
        else if(sort_by.equals("d")){
            moviesList = MovieListActivity.appDatabase.appDao().getMoviesBYDate();
        }else if(sort_by.equals("r")){
            moviesList = MovieListActivity.appDatabase.appDao().getMoviesByRating();
        }
 Log.i("GGGGG","moview="+moviesList.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
this.ratingBar=(RatingBar)itemView.findViewById(R.id.ratingbar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    int id = moviesList.get(pos).getId();
                    Log.i("TTTTT","position="+pos);
                    Intent i = new Intent(c,MovieDetailActivity.class);
                    i.putExtra("id",id);
                    c.startActivity(i);
                }
            });
        }
    }

    public MovieListAdapter(ArrayList<Movies> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movielist_card_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {



        byte img[]=moviesList.get(listPosition).getPoster();
        Log.i("ZZZZ","img array="+img);
        String imgenc=moviesList.get(listPosition).getImage();


//   Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);


        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        RatingBar ratingBar = holder.ratingBar;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(moviesList.get(listPosition).getTitle());
        textViewVersion.setText(moviesList.get(listPosition).getOverview());
        ratingBar.setRating(Float.parseFloat(String.valueOf(moviesList.get(listPosition).getVote_average())));
      //  ratingBar.setRating(Float.parseFloat(String.valueOf("1")));

        Picasso.with(c).load(moviesList.get(listPosition).getBackdrop_path()).into(imageView);

      //  imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
