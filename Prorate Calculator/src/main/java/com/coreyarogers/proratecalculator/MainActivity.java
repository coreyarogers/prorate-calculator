/*
 *Application Name: Prorate Calculator
 *Written by: Corey A. Rogers
 *Last updated: 03/16/2014
 */
package com.coreyarogers.proratecalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity {

    private double TWENTY_EIGHT_DAYS = 28;
    private double THIRTY_DAYS = 30;
    private double THIRTY_ONE_DAYS = 31;
    boolean isLeapYear;
    String leapYearStatus;
    private double daysRented;
    private double monthlyRate, dayRate, proratedRate;
    private int movingYear;
    private ShareActionProvider mShareActionProvider;
    private String tvShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Look up the AdView as a resource and loads requested ad.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Establishes text view, edit text, spinner and calculation button.
        final EditText rent = (EditText) findViewById(R.id.monthlyRateEditText);
        final RadioButton movingIn = (RadioButton) findViewById(R.id.movingInRadio);
        final RadioButton movingOut = (RadioButton) findViewById(R.id.movingOutRadio);
        final Spinner months = (Spinner) findViewById(R.id.monthsSpinner);
        final Spinner days = (Spinner) findViewById(R.id.daysSpinner);
        final EditText year =(EditText) findViewById(R.id.yearEditText);
        final Button calculate = (Button)findViewById(R.id.calculateBtn);
        final TextView tv = (TextView)findViewById(R.id.display);

        //Handles tvShare null.
        tvShare = "Your results will display here.";

        //Establishes onClick listener for the calculation button.
        calculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Get user inputted values.
                String monthlyRateStr = rent.getText().toString();
                Boolean movingInStatus = movingIn.isChecked();
                Boolean movingOutStatus = movingOut.isChecked();
                String monthSelected = months.getSelectedItem().toString();
                double daySelected = days.getSelectedItemPosition() + 1;
                String movingYearStr = year.getText().toString();

                //Handles null input for monthlyRate & year.
                if (((monthlyRateStr == null || monthlyRateStr.isEmpty()))
                        && ((movingYearStr == null || movingYearStr.isEmpty())))
                {
                    monthlyRate = 0.0;
                    movingYear = 2014;
                    Toast.makeText(getApplicationContext(),
                            "Lease amount & Year have not been entered.", Toast.LENGTH_LONG).show();
                }

                else
                {
                    if (monthlyRateStr == null || monthlyRateStr.isEmpty())
                    {
                        monthlyRate = 0.0;
                        Toast.makeText(getApplicationContext(),
                            "Lease amount has not been entered", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        monthlyRate = Double.parseDouble(rent.getText().toString());
                    }

                    if (movingYearStr == null || movingYearStr.isEmpty())
                    {
                        movingYear = 2014;
                        Toast.makeText(getApplicationContext(),
                            "Year has not been entered, " +
                            "2014 will be used instead.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        movingYear = Integer.parseInt(year.getText().toString());
                    }
                }
                //Moving in algorithm.
                if (movingInStatus == true)
                {
                    //Checks the year then divides the move in date by 28 days.
                    if (monthSelected.equals("February"))
                    {
                        if ((movingYear%400) == 0)
                        {
                            isLeapYear = true;
                            daysRented = ((TWENTY_EIGHT_DAYS + 1) - daySelected) + 1;
                            dayRate = (monthlyRate / (TWENTY_EIGHT_DAYS + 1));
                            proratedRate = dayRate * daysRented;
                            leapYearStatus = "Yes";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 29)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving in "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daysRented
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daysRented + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();

                            }

                            //Displays invalid input notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 29th" +
                                        " during a leap year.");

                                tvShare = tv.getText().toString();
                            }
                        }

                        else if ((movingYear%100) == 0)
                        {
                            isLeapYear = false;
                            daysRented = (TWENTY_EIGHT_DAYS - daySelected) + 1;
                            dayRate = (monthlyRate / TWENTY_EIGHT_DAYS);
                            proratedRate = dayRate * daysRented;
                            leapYearStatus = "No";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 28)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving in "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daysRented
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daysRented + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid text notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 28th.");
                                tvShare = tv.getText().toString();
                            }
                        }

                        else if ((movingYear%4) == 0)
                        {
                            isLeapYear = true;
                            daysRented = ((TWENTY_EIGHT_DAYS + 1) - daySelected) + 1;
                            dayRate = (monthlyRate / (TWENTY_EIGHT_DAYS + 1));
                            proratedRate = dayRate * daysRented;
                            leapYearStatus = "Yes";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 29)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving in "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daysRented
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daysRented + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid text notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 29th" +
                                        " during a leap year.");
                                tvShare = tv.getText().toString();
                            }
                        }

                        else
                        {
                            isLeapYear = false;
                            daysRented = (TWENTY_EIGHT_DAYS - daySelected) + 1;
                            dayRate = (monthlyRate / TWENTY_EIGHT_DAYS);
                            proratedRate = dayRate * daysRented;
                            leapYearStatus = "No";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 28)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving in "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daysRented
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daysRented + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid text notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 28th.");
                                tvShare = tv.getText().toString();
                            }
                        }
                    }

                    //Divides the move in date by 30 days.
                    else if (monthSelected.equals("April") ||
                             monthSelected.equals("June") ||
                             monthSelected.equals("September") ||
                             monthSelected.equals("November"))
                    {
                        daysRented = (THIRTY_DAYS - daySelected) + 1;
                        dayRate = (monthlyRate / THIRTY_DAYS);
                        proratedRate = dayRate * daysRented;

                        if (daySelected <= 30)
                        {
                            //Displays the prorated rate in the 'display' Text View.
                            String rentalAmount = String.format("%.2f", proratedRate);
                            String dayRateAmount = String.format("%.4f", dayRate);
                            String monthlyRateAmount = String.format("%.2f", monthlyRate);
                            String daySelectedInt = String.format("%.0f", daySelected);
                            tv.setText("The prorated rental amount for moving in "
                                    + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                    + "$" + rentalAmount + ".\n"
                                    + "\nCost Breakdown"
                                    + "\nMonthly rent: " + monthlyRateAmount
                                    + "\nDay rate: " + dayRateAmount
                                    + "\nDays rented: " + daysRented + "\n"
                                    + "\n" + dayRateAmount + " x " + daysRented + " = " + rentalAmount
                                    + "\n\n*Prorated result has been rounded."
                                    + "\n*Calculated using Prorate Calculator.");
                            tvShare = tv.getText().toString();
                        }

                        //Displays invalid text notification.
                        else
                        {
                            tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 30th.");
                            tvShare = tv.getText().toString();
                        }
                    }

                    //Divides the move in date by 31 days.
                    else
                    {
                        daysRented = (THIRTY_ONE_DAYS - daySelected) + 1;
                        dayRate = (monthlyRate / THIRTY_ONE_DAYS);
                        proratedRate = dayRate * daysRented;

                        //Displays the prorated rate in the 'display' Text View.
                        String rentalAmount = String.format("%.2f", proratedRate);
                        String dayRateAmount = String.format("%.4f", dayRate);
                        String monthlyRateAmount = String.format("%.2f", monthlyRate);
                        String daySelectedInt = String.format("%.0f", daySelected);
                        tv.setText("The prorated rental amount for moving in "
                                + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                + "$" + rentalAmount + ".\n"
                                + "\nCost Breakdown"
                                + "\nMonthly rent: " + monthlyRateAmount
                                + "\nDay rate: " + dayRateAmount
                                + "\nDays rented: " + daysRented + "\n"
                                + "\n" + dayRateAmount + " x " + daysRented + " = " + rentalAmount
                                + "\n\n*Prorated result has been rounded."
                                + "\n*Calculated using Prorate Calculator.");
                        tvShare = tv.getText().toString();
                    }
                }

                //Moving out algorithm.
                else if (movingOutStatus == true)
                {
                    //Checks the year then divides the move out date by 28/29 days.
                    if (monthSelected.equals("February"))
                    {
                        if ((movingYear%400) == 0)
                        {
                            isLeapYear = true;
                            dayRate = (monthlyRate / (TWENTY_EIGHT_DAYS + 1));
                            proratedRate = dayRate * daySelected;
                            leapYearStatus = "Yes";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 29)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving out "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daySelected
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daySelected + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid input notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 29th" +
                                        " during a leap year.");
                                tvShare = tv.getText().toString();
                            }
                        }

                        else if ((movingYear%100) == 0)
                        {
                            isLeapYear = false;
                            dayRate = (monthlyRate / TWENTY_EIGHT_DAYS);
                            proratedRate = dayRate * daySelected;
                            leapYearStatus = "No";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 28)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving out "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daySelected
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daySelected + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid input notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 28th.");
                                tvShare = tv.getText().toString();
                            }
                        }

                        else if ((movingYear%4) == 0)
                        {
                            isLeapYear = true;
                            dayRate = (monthlyRate / (TWENTY_EIGHT_DAYS + 1));
                            proratedRate = dayRate * daySelected;
                            leapYearStatus = "Yes";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 29)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving out "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daySelected
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daySelected + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid input notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 29th" +
                                        " during a leap year.");
                                tvShare = tv.getText().toString();
                            }
                        }

                        else
                        {
                            isLeapYear = false;
                            dayRate = (monthlyRate / TWENTY_EIGHT_DAYS);
                            proratedRate = dayRate * daySelected;
                            leapYearStatus = "No";

                            //Displays the prorated rate in the 'display' Text View.
                            if (daySelected <= 28)
                            {
                                String rentalAmount = String.format("%.2f", proratedRate);
                                String dayRateAmount = String.format("%.4f", dayRate);
                                String monthlyRateAmount = String.format("%.2f", monthlyRate);
                                String daySelectedInt = String.format("%.0f", daySelected);
                                tv.setText("The prorated rental amount for moving out "
                                        + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                        + "$" + rentalAmount + ".\n"
                                        + "\nCost Breakdown"
                                        + "\nMonthly rent: " + monthlyRateAmount
                                        + "\nDay rate: " + dayRateAmount
                                        + "\nDays rented: " + daySelected
                                        + "\nDuring a leap year: " + leapYearStatus + "\n"
                                        + "\n" + dayRateAmount + " x " + daySelected + " = " + rentalAmount
                                        + "\n\n*Prorated result has been rounded."
                                        + "\n*Calculated using Prorate Calculator.");
                                tvShare = tv.getText().toString();
                            }

                            //Displays invalid input notification.
                            else
                            {
                                tv.setText("Please enter a valid date for the month of " +
                                        monthSelected + ".\n"
                                        + "Dates range from " + monthSelected + " 1st - 28th.");
                                tvShare = tv.getText().toString();
                            }
                        }
                    }

                    //Divides the move out date by 30 days.
                    else if (monthSelected.equals("April") ||
                            monthSelected.equals("June") ||
                            monthSelected.equals("September") ||
                            monthSelected.equals("November"))
                    {
                        dayRate = (monthlyRate / THIRTY_DAYS);
                        proratedRate = dayRate * daySelected;

                        if (daySelected <= 30)
                        {
                            //Displays the prorated rate in the 'display' Text View.
                            String rentalAmount = String.format("%.2f", proratedRate);
                            String dayRateAmount = String.format("%.4f", dayRate);
                            String monthlyRateAmount = String.format("%.2f", monthlyRate);
                            String daySelectedInt = String.format("%.0f", daySelected);
                            tv.setText("The prorated rental amount for moving out "
                                    + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                    + "$" + rentalAmount + ".\n"
                                    + "\nCost Breakdown"
                                    + "\nMonthly rent: " + monthlyRateAmount
                                    + "\nDay rate: " + dayRateAmount
                                    + "\nDays rented: " + daySelected + "\n"
                                    + "\n" + dayRateAmount + " x " + daySelected + " = " + rentalAmount
                                    + "\n\n*Prorated result has been rounded."
                                    + "\n*Calculated using Prorate Calculator.");
                            tvShare = tv.getText().toString();
                        }

                        //Displays invalid text notification.
                        else
                        {
                            tv.setText("Please enter a valid date for the month of " +
                                    monthSelected + ".\n"
                                    + "Dates range from " + monthSelected + " 1st - 30th.");
                            tvShare = tv.getText().toString();
                        }
                    }

                    //Divides the move in date by 31 days.
                    else
                    {
                        dayRate = (monthlyRate / THIRTY_ONE_DAYS);
                        proratedRate = dayRate * daySelected;

                        //Displays the prorated rate out the 'display' Text View.
                        String rentalAmount = String.format("%.2f", proratedRate);
                        String dayRateAmount = String.format("%.4f", dayRate);
                        String monthlyRateAmount = String.format("%.2f", monthlyRate);
                        String daySelectedInt = String.format("%.0f", daySelected);
                        tv.setText("The prorated rental amount for moving out "
                                + monthSelected + " " + daySelectedInt + ", " + movingYear +" is "
                                + "$" + rentalAmount + ".\n"
                                + "\nCost Breakdown"
                                + "\nMonthly rent: " + monthlyRateAmount
                                + "\nDay rate: " + dayRateAmount
                                + "\nDays rented: " + daySelected + "\n"
                                + "\n" + dayRateAmount + " x " + daySelected + " = " + rentalAmount
                                + "\n\n*Prorated result has been rounded."
                                + "\n*Calculated using Prorate Calculator.");
                        tvShare = tv.getText().toString();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        // Return true to display menu
        return true;
    }

// Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String versionString = getResources().getString(R.string.version_name);

        int id = item.getItemId();

        if (id == R.id.menu_item_about)
        {
            Toast.makeText(getApplicationContext(),
                    "Prorate Calculator Version " + versionString, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (id == R.id.menu_item_share)
        {
            // Create the share Intent
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, tvShare);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
