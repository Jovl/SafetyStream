package me.kevingleason.androidrtc;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import me.kevingleason.androidrtc.util.Constants;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Login Activity for the first time the app is opened, or when a user clicks the sign out button.
 * Saves the username in SharedPreferences.
 */
public class LoginActivity extends Activity {

    private Button Submit;

    private EditText FirstName;
    private EditText LastName;
    private EditText Address;
    private EditText City;
    private EditText ZipCode;
    private AutoCompleteTextView State;
    private EditText PhoneNumber;
    private EditText Race;
    private Spinner Gender;
    private EditText Age;
    private EditText mUsername;
    private EditText Notes;
    // Declaring connection variables
    Connection con;
    String un,pass,db,ip;
    //End Declaring connection variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initializes a variable to store the username, allows the user to not have to sign in a second time
        mUsername = (EditText) findViewById(R.id.FirstName);

        //initializes each textfield to a variable to manipulate the text field from java code
        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        Address = (EditText) findViewById(R.id.Address);
        City = (EditText) findViewById(R.id.City);
        ZipCode = (EditText) findViewById(R.id.ZipCode);
        Age = (EditText) findViewById(R.id.Age);
        //Sets up the State text field to be auto-filled
        State = (AutoCompleteTextView) findViewById(R.id.State);
        String[] states = getResources().getStringArray(R.array.State_Array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);
        State.setAdapter(adapter);
        PhoneNumber = (EditText) findViewById(R.id.PhoneNumber);
        Race = (EditText) findViewById(R.id.Race);
        //sets up the gender textfield to be a drop down list
        Gender = (Spinner) findViewById(R.id.GenderList);
        Spinner dropdown = (Spinner)findViewById(R.id.GenderList);
        String[] genders = new String[]{"Gender", "Male", "Female"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        dropdown.setAdapter(adapter2);

        Notes = (EditText) findViewById(R.id.OtherNotes);
        Submit = (Button) findViewById(R.id.SubmitButton);

        // Declaring Server ip, username, database name and password
        ip = "safetystream.database.windows.net";
        db = "SafetyStream";
        un = "michaelcain";
        pass = "Password1";
        // Declaring Server ip, username, database name and password


    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        private static final String TAG = "MyActivity";

        String z = "";
        Boolean isSuccess = false;

        String fName = FirstName.getText().toString();
        String lName = LastName.getText().toString();
        String address = Address.getText().toString();
        String city = City.getText().toString();
        String zipcode = ZipCode.getText().toString();
        String age = Age.getText().toString();
        String state = State.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String race = Race.getText().toString();
        String gender = Gender.getSelectedItem().toString();
        String notes = Notes.getText().toString();

        @Override
        protected String doInBackground(String... params)
        {
            //TODO Add conditional statements to validate data entry boudaries
                try
                {
                    Log.v(TAG, "before");

                    con = connectionclass(un, pass, db, ip);        // Connect to database
                    Statement stmt = con.createStatement();

                    Log.v(TAG, "after");

                    if (con == null)
                    {
                        z = "Check Your Internet Access!";

                    }
                    else
                    {
                        Log.v(TAG, "beforeUpdate");
                        String query = "INSERT INTO Users VALUES ('" + fName + "', '" + lName + "', '" + gender + "', '" + age + "', '" + address + "', '" + city + "', '" + state + "', '" + zipcode + "', '" + phone + "', '" + race + "', '" + notes + "')";
                        stmt.executeUpdate(query);
                        Log.v(TAG, "AfterUpdate");
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            Log.v(TAG, z);
            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";databaseName=" + database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }
    public void testButton (View view){
        CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
        checkLogin.execute("");

        //when the app is opened for the first time the user enters all their information. After the information has been entered,
        //it is stored and their first name is used as the username and stored so that in the future the user does not need to sign
        //in
        String username = mUsername.getText().toString();

        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.USER_NAME, username);
        edit.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
