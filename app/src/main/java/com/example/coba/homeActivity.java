package com.example.coba;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class homeActivity extends AppCompatActivity {
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds, Hours ;
    ListView listView;
    LinearLayout timerCard;
    TextView txtTimer;
    String[] ListElements = new String[]{};
    List<String>ListElementsArrayList;
    ArrayAdapter<String>adapter;
    //GridView gridView;
    TextView txtSuhuUdara, txtKelembapanUdara, txtKelembapanTanah, txtPhTanah,  txtSuhuAir, txtVolumeAir;
    int imgSuhu = R.drawable.suhu;
    int imgKelembapan = R.drawable.kelembapan;
    int imgDingin = R.drawable.cold;
    //DataSnapshot dataSnapshot;
    //Result result = dataSnapshot.getValue(Result.class);
    //String[] names = {"Suhu Udara","Kelembapan Udara","Kelembapan Tanah","Ph Tanah","Voulme Air","Suhu Air"};
    //int[] Icons = {R.drawable.cold,R.drawable.air_humidity,R.drawable.soil_humidity,R.drawable.soil_ph,R.drawable.water_level,R.drawable.water_temp};
    //String[] Datas = {result.Suhu_Udara.toString(),result.Kelembabapan_Udara.toString(),result.Kelembapan_Tanah.toString(),result.Ph_Tanah.toString(),result.Volume_Air.toString(),result.Suhu_Air.toString()};
    //String[] Labels = {"°C", "%", "%", " ", "%", "°C"};
    Button btnLogout;
    Button btnSiram;
    Button btnStop;
    FirebaseAuth mFirebaseAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    //private FirebaseRecyclerAdapter<Result> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        txtSuhuUdara = findViewById(R.id.nilai_suhu_udara);
        txtKelembapanUdara = findViewById(R.id.nilai_kelembapan_udara);
        txtKelembapanTanah = findViewById(R.id.nilai_kelembapan_tanah);
        txtPhTanah = findViewById(R.id.nilai_ph_tanah);
        txtSuhuAir = findViewById(R.id.nilai_suhu_air);
        txtVolumeAir = findViewById(R.id.nilai_volume_air);
        txtTimer = findViewById(R.id.timertext);
        timerCard = findViewById(R.id.timer);

        handler = new Handler();

        btnSiram = findViewById(R.id.btn_siram);
        btnSiram.setEnabled(true);
        btnSiram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                timerCard.setVisibility(VISIBLE);
                myRef.child("Pompa").setValue("1");
                btnSiram.setEnabled(false);
                btnStop.setEnabled(true);
            }
        });
        btnStop = findViewById(R.id.btn_stop);
        btnStop.setEnabled(false);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                txtTimer.setText("00:00:00");
                timerCard.setVisibility(INVISIBLE);
                myRef.child("Pompa").setValue("0");
            }
        });
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(homeActivity.this, MainActivity.class);
                startActivity(intToMain);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Result result = dataSnapshot.getValue(Result.class);
                Log.d("cekData", result.Pompa); //ngecek
                txtSuhuUdara.setText(result.Suhu_Udara.toString());
                txtKelembapanUdara.setText(result.Kelembapan_Udara.toString());
                txtKelembapanTanah.setText(result.Kelembaban_Tanah.toString());
                txtPhTanah.setText(result.Ph_Tanah.toString());
                txtSuhuAir.setText(result.Suhu_Air.toString());
                txtVolumeAir.setText(result.Volume.toString());

                String pompa = dataSnapshot.child("Pompa").getValue(String.class);
                Double suhuUdara = dataSnapshot.child("Suhu_Udara").getValue(Double.class);
                Long kelembapanUdara = dataSnapshot.child("Kelembapan_Udara").getValue(Long.class);
                Long kelembapanTanah = dataSnapshot.child("Kelembapan_Tanah").getValue(Long.class);

                //siram otomatis
                if(pompa.equals("1")){
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    timerCard.setVisibility(VISIBLE);
                    btnSiram.setEnabled(false);
                    btnStop.setEnabled(true);
                }else {
                    MillisecondTime = 0L ;
                    StartTime = 0L ;
                    TimeBuff = 0L ;
                    UpdateTime = 0L ;
                    Seconds = 0 ;
                    Minutes = 0 ;
                    MilliSeconds = 0 ;

                    txtTimer.setText("00:00:00");
                    timerCard.setVisibility(INVISIBLE);
                    btnSiram.setEnabled(true);
                    btnStop.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });



    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff+MillisecondTime;
            Seconds = (int) (UpdateTime/1000);
            Minutes = Seconds/60;
            Seconds = Seconds % 60;
            Hours = Minutes/60;

            MilliSeconds = (int) (UpdateTime % 1000);

            txtTimer.setText("" + String.format("%02d", Hours) + ":"
                    + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));

            handler.postDelayed(this, 0);
        }
    };

//Styling for double press back button
private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
    }



