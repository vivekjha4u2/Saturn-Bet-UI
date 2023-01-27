package com.yunt.secretvc;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BettingActivity extends AppCompatActivity {
    private String selectedColor;
    private TextView selectedColorTextView;
    private int selectedAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);


        findViewById(R.id.button_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectedColor("red");
                showAmountDialog("RED");
            }
        });

        findViewById(R.id.button_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectedColor("blue");
                showAmountDialog("BLUE");
            }
        });

        findViewById(R.id.button_violet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String color;
//                setSelectedColor("violet");
                showAmountDialog("GREEN");
            }
        });

    }
    private void showAmountDialog(String color) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an amount:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedAmount = Integer.parseInt(input.getText().toString());
                // here you can call the place bet method
                placeBet(color, selectedAmount);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void placeBet(String color, int selectedAmount) {
//        Toast.makeText(this, "Bet placed successfully! "+color+" "+selectedAmount, Toast.LENGTH_SHORT).show();
//        String color = getSelectedColor();
//        int amount = getSelectedAmount();
        // call the API endpoint to place the bet with the selected color and amount
        // or any other logic you have to place the bet


        // create the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("betAmount", selectedAmount);
            requestBody.put("bettingParameter", color);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // create the request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.38:7001/savePlayer", requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                int id = response.getInt("id");
                                int betAmount = response.getInt("betAmount");
                                String bettingParameter = response.getString("bettingParameter");
                                Toast.makeText(BettingActivity.this, "Bet placed successfully!\nBet ID: " + id + "\nBet Amount: " + betAmount + "\nBetting Parameter: " + bettingParameter, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(BettingActivity.this, "Failed to place bet. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null)
                            Log.e("VolleyError", error.getMessage());
                        Toast.makeText(BettingActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        // add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}

