package com.example.qrscannertest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    Context context;
    List<Event> events;
    DatabaseReference myRef;
    FirebaseDatabase database;

    HashMap dataSet;

    int points;
    EditText edtxtPoints;
    char ch;
    int cnt;
    String scannedData;


    public MyAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    public MyAdapter() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.event_view,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(events.get(position).getEventName());
        holder.department.setText(events.get(position).getDepartment());
        holder.date.setText((events.get(position).getDate()));
        holder.time.setText((events.get(position).getTime()));
        holder.venue.setText((events.get(position).getVenue()));
        holder.faculty.setText((events.get(position).getFaculty()));
        holder.points.setText((events.get(position).getPoints()));
        holder.btn_scan.setOnClickListener(view ->{
            //scanCode();
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }
}