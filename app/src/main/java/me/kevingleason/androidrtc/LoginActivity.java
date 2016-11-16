package me.kevingleason.androidrtc;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.StrictMode;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import me.kevingleason.androidrtc.util.Constants;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Login Activity for the first time the app is opened, or when a user clicks the sign out button.
 * Saves the username in SharedPreferences.
 */
public class LoginActivity extends Activity {

    //Declarations for variables that will hold the information entered in
    //all of the text fields from the login screen and the submit button
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

    // Declaring connection variables for connecting to SQL database
    Connection con;
    Statement stmt;
    String un,pass,db,ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast toast = Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG);
        toast.show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializes a variable to store the first name as the username
        mUsername = (EditText) findViewById(R.id.FirstName);

        //initializes the variables created for each text field to that text field
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
        // These are connection strings for the SQL database
        ip = "safetystream.database.windows.net";
        db = "SafetyStream";
        un = "michaelcain";
        pass = "Password1";



    }


    public class signIn extends AsyncTask<String,String,String>
    {

        //string used to hold any error messages for information that may be entered wrong or if the
        //connection to the database cannot be made
        String z = "";

        //initializes variables that will store the information that is inputted into the text fields
        String fName = FirstName.getText().toString();
        String lName = LastName.getText().toString();
        String address = Address.getText().toString();
        String city = City.getText().toString();
        String zipCode = ZipCode.getText().toString();
        String age = Age.getText().toString();
        String state = State.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String race = Race.getText().toString();
        String gender = Gender.getSelectedItem().toString();
        String notes = Notes.getText().toString();

        //method that will connect to the database and then write the account information to the database in the
        //proper format
        @Override
        protected String doInBackground(String... params)
        {
            Looper.prepare();
                try
                {
                    ///Connects to database
                    con = connectionClass(un, pass, db, ip);

                    //sets up the object that will hold the information uploaded to the database.
                    stmt = con.createStatement();
                    if (con == null)
                    {
                        //if con returns as null then the connection could not be made
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        //if con returns the connection string, then the connection was made
                        //query holds the information that will be uploaded to the database in proper format
                        String query = "INSERT INTO Users VALUES ('" + fName + "', '" + lName + "', '" + gender + "', '" + age + "', '" + address + "', '" + city + "', '" + state + "', '" + zipCode + "', '" + phone + "', '" + race + "', '" + notes + "')";

                        //sends the query string to the database
                        stmt.executeUpdate(query);
                    }
                }
                catch (Exception ex)
                {
                    z = ex.getMessage();
                }
                LoginSuccess();



            if(z != "") {
                //if z is equal to a different string an error was found and the button is set to be clickable again
                //since no information was sent to the database and the user must go back and fix their inputs
                setSubmitClickable(true);
                //displays the message to the user
                Toast toast = Toast.makeText(LoginActivity.this, z, Toast.LENGTH_LONG);
                toast.show();

            }
            return z;
        }
    }

    @SuppressLint("NewApi")
    //Class created to connect to any database given the connection strings for that database
    public Connection connectionClass(String user, String password, String database, String server)
    {
        //defines necessary variables for creating the connection string
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            //sets up a connection string to used to connect to the database
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";databaseName=" + database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        //error catching statements
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
        //returns the connection string
        return connection;
    }

    //this method is linked to the submit button on the login screen, when that button is clicked this method is called
    public void testButton (View view){
        Boolean[] success = new Boolean[10];
        Boolean inputCorrect = true;
        int i = 0;


        //sets the submit button so it cannot be clicked multiple times to prevent the user from sending multiple
        //entries to the database
        setSubmitClickable(false);

        String fName = FirstName.getText().toString();
        String lName = LastName.getText().toString();
        String address = Address.getText().toString();
        String city = City.getText().toString();
        String zipCode = ZipCode.getText().toString();
        String age = Age.getText().toString();
        String state = State.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String race = Race.getText().toString();
        String gender = Gender.getSelectedItem().toString();
        String notes = Notes.getText().toString();

        //goes through and checks all input fields against their specific requirements
        success[0] = checkCharOnlyInputs(fName, FirstName);
        success[1] = checkCharOnlyInputs(lName, LastName);
        success[2] = checkCharOnlyInputs(city, City);
        success[3] = checkCharOnlyInputs(race, Race);
        success[4] = checkZipCode(zipCode, ZipCode);
        success[5] = checkPhone(phone, PhoneNumber);
        success[6] = checkOtherInputs(address, Address);
        success[7] = checkOtherInputs(age, Age);
        success[8] = checkState(state, State);
        success[9] = checkGender(gender, Gender);

        //goes through the array to see if any of the check methods returned false, indicating an invalid input
        for(i = 0; i < success.length; i++){
            if(success[i] == false){
                //if any array element is equal to false then the inputCorrect is set to false to prevent the information
                //from being sent to the databse
                inputCorrect = false;
                i = success.length;
            }
        }

        if(inputCorrect == true) {

            //begins the singIn method to send the information to the database
            signIn signIn = new signIn();// this is the Asynctask, which is used to process in background to reduce load on app process
            signIn.execute("");
        }else {
            setSubmitClickable(true);
        }
    }

    public void LoginSuccess(){
        //when the app is opened for the first time the user enters all their information. After the information has been entered,
        //it is stored and their first name is used as the username and stored so that in the future the user does not need to sign
        //in again
        String username = mUsername.getText().toString();

        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.USER_NAME, username);
        edit.apply();

        //the main activity is started after the account creation is complete
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //this method is used to check if the String only methods are filled in and make sure that they only contain characters
    public Boolean checkCharOnlyInputs (String input, EditText textField){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String message = null;
        boolean digit = false;
        boolean splicerIndicator = false;
        String textFieldName = "";

        if(input == null || input.isEmpty()){
            //if the string is empty then a message is recorded alerting the user that
            //they must fill in all text fields, the color of the corresponding text field is turned to red
            message = "Please fill in all text fields";
            textField.setBackgroundColor(Color.RED);
        }
        else{
            //looks through the input to see if any of the characters are digits
            for(char c : input.toCharArray()){
                if(Character.isDigit(c) ){
                    //if a digit is found, a variable is flagged
                   digit = true;
                }
            }

            //if a number is entered into the string then a message is recorded alerting the user
            //that that field cannot contain strings, the text field is also turned to red

            if(digit == true){
                //converts the stored reference location of the textField to a string
                String field = textField.toString();
                //looks throught the reference location of the string
                for(char c2: field.toCharArray()){
                    //if a } is found then that is the end of the reference location and that character will not be recorded
                    if (Character.toString(c2).compareTo("}") == 0) {
                        splicerIndicator = false;
                    }
                    //if the splicerIndicator is true then the reference location string is in the portion specifying the name of the textfield so those characters
                    //are added to a variable storing the name of the text field
                    if(splicerIndicator == true){
                        textFieldName += Character.toString(c2);
                    }
                    //if the character is a / then this is the portion of the reference location just before the name of the text field, so the next character will
                    //be recorded
                    if (Character.toString(c2).compareTo("/") == 0) {
                        splicerIndicator = true;
                    }
                }
                //a message is recorded and the textfield is changed to be a red color
                message = "Your " + textFieldName + " cannot contain numbers";
                textField.setBackgroundColor(Color.RED);
            }
        }

        if(message == null){
            //if the message is null then to error was found
            textField.setBackgroundColor(Color.TRANSPARENT);
            return true;
        }else {
            //displays the message to the user
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    //this method is used to check the length of the ZipCode
    public Boolean checkZipCode(String input, EditText textField){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String message = null;
        if(input == null || input.isEmpty()){
            //if the string is empty then a message is recorded alerting the user that
            //they must fill in all text fields, the color of the corresponding text field is turned to red
            message = "Please fill in all text fields";
            textField.setBackgroundColor(Color.RED);
            return false;
        }else
        {
            if(5 == input.length()){
                //if the length is 5 digits, then it is a valid zip code
                textField.setBackgroundColor(Color.TRANSPARENT);
                return true;
            }else {
                //if the length is not 5 digits, then it is an invalid zip code. A message is recorded and displayed to the
                //user, the corresponding text field is also changed to be red
                message = "The Zip Code is not Valid. \n It must be 5 digits long";
                textField.setBackgroundColor(Color.RED);
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        }
    }

    //this method is used to check the length of the phone number
    public Boolean checkPhone(String input, EditText textField){
        String message = null;
        if(input == null || input.isEmpty()){
            //if the string is empty then a message is recorded alerting the user that
            //they must fill in all text fields, the color of the corresponding text field is turned to red
            message = "Please fill in all text fields";
            textField.setBackgroundColor(Color.RED);
            return false;
        }
        else{
            if(10 == input.length()){
                //if the length is 10 digits then it is a valid phone number
                textField.setBackgroundColor(Color.TRANSPARENT);
                return true;
            } else {
                //if the length is not 10 digits, then it is an invalid phone number. A message is recorded and displayed to the
                //user, the corresponding text field is also changed to be red
                message = "The Phone Number is not Valid. \n It must be 10 digits long";
                textField.setBackgroundColor(Color.RED);
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        }
    }

    //this method is used to ensure that other text fields are filled in and not left blank
    public Boolean checkOtherInputs(String input, EditText textField){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String message = null;

        if(input == null || input.isEmpty())
        {
            //if the string is empty then a message is recorded alerting the user that
            //they must fill in all text fields, the color of the corresponding text field is turned to red
            message = "Please fill in all text fields";
            textField.setBackgroundColor(Color.RED);
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
            return false;
        } else {
            textField.setBackgroundColor(Color.TRANSPARENT);
            return true;
        }
    }

    //this method is used to ensure that the gender field is not left blank
    public Boolean checkGender(String input, Spinner textField){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String message = null;
        if(input == "Gender" || input.isEmpty() || input == null)
        {
            //if the string is empty then a message is recorded alerting the user that
            //they must fill in all text fields, the color of the corresponding text field is turned to red
            message = "Please fill in all text fields";
            textField.setBackgroundColor(Color.RED);
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
            return false;
        } else {
            textField.setBackgroundColor(Color.TRANSPARENT);
            return true;
        }
    }

    //used to ensure that the state field is not left blank
    public Boolean checkState(String input, AutoCompleteTextView textField){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String message = null;

        if(input == null || input.isEmpty())
        {
            //if the string is empty then a message is recorded alerting the user that
            //they must fill in all text fields, the color of the corresponding text field is turned to red
            message = "Please fill in all text fields";
            textField.setBackgroundColor(Color.RED);
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
            return false;
        } else {
            textField.setBackgroundColor(Color.TRANSPARENT);
            return true;
        }
    }

    //method used to change the click-ability of the submit button
    public void setSubmitClickable(Boolean set){
        Submit.setClickable(set);
    }
}
