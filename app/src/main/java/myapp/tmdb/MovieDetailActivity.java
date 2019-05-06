package myapp.tmdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class MovieDetailActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
int pos = getIntent().getIntExtra("id",0);
//Toast.makeText(getBaseContext(),"POS="+pos,Toast.LENGTH_LONG).show();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        MovieDetailAdapter movieDetailAdapter = new MovieDetailAdapter(MovieDetailActivity.this,pos);
        recyclerView.setAdapter(movieDetailAdapter);
    }
}
