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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MyViewHolder> {
    List<Movies> moviesList;
    private ArrayList<Movies> dataSet;
    Context c;

    public MovieDetailAdapter(Context context, int pos) {
        c = context;
        moviesList = MovieListActivity.appDatabase.appDao().getMovieDetails(pos);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRating;
        TextView textViewTitle;
        TextView textViewTime;
        TextView textViewDetails;
        TextView textViewPopularity;
        ImageView imageViewIcon;
        ImageView imageTop;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewRating = (TextView) itemView.findViewById(R.id.ratings);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.title);
            this.textViewTime = (TextView) itemView.findViewById(R.id.time);
            this.textViewDetails = (TextView) itemView.findViewById(R.id.detail);
            this.textViewPopularity = (TextView) itemView.findViewById(R.id.popularity);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.logoImageView);

        }
    }

    public MovieDetailAdapter(ArrayList<Movies> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_detail_card_layout, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
try {
    byte[] img = moviesList.get(listPosition).getPoster();
    Log.i("ZZZZ DETAIL", "img array=" + img);
//   Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);


    TextView textViewRating = holder.textViewRating;
    TextView textViewTime = holder.textViewTime;
    TextView textViewDetails = holder.textViewDetails;
    TextView textViewTitle = holder.textViewTitle;
    TextView textViewPopularity = holder.textViewPopularity;
    ImageView imageView = holder.imageViewIcon;

    textViewRating.setText(Integer.toString(moviesList.get(listPosition).getVote_average()));

    String date = moviesList.get(listPosition).getRelease_date();
    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
    Date newDate = spf.parse(date);
    spf = new SimpleDateFormat("dd-MM-yyyy");
    date = spf.format(newDate);


    textViewTime.setText(date);
    textViewDetails.setText(moviesList.get(listPosition).getOverview());
    textViewTitle.setText(moviesList.get(listPosition).getTitle());
    textViewPopularity.setText(moviesList.get(listPosition).getPopularity());

    Picasso.with(c).load(moviesList.get(listPosition).getPoster_path()).into(imageView);
    // Picasso.with(c).load(moviesList.get(listPosition).getBackdrop_path()).into(holder.imageTop);

    //  imageView.setImageBitmap(bitmap);
}catch (Exception e){

}
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
