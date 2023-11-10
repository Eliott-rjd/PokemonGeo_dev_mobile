package com.example.pokemongeo.map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Handle the api calls
 */
public class MapAPIManager extends Thread {

    private PokestopListener listener;
    public MapAPIManager(PokestopListener pokestopListener){
        listener = pokestopListener;
    }


    public void run() {
        try {
            callAPIPokestop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }catch (RuntimeException e){
        }
    }

    /**
     * Call the api to get place for the pokestops
     * @throws IOException
     * @throws JSONException
     */
    private void callAPIPokestop() throws IOException, JSONException {
        URL url = new URL("https://overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Bnode%20%5Bshop%3Dbakery%5D%20%2845.76000839079471%2C4.856901168823242%2C45.790659088204684%2C4.902176856994628%29%3B%20out%3B");
        HttpURLConnection connection =(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept","*/*");

        InputStream istream = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(istream);

        BufferedReader br = new BufferedReader(isr);

        JSONObject jsonData = getData(br);

        JSONArray jsonArray= (JSONArray)jsonData.get("elements");


        for (int i=0; i < jsonArray.length(); i++) {
            listener.setupPokestop(
                    new GeoPoint(
                            (double) jsonArray.getJSONObject(i).get("lat")
                            ,(double)jsonArray.getJSONObject(i).get("lon")
                    )
            );

        }

        br.close();
        isr.close();
        istream.close();
        connection.disconnect();
    }


    /**
     * retrun a JSONobject from the data in the buffer
     * @param br
     * @return
     * @throws JSONException
     * @throws IOException
     */
    private JSONObject getData( BufferedReader br) throws JSONException, IOException {
        String line = br.readLine();
        String data= "";
        while (  line != null){
            data = data + line;
            line = br.readLine();
        }
        return new JSONObject(data);
    }
}
