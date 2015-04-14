package no.kvarstein.eirik.friendly;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.*;

/**
 * Created by eirik on 14.04.2015.
 */

public class utils{
   //Contains utility functions, reading/writing files etc.

    public utils(){

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

}
