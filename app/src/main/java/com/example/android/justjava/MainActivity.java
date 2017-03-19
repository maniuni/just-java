package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Get the name of the customer
        EditText name = (EditText) findViewById(R.id.name_field);
        String clientName = name.getText().toString();

        //Check if customer wants whipped cream
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream);
        boolean hasWhippedCream = whippedCream.isChecked();

        //Check if customer wants chocolate
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate);
        boolean hasChocolate = chocolate.isChecked();

        int price = calculatePrice(hasChocolate, hasWhippedCream);
        String orderSummary = createOrderSummary(clientName, price, hasWhippedCream, hasChocolate);
        String subject = getString(R.string.order_summary_email_subject, clientName);

        //Send an email message with the order
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(intent.EXTRA_SUBJECT, subject);
        intent.putExtra(intent.EXTRA_TEXT, orderSummary);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasChocolate is whether the client wants chocolate
     * @param hasWhippedCream is whether the client wants whipped cream
     */
    private int calculatePrice(boolean hasChocolate, boolean hasWhippedCream){
        //Sets the initial price of coffee
        int basePrice = 5;

        //Checks if the client has ordered chocolate
        if (hasChocolate){
            basePrice += 2;
        }
        //Checks if the client has ordered whipped cream
        if(hasWhippedCream){
            basePrice += 1;
        }
        //Returns the final price according to the number of coffees
        return basePrice * quantity;
    }

    /**
     * Creates a summary with the order details.
     *
     * @param clientName is the name of the customer
     * @param price of the order
     * @param hasWhippedCream does user wants whipped cream topping
     * @param hasChocolate does user wants chocolate topping
     * @return text summary
     */
    private String createOrderSummary(String clientName, int price, boolean hasWhippedCream, boolean hasChocolate){

        return getString(R.string.order_summary_name, clientName) + "\n" +
                getString(R.string.order_summary_whipped_cream, hasWhippedCream)  + "\n" +
                getString(R.string.order_summary_chocolate, hasChocolate) + "\n" +
                getString(R.string.order_summary_quantity, quantity) +"\n" +
                getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price)) +
                "\n" + getString(R.string.thank_you);
    }

    /**
     * This method is called when the quantity is increased.
     */
    public void increase(View view) {
        if(quantity == 100){
            //Show an error message as a toast
            Toast.makeText(this, getString(R.string.high_amount_error), Toast.LENGTH_SHORT).show();
            //Return as there is nothing left to do
            return;
        }
        quantity++;
        display(quantity);
    }

    /**
     * This method is called when the quantity is decreased.
     */
    public void decrease(View view) {
        if(quantity == 1) {
            //Show an error message as a toast
            Toast.makeText(this, getString(R.string.low_amount_error), Toast.LENGTH_SHORT).show();
            //Return as there is nothing left to do
            return;
        }
        quantity--;

        display(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}