package edu.cmu.idrift0605.Utilities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by madturtle on 2015/7/29.
 * A utility class specific for drawing route on map.
 */
//public class DrawRouteTask extends AsyncTask<String, String,String>{
public class DrawRouteTask extends AsyncTask<Void, Void, String>{

    GoogleMap mMap;
    LatLng startPosition;
    LatLng endPosition;

    /* Use constructor for passing variable instead of params in doInBackground because we need different input type */
    public DrawRouteTask(GoogleMap map, LatLng p1, LatLng p2){
        mMap = map;
        startPosition = p1;
        endPosition = p2;
    }



    @Override
    protected String doInBackground(Void... params) {
        String url = getMapsApiDirectionsUrl();
        String data = "";
        try {
            HttpConnection http = new HttpConnection();
            data = http.readUrl(url);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new ParserTask().execute(result);
    }



    private String getMapsApiDirectionsUrl() {
//        String waypoints = "waypoints=optimize:true|"
//                + startPosition.latitude + "," + startPosition.longitude + "|"
//                + endPosition.latitude + "," + endPosition.longitude;
        String route =
                "origin=" + startPosition.latitude + "," + startPosition.longitude + "&" +
                "destination=" + endPosition.latitude + "," + endPosition.longitude;
        String sensor = "sensor=false";
        String params = route + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            /* Make sure app doesn't crash if route not found */
            if(routes==null) return;

            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }



}
