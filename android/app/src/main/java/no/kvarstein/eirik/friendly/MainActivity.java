package no.kvarstein.eirik.friendly;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    utils Utils = null;
    final long interval_delay = 60; //Delay between requests. In seconds, read from config.
    final boolean app_enabled = true;
    TextView debug;
    private String username;
    //runs without a timer by reposting this handler at the end of the runnable


    private void sendLocation(Location pos){
        //Create the URL
        String url = "http://eirik.pw:3000/loc/" + username + "/" + pos.getLatitude() + "/" + pos.getLongitude();

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(url);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return Utils.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //etResponse.setText(result);
            debug.setText(result);
        }
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            //Get GPS location
            //Test location
             Location test = Utils.getLastPos();
            // String nfo = test.getLatitude() + ", "+test.getLongitude() + " alt: "+test.getAltitude();
             String nfo = String.format("%.5f, %.5f, %.5f", test.getLatitude(), test.getLongitude(), test.getAltitude());
             toast(nfo);

            sendLocation(test);

           // timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            if(app_enabled){
            timerHandler.postDelayed(this, interval_delay * 1000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText usrText = (EditText) findViewById(R.id.usrNameText);
        final Button newUserBtn = (Button) findViewById(R.id.newusrbtn);
        final Context mContext = getApplicationContext();
        debug= (TextView)findViewById(R.id.debug);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Utils = new utils(locationManager);


        //Hook up our new User button to a method.
        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrname = usrText.getText().toString();

                Toast.makeText(getApplicationContext(), "Username: " + usrname,
                        Toast.LENGTH_SHORT).show();
                // Log.e("ReadWrite", "Username input: " + usrname);
                Utils.writeUserName(usrname, mContext);
                TextView usr = (TextView) findViewById(R.id.username);
                usr.setText(Utils.readUserName(mContext));
                usrText.setVisibility(View.INVISIBLE);
                newUserBtn.setVisibility(View.INVISIBLE);
            }
        });


        //Check if userfile exist
        if (Utils.readUserName(this) == null) {
            //Userfile does not exist, prompt for username with popup
            //Make controlls visible

            usrText.setVisibility(View.VISIBLE);
            newUserBtn.setVisibility(View.VISIBLE);

        } else {
            //Read username from file, display info
            TextView usr = (TextView) findViewById(R.id.username);
            username = Utils.readUserName(mContext);
            usr.setText(username);
            debug.setVisibility(View.VISIBLE);
        }


        //Start main thread, sends GPS location every X minutes
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
