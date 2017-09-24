package com.example.gabriel.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private final int COFFEE_PRICE=5;
    private final int WHIPPED_CREAM_PRICE=1;
    private final int CHOCOLATE_PRICE=2;

    private final int MAX_ORDERS=25;
    private final int MIN_ORDERS=0;

    static final int SENT_EMAIL = 1;

    int quantity=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    /**
     * Calculates the price of the order
     * @param hasChocolate is whether or not the user wants chocolate
     * @param hasWhippedCream is whether or not the user wants whipped cream
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        int addCream = 0;
        int addChocolate = 0;

        if (hasWhippedCream){
            addCream = WHIPPED_CREAM_PRICE;
        }

        if (hasChocolate){
            addChocolate = CHOCOLATE_PRICE;
        }

        return  (COFFEE_PRICE + addChocolate + addCream) * quantity;

    }

    /**
     * This method displays the given quantity value on the screen.
     * @param numberOfCoffees  quantity of coffees
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }


    public void increment(View view){
        if (quantity<MAX_ORDERS){
            quantity ++;
            displayQuantity(quantity);
        }

    }

    public void decrement(View view){
        if(quantity>MIN_ORDERS){
            quantity--;
            displayQuantity(quantity);

        }
    }




    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        boolean hasWhippedCream=hasWhippedCream();
        boolean hasChocolate=hasChocolate();
        int price=calculatePrice(hasWhippedCream,hasChocolate);
        String name=getName();

        String priceMessage=createOrderSummary(price, hasWhippedCream, hasChocolate,name);
        sendEmail(priceMessage,name);
    }


    /**
     * This method creates the order summary
     * @param totalPrice of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @return text summary
     */
    public String createOrderSummary(int totalPrice,boolean addWhippedCream, boolean addChocolate, String name){
        String summary=getString(R.string.summary_name,name);
        summary += "\n"+getString(R.string.summary_whipped_cream,addWhippedCream);
        summary += "\n"+getString(R.string.summary_chocolate,addChocolate);
        summary += "\n"+getString(R.string.summary_quantity,quantity);
        summary += "\n"+getString(R.string.summary_total,NumberFormat.getCurrencyInstance().format(totalPrice));
        summary += "\n"+getString(R.string.summary_thank_you);

        return summary;

    }



    /**
     * This method check if whipped cream was requested
     * @return if the whipped cream check box is checked or not
     **/
    private boolean hasWhippedCream () {
        CheckBox hasWhippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_check_box);
        return hasWhippedCreamCheckBox.isChecked();

    }


    /**
     * This method check if whipped cream was requested
     * @return if the customer wants chocolate
     **/
    private boolean hasChocolate () {
        CheckBox hasChocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_check_box);
        return hasChocolateCheckBox.isChecked();

    }

    /**
     *
     * @return
     */
    private String getName(){
        EditText nameEditText=(EditText) findViewById(R.id.name_field);
        return nameEditText.getText().toString();
    }





    private void sendEmail(String message, String name){

        String[] addresses={getString(R.string.email_email)};
        String subject=getString(R.string.email_subject,name);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,SENT_EMAIL);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SENT_EMAIL) {
            quantity=0;
            displayQuantity(quantity);
            blankName();
            setNoChocolate();
            setNoWhippedCream();


        }
    }

    private void blankName(){
        EditText nameEditText=(EditText) findViewById(R.id.name_field);
        nameEditText.setText("");
    }


    private void setNoChocolate () {
        CheckBox hasChocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_check_box);
        hasChocolateCheckBox.setChecked(false);

    }

    private void setNoWhippedCream () {
        CheckBox hasWhippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_check_box);
        hasWhippedCreamCheckBox.setChecked(false);

    }





}