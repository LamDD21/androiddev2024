package vn.edu.usth.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "WeatherFragment";
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=Hanoi&units=metric&appid=" + API_KEY;

    private RequestQueue requestQueue;
    private TextView temperatureText;
    private TextView descriptionText;
    private TextView humidityText;
    private ImageView weatherIcon;
    public WeatherFragment() {



    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);


        temperatureText = view.findViewById(R.id.temperature);
        descriptionText = view.findViewById(R.id.description);
        humidityText = view.findViewById(R.id.humidity);
        weatherIcon = view.findViewById(R.id.weather_icon);


        requestQueue = Volley.newRequestQueue(requireContext());


        getWeatherData();

        return view;
    }

    private void getWeatherData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                WEATHER_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject main = response.getJSONObject("main");
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject weatherObject = weather.getJSONObject(0);


                            double temp = main.getDouble("temp");
                            String temperature = String.format("%.1f°C", temp);


                            int humidity = main.getInt("humidity");

                            String description = weatherObject.getString("description");


                            String iconCode = weatherObject.getString("icon");
                            String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";


                            updateUI(temperature, description, humidity, iconUrl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error parsing weather data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching weather data: " + error.getMessage());
                        Toast.makeText(getContext(),
                                "Error fetching weather data",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );


        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(String temperature, String description, int humidity, String iconUrl) {
        temperatureText.setText(temperature);
        descriptionText.setText(description);
        humidityText.setText("Humidity: " + humidity + "%");


        ImageRequest imageRequest = new ImageRequest(
                iconUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        weatherIcon.setImageBitmap(bitmap);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error loading weather icon: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(imageRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
}