package vn.edu.usth.weather;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

        MediaPlayer();
        simulateNetworkRequest();



    }

    private void MediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        mediaPlayer.start();
    }

@Override
        protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
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
        switch (item.getItemId()) {
            case R.id.action_refresh:

                Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.action_settings:

                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void simulateNetworkRequest() {

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

                String content = msg.getData().getString("server_response");
                Toast.makeText(WeatherActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        };


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Bundle bundle = new Bundle();
                bundle.putString("server_response", " Data retrieved");


                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }
}
