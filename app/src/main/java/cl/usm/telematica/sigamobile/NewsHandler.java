package cl.usm.telematica.sigamobile;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pipos on 05-12-2016.
 */

public class NewsHandler extends AsyncTask<Void, Void, Boolean> {
    URLConnection conn;
    NewsFragment mFragment;
    String server_response;
    List<Event> eventos;

    public NewsHandler(NewsFragment mFragment){
        this.mFragment = mFragment;
        eventos = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            URL url = new URL("http://www.mocky.io/v2/5844eb2111000025160e6ba1");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            urlConnection.connect();
            int response = urlConnection.getResponseCode();

            server_response = readStream(urlConnection.getInputStream());
            urlConnection.disconnect();


        }catch (MalformedURLException e){
            Toast.makeText(mFragment.getContext(), "URL inválida", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){
            Toast.makeText(mFragment.getContext(), "Error! :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (server_response != null){
            try {
                JSONObject cursor = new JSONObject(server_response);
                JSONArray content = cursor.getJSONArray("content");
                for (int i=0;i<content.length();i++){
                    JSONObject current = content.getJSONObject(i);
                    int year = current.getInt("year");
                    String month = current.getString("month");
                    String date = current.getString("day");
                    String tipo = current.getString("type");
                    String description = current.getString("description");
                    Event temp = new Event(year,month,date,tipo,description);
                    eventos.add(temp);

                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
    public void onPostExecute(Boolean success){
        if (success) {
            mFragment.changeDataSetNews(eventos);
        }
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
