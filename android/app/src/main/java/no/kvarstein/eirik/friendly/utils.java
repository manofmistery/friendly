package no.kvarstein.eirik.friendly;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

/**
 * Created by eirik on 14.04.2015.
 */

public class utils{
   //Contains utility functions, reading/writing files etc.
   private LocationManager locationManager;
    public utils(LocationManager manager){
        this.locationManager = manager; //pass GPS Location manager from MainActivity

    }


    public void sendLocation(Location pos, String username){


    }

    public Location getLastPos(){
        String locationProvider = LocationManager.GPS_PROVIDER; //NETWORK_PROVIDER
        // Or use LocationManager.GPS_PROVIDER

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if(lastKnownLocation == null){
            //Testing..
            lastKnownLocation = new Location(locationProvider);
            lastKnownLocation.setLatitude(0);
            lastKnownLocation.setAltitude(100);
            lastKnownLocation.setLongitude(0);
        }
        return lastKnownLocation;
    }


    public void writeUserName(String usr, Context mContext){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(usr);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        catch(Exception e){
            Log.e("Exception", "Something fucked up: "+e.toString());
        }
    }

    public String readUserName(Context mContext){
        String ret = "";

        try {
            InputStream inputStream = mContext.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return null;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return null;
        } catch(Exception e){
            Log.e("Fuckup", "in readUserName: "+e.toString());
            return null;
        }

        return ret;
    }


    /* Send HTTP Get request and catch response */

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
