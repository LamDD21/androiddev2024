package vn.edu.usth.weather;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.media.MediaPlayer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.InputStream;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void OnS() {
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        mediaPlayer.start();


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(WeatherAndForecastFragment.newInstance(), "Ha Noi,Viet Nam");
        adapter.addFragment(WeatherAndForecastFragment.newInstance(), "Paris,France");
        adapter.addFragment(WeatherAndForecastFragment.newInstance(), "Thuong Hai, China ");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {

            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


