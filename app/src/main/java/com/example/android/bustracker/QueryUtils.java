package com.example.android.bustracker;

import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


/**
 * Created by yishuyan on 9/14/16.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final String key = "AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";
    /**
     * Return a list of {@link Route} objects by parsing out information
     */
    public static Direction extractFeatureFromJson(String base) {


        URI uri = UriComponentsBuilder.fromHttpUrl(base)
                .queryParam("mode", "transit")
                .queryParam("transit_mode", "bus")
                .queryParam("alternatives", "true")
                .build().encode().toUri();

//        URL url = createUrl(requestUrl);
//        String busJSON = null;
//        try {
//            busJSON = makeHttpRequest(url);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error closing input stream", e);
//        }
//        if (TextUtils.isEmpty(busJSON)) {
//            return null;
//        }
//        try {
//            JSONObject baseJsonResponse = new JSONObject(busJSON);
//            JSONArray routeArray = (JSONArray) baseJsonResponse.get("routes");
//            for (int i = 0; i < routeArray.length(); i ++) {
//
//                JSONObject tempObject = (JSONObject) routeArray.get(0);
//                JSONArray legsObject = (JSONArray) tempObject.get("legs");
//                JSONObject tempObject2 = (JSONObject) legsObject.get(0);
//                JSONObject arrivalObject = (JSONObject) tempObject2.get("arrival_time");
//                String arrival_Time = (String) arrivalObject.get("text");
//                System.out.println("arrival_time :" + arrival_Time);
//                JSONObject departureObject =  (JSONObject) tempObject2.get("departure_time");
//                String departure_Time = (String) departureObject.get("text");
//                System.out.println("depart_time : " + departure_Time);
//
//                JSONArray stepsArray = (JSONArray) tempObject2.get("steps");
//                for (int j = 0; j < stepsArray.length(); j ++) {
//                    JSONObject tempStepObject = (JSONObject) stepsArray.get(j);
//                    String mode = (String) tempStepObject.get("travel_mode");
//                    if (mode.equals("TRANSIT")) {
//                        JSONObject transitObject = (JSONObject) tempStepObject.get("transit_details");
//                        JSONObject lineObject = (JSONObject) transitObject.get("line");
//                        String routeName = (String) lineObject.get("short_name");
//                        System.out.println("bus name : " + routeName);
//                        BusInfo busInfo = new BusInfo(departure_Time, arrival_Time, routeName, tempObject.toString());
//                        busList.add(busInfo);
//                    }
//
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Direction> response = restTemplate.getForEntity(
                uri, Direction.class);
        Direction dr = response.getBody();
        return dr;
    }

//    private static URL createUrl(String stringUrl) {
//        URL url = null;
//        try {
//            url = new URL(stringUrl);
//        } catch (MalformedURLException e) {
//            Log.e(LOG_TAG, "Error with creating URL", e);
//        }
//        return url;
//    }
//
//    private static String makeHttpRequest(URL url) throws IOException {
//        String jsonResponse = "";
//
//        if (url == null) {
//            return jsonResponse;
//        }
//        HttpURLConnection urlConnection = null;
//        InputStream inputStream = null;
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setReadTimeout(10000);
//            urlConnection.setConnectTimeout(15000);
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            if (urlConnection.getResponseCode() == 200) {
//                inputStream = urlConnection.getInputStream();
//                jsonResponse = readFromStream(inputStream);
//            } else {
//                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
//            }
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Problem retrieving the book list JSON results.", e);
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        }
//        //Log.e(LOG_TAG,jsonResponse);
//        return jsonResponse;
//    }
//
//    /**
//     * Convert the {@link InputStream} into a String which contains the
//     * whole JSON response from the server.
//     */
//    private static String readFromStream(InputStream inputStream) throws IOException {
//        StringBuilder output = new StringBuilder();
//        if (inputStream != null) {
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
//            BufferedReader reader = new BufferedReader(inputStreamReader);
//            String line = reader.readLine();
//            while (line != null) {
//                output.append(line);
//                line = reader.readLine();
//            }
//        }
//        return output.toString();
//    }
}
