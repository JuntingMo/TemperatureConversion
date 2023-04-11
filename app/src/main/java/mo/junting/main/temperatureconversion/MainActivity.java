package mo.junting.main.temperatureconversion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText vNumber;
    Button submitButton;

    String[] modes;

    int mode_choose;

    double cel;
    double fah;
    double kel;
    double num;


    TextView mResultView1;
    TextView mResultView2;
    TextView mResultView3;

    Spinner mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vNumber = (EditText) findViewById(R.id.firsinput);
        submitButton = (Button) findViewById(R.id.button1);
        mResultView1 = (TextView) findViewById(R.id.firstoutput);
        mResultView2 = (TextView) findViewById(R.id.secondoutput);
        mResultView3 = (TextView) findViewById(R.id.thirdtoutput);

        //Create a spinner
        modes = getResources().getStringArray(R.array.mode_array);
        mSpinner = (Spinner) findViewById(R.id.spin_choose);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modes);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {
                        int index = arg0.getSelectedItemPosition();
                        Toast.makeText(getBaseContext(),
                                "You have selected item : " + modes[index],
                                Toast.LENGTH_LONG).show();
                        mode_choose = arg0.getSelectedItemPosition();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        //Calculate the result
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // The situation when input is null
                String number = vNumber.getText().toString();


                if(vNumber.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, R.string.bmi_warning, Toast.LENGTH_LONG).show();
                }
                else {


                    if (mode_choose == 0) {
                        num = Double.parseDouble(number); // Convert String to double
                        cel = num;
                        fah = (9.0 / 5.0) * num + 32.0;
                        kel = cel + 273.15;

                        mResultView1.setText(String.valueOf(num));
                        mResultView2.setText(String.valueOf(fah));
                        mResultView3.setText(String.valueOf(kel));


                    } else if (mode_choose == 1) {
                        num = Double.parseDouble(number); // Convert String to double
                        fah = num;
                        cel = (num - 32.0) * (5.0 / 9.0);
                        kel = 273.15 + cel;
                        mResultView2.setText(String.valueOf(num));
                        mResultView1.setText(String.valueOf(cel));
                        mResultView3.setText(String.valueOf(kel));

                    } else if (mode_choose == 2) {
                        num = Double.parseDouble(number); // Convert String to double
                        kel = num;
                        cel = (num - 32.0) * (5.0 / 9.0);
                        fah = (9.0 / 5.0) * num + 32.0;
                        mResultView3.setText(String.valueOf(num));
                        mResultView1.setText(String.valueOf(cel));
                        mResultView2.setText(String.valueOf(fah));
                    }
                }

                //Save data
                savePreferences(number,mode_choose);


            }
        });

    }

    // Create an option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the aaction bar if it is present
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_about:
                Toast.makeText(this, "About Button Clicked !",
                        Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.menu_about);
                builder.setMessage(R.string.about_msg);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            { }
                        });
                builder.create();
                builder.show();
                return true;

            case R.id.menu_exit:
                finish();
                return true;
        }
        return false;
    }

    //Save data
    public void savePreferences(String n,int cho) {
        SharedPreferences pref = getSharedPreferences("TEM", MODE_PRIVATE);

        pref.edit().putString("num", n).commit();
        pref.edit().putInt("choose", cho).commit();

    }
    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("TEM", MODE_PRIVATE);
        vNumber.setText(pref.getString("num","0"));
        mSpinner.setSelection(pref.getInt("choose", 0));

    }

    @Override
    protected void onStart() {
        super.onStart();
        //load data
        loadPreferences();
    }


}
