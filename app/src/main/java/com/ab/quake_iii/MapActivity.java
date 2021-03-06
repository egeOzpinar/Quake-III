package com.ab.quake_iii;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.threetenabp.AndroidThreeTen;

/*import java.time.LocalDate;
import java.time.LocalTime;*/
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gMap;
    private RadioGroup radioGroupDepth;
    private RadioButton fromOneRadio;
    private RadioButton fromTwoRadio;
    private RadioButton fromThreeRadio;
    private RadioButton fromFourRadio;
    private RadioButton fromFiveRadio;
    private RadioButton fromSixRadio;
    private RadioButton fromSevenRadio;
    private RadioGroup radioGroupTime;
    private RadioButton allTimeRadio;
    private RadioButton twoDaysRadio;
    private RadioButton oneDayRadio;
    private RadioButton twelveHoursRadio;
    private RadioButton sixHoursRadio;
    private RadioButton threeHoursRadio;
    private RadioButton oneHourRadio;


    private List<Ping> pingList;
    private List<Ping> tempPingList;

    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyARLi5lVDsohtSSY2d0pCBCDIMlnl3K_Kg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        AndroidThreeTen.init(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            //Bundle'da Key'e karşılık mapViewBundle tutuluyor.Gereksiz, key göstermek yerine başka kelime konabilir
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);

        radioGroupDepth = findViewById(R.id.radioGroupDepth);
        fromOneRadio = findViewById(R.id.fromOneRadio);
        fromTwoRadio = findViewById(R.id.fromTwoRadio);
        fromThreeRadio = findViewById(R.id.fromThreeRadio);
        fromFourRadio = findViewById(R.id.fromFourRadio);
        fromFiveRadio = findViewById(R.id.fromFiveRadio);
        fromSixRadio = findViewById(R.id.fromSixRadio);
        fromSevenRadio = findViewById(R.id.fromSevenRadio);

        radioGroupTime = findViewById(R.id.radioGroupTime);
        allTimeRadio = findViewById(R.id.allTimeRadio);
        twoDaysRadio = findViewById(R.id.twoDaysRadio);
        oneDayRadio = findViewById(R.id.oneDayRadio);
        twelveHoursRadio = findViewById(R.id.twelveHoursRadio);
        sixHoursRadio = findViewById(R.id.sixHoursRadio);
        threeHoursRadio = findViewById(R.id.threeHoursRadio);
        oneHourRadio = findViewById(R.id.oneHourRadio);

        radioGroupDepth.setOnCheckedChangeListener(new RadioDepthListener());
        radioGroupTime.setOnCheckedChangeListener(new RadioTimeListener());

        pingList = MainActivity.getPingList();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //mapView.onResume()/onDestroy() gibi tüm lifecycle için ilgili method vardı ben sildim onları, bir daha düşün
        mapView.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(MapActivity.this, MainActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        addMarkerToMapWithDepth(3.0);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38,34), 5));
    }

    private class RadioDepthListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            if(i == R.id.fromOneRadio){
                addMarkerToMapWithDepth(1.0);
            }else if( i == R.id.fromTwoRadio){
                addMarkerToMapWithDepth(2.0);
            }else if( i == R.id.fromThreeRadio){
                addMarkerToMapWithDepth(3.0);
            }else if( i == R.id.fromFourRadio){
                addMarkerToMapWithDepth(4.0);
            }else if( i == R.id.fromFiveRadio){
                addMarkerToMapWithDepth(5.0);
            }else if( i == R.id.fromSixRadio){
                addMarkerToMapWithDepth(6.0);
            }else if( i == R.id.fromSevenRadio){
                addMarkerToMapWithDepth(7.0);
            }
        }
    }

    private void addMarkerToMapWithDepth(Double filterNum){
        tempPingList = new ArrayList<>();
        for(Ping p: pingList){
            if(p.getMagnitudeML() >= filterNum){
                tempPingList.add(p);
            }
        }
        new RadioTimeListener().onCheckedChanged(radioGroupTime, radioGroupTime.getCheckedRadioButtonId());
    }

    private class RadioTimeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.allTimeRadio){
                addMarkerToMapWithTime("D", 5);
            }else if( i == R.id.twoDaysRadio){
                addMarkerToMapWithTime("D",2);
            }else if( i == R.id.oneDayRadio){
                addMarkerToMapWithTime("D", 1);
            }else if( i == R.id.twelveHoursRadio){
                addMarkerToMapWithTime("T", 12);
            }else if( i == R.id.sixHoursRadio){
                addMarkerToMapWithTime("T",6);
            }else if( i == R.id.threeHoursRadio){
                addMarkerToMapWithTime("T",3);
            }else if( i == R.id.oneHourRadio){
                addMarkerToMapWithTime("T",1);
            }
        }
    }

    private void addMarkerToMapWithTime(String type, int timeOrDay){
        gMap.clear();
        if(type.equals("D")){
            LocalDate localDateD = LocalDate.now().minusDays(timeOrDay);
            LocalTime localTimeD = LocalTime.now();
            for(Ping p: tempPingList) {
                if(p.getDate().isAfter(localDateD) ||
                        (p.getDate().isEqual(localDateD) && p.getTime().isAfter(localTimeD))){
                    gMap.addMarker(p.getMarkerOptions());
                }
            }
        }else{
            LocalDate localDateT1 = LocalDate.now();
            LocalDate localDateT2 = LocalDate.of(1970,02,20);
            LocalTime localTimeNow = LocalTime.now();
            LocalTime localTimeT = localTimeNow.minusHours(timeOrDay);
            if(localTimeNow.getHour()<timeOrDay){
                localDateT1.minusDays(1);
                localDateT2 = localDateT1;
            }
            for(Ping p: tempPingList){
                if((p.getTime().isAfter(localTimeT) && p.getDate().isEqual(localDateT1)) ||
                        (p.getDate().isEqual(localDateT2))){
                    gMap.addMarker(p.getMarkerOptions());
                }
            }
        }
    }
}
