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

import java.util.HashMap;


public class ScanActivity extends AppCompatActivity {
    DatabaseReference myRef;
    FirebaseDatabase database;
    HashMap dataSet;
    String points;
    char ch;
    int cnt;
    String scannedData;

    String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanCode();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            points = (String) bundle.get("points");
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
//                    dialogInterface.dismiss();
                    updateDB(arrOfStr[1], String.valueOf(points));
                    dialogInterface.dismiss();
//                    Intent k =  new Intent(ScanActivity.this, ViewEvents.class);
//                    startActivity(k);
                    scanCode();

                }
            }).show();
        }
    });

    private void updateDB(String uid, String points) {
        // Read from the database

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    dataSet = (HashMap) task.getResult().getValue();

                    myRef.child(uid).child("score").setValue(Integer.parseInt(String.valueOf(((HashMap) dataSet.get(uid)).get("score"))) + Integer.parseInt(points));
                }
            }
        });

        Log.d("firebase", "updateDB: "+dataSet);

    }
}
