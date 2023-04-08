package com.example.qrscannertest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ScanActivity extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;
    HashMap dataSet;

    List<String> attended = new ArrayList<>();
    String points;
    char ch;
    int cnt;
    String scannedData;

    String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanCode();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            points = (String) bundle.get("points");
            key = (String) bundle.get("key");
        }

        database = FirebaseDatabase.getInstance("https://eccloginmoduletest-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("users");

    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
            builder.setTitle("Result");

//            reading and converting the string
            scannedData=result.getContents();

//            counting the number of times # appears to make sure data is valid
            ch = '#';
            cnt = 0;
            for ( int i = 0; i < scannedData.length(); i++) {
                if (scannedData.charAt(i) == ch)
                    cnt++;
            }

            String[] arrOfStr = scannedData.split("#", 0);
            if(cnt==2) {
//                for (String a : arrOfStr)    //debug
//                    Log.d("data ", "data is: "+a);
                scannedData = "Name: " + arrOfStr[0] + "\n" + "UID: " + arrOfStr[1] + "\n" + "Class: " + arrOfStr[2];
            }else{
                scannedData="This is not an official SXC ECC QRcode... Please try again or contact the admin";
            }

//            builder.setMessage(result.getContents());
//            builder.setMessage("this is the msg");
            builder.setMessage(scannedData);

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());


            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Log.e("params", "uid: " + arrOfStr[1] + " points: " + points);
                    updateDB(arrOfStr[1], String.valueOf(points), String.valueOf(key));
                    dialogInterface.dismiss();
//                    Intent k =  new Intent(ScanActivity.this, ViewEvents.class);
//                    startActivity(k);
                    scanCode();
                }
            }).show();
        }
    });


    private void updateDB(String uid, String points, String key) {
        // Read from the database

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String a = String.valueOf(task.getResult().getValue());

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    dataSet = (HashMap) task.getResult().getValue();
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    myRef.child(uid).child("eventsAttended").get().addOnCompleteListener(task1 -> {
                        attended= (List<String>) task1.getResult().getValue();
//                        Log.e("1610", "attended: " +attended );
                        if(attended != null) {
                            if(!attended.contains(key)) {
                                while (attended.remove(null)) {
                                }
                                attended.add(key);
                                myRef.child(uid).child("eventsAttended").setValue(attended);
                                myRef.child(uid).child("score").setValue(Integer.parseInt(String.valueOf(((HashMap) dataSet.get(uid)).get("score"))) + Integer.parseInt(points));
                            }
                            else{
                                Toast.makeText(ScanActivity.this, "Event already attended by student", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            List<String> attended = new ArrayList<>();
                            attended.add(key);
                            myRef.child(uid).child("eventsAttended").setValue(attended);
                            myRef.child(uid).child("score").setValue(Integer.parseInt(String.valueOf(((HashMap) dataSet.get(uid)).get("score"))) + Integer.parseInt(points));
                        }

                        Log.d("lol", "array data "+attended);
                    });
                }
            }
        });

        Log.d("firebase", "updateDB: "+dataSet);

    }
}
