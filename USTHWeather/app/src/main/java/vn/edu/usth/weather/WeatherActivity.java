package vn.edu.usth.weather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Bitmap usthLogo;


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

        new LogoDownloadTask().execute("http://ict.usth.edu.vn/wp-content/uploads/usth/usthlogo.png");




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



    private class LogoDownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap logoBitmap = null;
            try {
                URL url = new URL(urlDisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                logoBitmap = BitmapFactory.decodeStream(input);
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return logoBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                usthLogo = bitmap; // Lưu logo vào biến toàn cục
                setAppBackground(bitmap); // Đặt ảnh làm nền
            } else {
                Toast.makeText(WeatherActivity.this, "Failed to download logo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Đặt ảnh làm nền
    private void setAppBackground(Bitmap bitmap) {
        ImageView backgroundLogo = findViewById(R.id.background_logo);
        if (backgroundLogo != null) {
            backgroundLogo.setImageBitmap(bitmap);
            backgroundLogo.setAlpha(0.1f); // Điều chỉnh độ mờ để ảnh làm nền chìm
        }
    }

    // Hàm lấy logo đã tải
    public Bitmap getUsthLogo() {
        return usthLogo;
    }
}