package com.mobilhaber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobilhaber.arayuz.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static DatabaseReference database;
    private static final String DATABASE_URL = "https://mobilhaber-42cb5.firebaseio.com/";
    private static final String API_KEY = "AIzaSyBTKPbsxo450yU-9pSEGtqKX9FD3hxJqjI";
    private static final String APP_ID = "1:84357699223:android:f256dd0e258a5ac4";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        listView = findViewById(R.id.list);
        initDatabase();
        getHaberler("");

        Button btnYenile = findViewById(R.id.btnYenile);
        btnYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);
                getHaberler("");
            }
        });

        final Spinner cbKategori = findViewById(R.id.cbKategori);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.kategoriler, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbKategori.setAdapter(adapter);



        cbKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Tumu")) {
                    kategoriSecildi( "" );
                } else {
                    kategoriSecildi( parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                final Haber haber = (Haber) listView.getItemAtPosition(position);

                TextView baslik = popupView.findViewById(R.id.baslik);
                ImageView resim = popupView.findViewById(R.id.haberResmi);
                final TextView icerik = popupView.findViewById(R.id.icerik);


                final TextView begenmeSayisi = popupView.findViewById(R.id.begenmeSayisi);
                final TextView begenmemeSayisi = popupView.findViewById(R.id.begenmemeSayisi);
                TextView goruntulenmeSayisi = popupView.findViewById(R.id.goruntulenmeSayisi);

                final Button begen = popupView.findViewById(R.id.begen);
                begen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        begen(haber, begenmeSayisi, begen);
                    }
                });

                final Button begenme = popupView.findViewById(R.id.begenme);
                begenme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        begenme(haber, begenmemeSayisi, begenme);
                    }
                });


                updateGoruntulenme(haber);

                baslik.setText(haber.getBaslik());
                resim.setImageBitmap(haber.getResim());
                icerik.setText(haber.getIcerik());
                begenmeSayisi.setText(haber.getBegenmeSayisi() + "");
                begenmemeSayisi.setText(haber.getBegenmemeSayisi() + "");
                goruntulenmeSayisi.setText(haber.getGoruntulenmeSayisi()  + "");


                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });
    }

    private void kategoriSecildi(String secilenKategori ) {
        listView.setAdapter(null);
        getHaberler(secilenKategori);
    }

    private void begenme(Haber haber, TextView begenmemeSayisi, Button begenme) {
        DatabaseReference haberlerRef = database.getDatabase().getReference("haberler");
        DatabaseReference haberRef = haberlerRef.child(haber.getFirebaseId());
        Map<String, Object> update = new HashMap<>();
        haber.begenme();
        update.put("begenmemeSayisi", haber.getBegenmemeSayisi());

        begenmemeSayisi.setText(haber.getBegenmemeSayisi() + "");

        haberRef.updateChildren(update);
        begenme.setEnabled(false);
    }

    private void begen(Haber haber, TextView textView, Button begen) {
        DatabaseReference haberlerRef = database.getDatabase().getReference("haberler");
        DatabaseReference haberRef = haberlerRef.child(haber.getFirebaseId());
        Map<String, Object> update = new HashMap<>();
        haber.begen();
        update.put("begenmeSayisi", haber.getBegenmeSayisi());
        textView.setText(haber.getBegenmeSayisi() + "");

        haberRef.updateChildren(update);
        begen.setEnabled(false);
    }

    /**
     * goruntulenme sayisini 1 arrtır
     */
    void updateGoruntulenme(Haber haber) {
        DatabaseReference haberlerRef = database.getDatabase().getReference("haberler");
        DatabaseReference haberRef = haberlerRef.child(haber.getFirebaseId());
        Map<String, Object> update = new HashMap<>();
        haber.goruntulendi();
        update.put("goruntulenmeSayisi", haber.getGoruntulenmeSayisi());

        haberRef.updateChildren(update);
    }

    private void initDatabase() {
        if (database == null) {

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(API_KEY)
                .setApplicationId(APP_ID)
                .setDatabaseUrl(DATABASE_URL)
                .build();

            FirebaseApp finestayApp;
            boolean hasBeenInitialized = false;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps(getApplicationContext());
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    hasBeenInitialized = true;
                    finestayApp = app;
                }
            }

            if (!hasBeenInitialized) {
                finestayApp = FirebaseApp.initializeApp(getApplicationContext(), options);
            }

            database = FirebaseDatabase.getInstance().getReference();
        }
    }

    private void getHaberler(final String kategori) {
        DatabaseReference ref = database.getDatabase().getReference("haberler");
        Query id = ref.orderByChild("id");
        id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Haber> haberler = new ArrayList<>();


                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        if (issue.getValue() instanceof Map) {
                            HashMap map = (HashMap<String, String>) issue.getValue();
                            Haber haber = new Haber(map.get("id").toString(),
                                map.get("baslik").toString(),
                                map.get("icerik").toString(),
                                map.get("haberTuru").toString(),
                                map.get("yayinlanmaTarihi").toString(),
                                ((Long) map.get("begenmeSayisi")).intValue(),
                                ((Long) map.get("begenmemeSayisi")).intValue(),
                                ((Long) map.get("goruntulenmeSayisi")).intValue(),
                                map.get("resimStr").toString());

                            if (kategori.equals("") || kategori.equals("Tumu")) {
                                haber.setFirebaseId(issue.getKey());
                                haberler.add(haber);
                            } else {
                                if (haber.getHaberTuru().equals(HaberTuru.gundem) && kategori.equals("Gündem")) {
                                    haber.setFirebaseId(issue.getKey());
                                    haberler.add(haber);
                                } else if (haber.getHaberTuru().equals(HaberTuru.ekonomi) && kategori.equals("Ekonomi")) {
                                    haber.setFirebaseId(issue.getKey());
                                    haberler.add(haber);
                                } else if (haber.getHaberTuru().equals(HaberTuru.siyaset) && kategori.equals("Siyaset")) {
                                    haber.setFirebaseId(issue.getKey());
                                    haberler.add(haber);
                                } else if (haber.getHaberTuru().equals(HaberTuru.magazin) && kategori.equals("Magazin")) {
                                    haber.setFirebaseId(issue.getKey());
                                    haberler.add(haber);
                                } else if (haber.getHaberTuru().equals(HaberTuru.spor) && kategori.equals("Spor")) {
                                    haber.setFirebaseId(issue.getKey());
                                    haberler.add(haber);
                                }
                            }
                        }
                    }

                    ArrayAdapter<Haber> adapter = new ArrayAdapter<Haber>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, haberler);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private boolean kategoriVarmi(String gundem, String[] kategoriler) {
        for (String s : kategoriler) {
            if (s.equals(gundem)) {
                return true;
            }
        }
        return false;
    }
}
