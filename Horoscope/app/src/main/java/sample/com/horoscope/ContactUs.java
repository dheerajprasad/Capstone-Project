package sample.com.horoscope;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactUs extends AppCompatActivity  implements OnMapReadyCallback {

    MarkerOptions renton;

    GoogleMap m_map;
    boolean mapReady=false;

    static final CameraPosition TAMPINESE = CameraPosition.builder()
            .target(new LatLng(1.361649, 103.892417))
            .zoom(15)
            .bearing(0)
            .tilt(45)
            .build();
//    LatLng rentonltn =  new LatLng(1.361649, 103.892417);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        renton = new MarkerOptions()
                .position(new LatLng(1.361649, 103.892417))
                .title("Mobi Future")
        ;




        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map){
        mapReady=true;
        m_map = map;

        m_map.addMarker(renton);

      /*  LatLng newYork = new LatLng(40.7484,-73.9857);
        CameraPosition target = CameraPosition.builder().target(newYork).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
*/
        flyTo(TAMPINESE);
    }
    private void flyTo(CameraPosition target)
    {
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 6000, null);



    }

}
