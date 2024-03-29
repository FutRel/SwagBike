package com.example.maptest;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.example.maptest.recycler.RecordAdapter;
import com.example.maptest.recycler.RecordForRecycler;
import com.example.maptest.recycler.RecyclerItemSpace;
import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private float totalDistance = 0;
    private ArrayList<RecordForRecycler> arrayListRecords;
    private boolean sortDistFlag = true;
    private boolean sortTimeFlag = true;
    private boolean sortDateFlag = false;
    TextView tvTotalDist;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        rg = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb3.setChecked(true);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        },
                new int[]
                        {
                                getResources().getColor(R.color.biruze),
                                Color.BLACK,
                        }
        );
        rb1.setButtonTintList(colorStateList);
        rb2.setButtonTintList(colorStateList);
        rb3.setButtonTintList(colorStateList);
        RecordsDBHelper rdbHelper = new RecordsDBHelper(this);
        SQLiteDatabase db = rdbHelper.getReadableDatabase();
        String[] columns = {
                RecordsContract.ClassForRecords._id,
                RecordsContract.ClassForRecords.column_distance,
                RecordsContract.ClassForRecords.column_time,
                RecordsContract.ClassForRecords.column_date};
        Cursor cursor = db.query(
                RecordsContract.ClassForRecords.table_name,
                columns, null, null, null, null, null
        );
        ArrayList<RecordForRecycler> arrayListRecordsNotReversed = new ArrayList<>();
        arrayListRecords = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            float distance = cursor.getFloat(1);
            int time = cursor.getInt(2);
            String dateToItem = cursor.getString(3);
            String distanceToItem = String.format("%.2f", distance / 1000) + " км";
            String timeHours = "" + time / 3600;
            if(timeHours.length() == 1) timeHours = "0" + timeHours;
            String timeMin = "" + time % 3600 / 60;
            if(timeMin.length() == 1) timeMin = "0" + timeMin;
            String timeSec = "" + time % 3600 % 60;
            if(timeSec.length() == 1) timeSec = "0" + timeSec;
            String timeToItem = timeHours + ":" + timeMin + ":" + timeSec;
            arrayListRecordsNotReversed.add(new RecordForRecycler(id, distanceToItem, timeToItem, dateToItem));
            totalDistance += distance;
        }
        cursor.close();
        tvTotalDist = findViewById(R.id.arvdist);
        tvTotalDist.setText(String.format("%.2f", totalDistance / 1000) + " км");
        if(!arrayListRecordsNotReversed.isEmpty()){
            for (int i = arrayListRecordsNotReversed.size() - 1; i >= 0; i--) {
                arrayListRecords.add(arrayListRecordsNotReversed.get(i));
            }
            recyclerView = findViewById(R.id.list);
            recyclerView.addItemDecoration(new RecyclerItemSpace(10));
            RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
                Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
                intent.putExtra("idOfRecord", arrayListRecords.get(position).getId());
                startActivity(intent);
            };
            RecordAdapter adapter = new RecordAdapter(this, arrayListRecords, recordClickListener);
            recyclerView.setAdapter(adapter);
        }
    }

    public void back(View view){startActivity(new Intent(RecyclerViewActivity.this, MainActivity.class));}

    public void sortDist(View view){
        if(!arrayListRecords.isEmpty()){
            ArrayList<RecordForRecycler> arrayList = new ArrayList<>(arrayListRecords);
            ColorStateList colorStateList;
            if(sortDistFlag) {
                arrayList.sort(RecordForRecycler.compareByDistReversed);
                colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                        new int[]
                                {
                                        getResources().getColor(R.color.biruze),
                                        Color.BLACK,
                                }
                );
            }
            else {
                arrayList.sort(RecordForRecycler.compareByDist);
                colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                        new int[]
                                {
                                        getResources().getColor(R.color.purple_200),
                                        Color.BLACK,
                                }
                );
            }
            rb1.setButtonTintList(colorStateList);
            RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
                Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
                intent.putExtra("idOfRecord", arrayList.get(position).getId());
                startActivity(intent);
            };
            RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
            recyclerView.setAdapter(adapter);
            sortDistFlag = !sortDistFlag;
            sortTimeFlag = true;
            sortDateFlag = true;
        }
    }

    public void sortTime(View view){
        if(!arrayListRecords.isEmpty()){
            ArrayList<RecordForRecycler> arrayList = new ArrayList<>(arrayListRecords);
            ColorStateList colorStateList;
            if(sortTimeFlag) {
                arrayList.sort(RecordForRecycler.compareByTimeReversed);
                colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                        new int[]
                                {
                                        getResources().getColor(R.color.biruze),
                                        Color.BLACK,
                                }
                );
            }
            else {
                arrayList.sort(RecordForRecycler.compareByTime);
                colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                        new int[]
                                {
                                        getResources().getColor(R.color.purple_200),
                                        Color.BLACK,
                                }
                );
            }
            rb2.setButtonTintList(colorStateList);
            RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
                Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
                intent.putExtra("idOfRecord", arrayList.get(position).getId());
                startActivity(intent);
            };
            RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
            recyclerView.setAdapter(adapter);
            sortTimeFlag = !sortTimeFlag;
            sortDistFlag = true;
            sortDateFlag = true;
        }
    }

    public void sortDate(View view){
        if(!arrayListRecords.isEmpty()){
            ArrayList<RecordForRecycler> arrayList = new ArrayList<>(arrayListRecords);
            ColorStateList colorStateList;
            if(sortDateFlag) {
                colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                        new int[]
                                {
                                        getResources().getColor(R.color.biruze),
                                        Color.BLACK,
                                }
                );
            }
            else {
                Collections.reverse(arrayList);
                colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                        new int[]
                                {
                                        getResources().getColor(R.color.purple_200),
                                        Color.BLACK,
                                }
                );
            }
            rb3.setButtonTintList(colorStateList);
            RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
                Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
                intent.putExtra("idOfRecord", arrayList.get(position).getId());
                startActivity(intent);
            };
            RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
            recyclerView.setAdapter(adapter);
            sortDateFlag = !sortDateFlag;
            sortDistFlag = true;
            sortTimeFlag = true;
        }
    }
}