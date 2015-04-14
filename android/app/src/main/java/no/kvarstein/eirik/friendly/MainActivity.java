package no.kvarstein.eirik.friendly;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final utils Utils = new utils();
        final EditText usrText = (EditText)findViewById(R.id.usrNameText);
        final Button newUserBtn = (Button)findViewById(R.id.newusrbtn);
        final Context mContext = getApplicationContext();

        //Hook up our new User button to a method.
        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrname = usrText.getText().toString();

                Toast.makeText(getApplicationContext(), "Username: "+usrname,
                        Toast.LENGTH_SHORT).show();
               // Log.e("ReadWrite", "Username input: " + usrname);
            Utils.writeUserName(usrname, mContext);
                TextView usr = (TextView)findViewById(R.id.username);
                usr.setText(Utils.readUserName(mContext));
                usrText.setVisibility(View.INVISIBLE);
                newUserBtn.setVisibility(View.INVISIBLE);
            }
        });



        //Check if userfile exist
        if(Utils.readUserName(this) == null){
            //Userfile does not exist, prompt for username with popup
            //Make controlls visible

            usrText.setVisibility(View.VISIBLE);
            newUserBtn.setVisibility(View.VISIBLE);

        }else {
            //Read username from file, display info
            TextView usr = (TextView)findViewById(R.id.username);
            usr.setText(Utils.readUserName(mContext));
        }


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
