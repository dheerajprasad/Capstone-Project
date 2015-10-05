package sample.com.horoscope;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "myFetchService";
    private static final String ns = null;
    public static  String userSelectedType = "";
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);


        collapsingToolbar.setTitle("Horoscope");
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.ltgray));

        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.pink_a200));







        initRecyclerView();



    }


        public void moveToNextpage(){


            Intent intent = new Intent(this, HoroscopeDetail.class);
            this.startActivity ( intent );
        }
    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> messages = Arrays.asList(getResources().getString(R.string.Horoscopefortheday), getResources().getString(R.string.Horoscopefortheweek), getResources().getString(R.string.Horoscopeforthemonth),getResources().getString(R.string.Horoscopefortheyear), getResources().getString(R.string.Contactus));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(messages, this);
        recyclerView.setAdapter(recyclerAdapter);

    }

}
