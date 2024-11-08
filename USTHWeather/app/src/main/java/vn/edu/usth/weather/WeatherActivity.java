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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private static final String USTH_LOGO_URL = "http://ict.usth.edu.vn/wp-content/uploads/usth/usthlogo.png";
    private MediaPlayer mediaPlayer;
    private Bitmap usthLogo;
    private RequestQueue requestQueue;


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

        requestQueue = Volley.newRequestQueue(this);
        downloadLogoWithVolley();

        private void downloadLogoWithVolley() {
            ImageRequest imageRequest = new ImageRequest(
                    USTH_LOGO_URL,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            if (bitmap != null) {
                                usthLogo = bitmap;
                                setAppBackground(bitmap);
                            }
                        }
                    },
                    0,
                    0,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error downloading logo: " + error.getMessage());
                            Toast.makeText(WeatherActivity.this,
                                    "Failed to download logo",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(imageRequest);
        }

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



    private void setAppBackground(Bitmap bitmap) {
        ImageView backgroundLogo = findViewById(R.id.background_logo);
        if (backgroundLogo != null) {
            backgroundLogo.setImageBitmap(bitmap);
            backgroundLogo.setAlpha(0.1f);
        }
    }

    public Bitmap getUsthLogo() {
        return usthLogo;
    }
}
