package edu.cmu.idrift0605.View;


import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.idrift0605.Model.ParkingData;
import edu.cmu.idrift0605.R;
import edu.cmu.idrift0605.Utilities.DrawRouteTask;

public class FindPositionFragment extends Fragment implements
        OnMapReadyCallback,
        View.OnFocusChangeListener,
        View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Geocoder mGeocoder;
    private LocationManager mLocationManager;
    private boolean mIsFocused=true;
    private TextView mParkingTimeView;
    private TextView mParkingNoteView;
    private Button mGetDirectionButton;

    public FindPositionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_position, container, false); // Inflate the layout for this fragment
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* Set references for UI elements */
        mParkingNoteView = (TextView) view.findViewById(R.id.parkingNoteRecord);
        mParkingTimeView = (TextView) view.findViewById(R.id.parkingTimeRecord);
        mGetDirectionButton = (Button) view.findViewById(R.id.button_get_direction);
        mGetDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawDirectionOnMap();
            }
        });

        refreshParkingData();
    }

    public void onButtonPressed(String id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        /* Initialize Variables */
        mMap = map;
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        mGeocoder = new Geocoder(getActivity().getBaseContext());
        /* Get User's location */
        LatLng userLoc = getUserPosition();

        /* Reverse Geocoding: Transfer coordinate into address */
        String address = coordinateToAddress(userLoc);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 13f)); // Zooming level: 2.0f ~ 21.0f

        /* Update User's location every 1 sec */
        //updateUserLocation();
    }

    Timer mUserLocationTimer = new Timer();
    private void updateUserLocation(){

        /* Define The Action of this subroutine */
        if(mIsFocused) getUserPosition();


        /* Make this function run repeatly */
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                updateUserLocation();
            }
        };
        mUserLocationTimer.schedule(timerTask, 2000);
    }

    private LatLng getUserPosition(){
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(lastKnownLocation==null) return new LatLng(-34, 151);// Placeholder location (Sydney);
        LatLng userLoc = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        Log.i("[User Location] ", "(" + userLoc.latitude + ", " + userLoc.longitude + ")");
        return userLoc;
    }
    @Override
    public void onFocusChange (View v, boolean hasFocus){
        //Log.i("[changing focus] ", v.toString());
        if(v!=null) Log.i("[changing focus] ", v.toString());
        mIsFocused = hasFocus;

        /* Update Data on UI */
        refreshParkingData();

        if(mMap!=null) mMap.setMyLocationEnabled(mIsFocused);
    }

    private String coordinateToAddress(LatLng coordinate){
        List<Address> addrList=new ArrayList<Address>();
        try{
            addrList= mGeocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 5);
        }
        catch (java.io.IOException  e){
            e.printStackTrace();
        }

        String address="";
        int numAddrLines=addrList.get(0).getMaxAddressLineIndex();
        for(int i=0; i<=numAddrLines; i++){
            address += addrList.get(0).getAddressLine(i);
            if(i!=numAddrLines) address+=", ";
        }
        return address;
    }

    private void drawDirectionOnMap(){
        if(mParkingPositionMarker==null) {
            Log.d("[DrawDirection] ", "Marker not found...Please try later.");
            return;
        }
        new DrawRouteTask(mMap, getUserPosition(),mParkingPositionMarker.getPosition()).execute();

    }

    private void refreshParkingData(){
        if(mParkingNoteView==null || mParkingTimeView==null) return;
        mParkingTimeView.setText(ParkingData.parkingTime);
        mParkingNoteView.setText(ParkingData.parkingNote);
        updateParkingPosition();
    }

    Marker mParkingPositionMarker;
    private void updateParkingPosition(){
        /* If map is not ready */
        if(mMap==null) return;
        /* If is first time */
        if(mParkingPositionMarker==null){
            mParkingPositionMarker = mMap.addMarker(new MarkerOptions()
                            .position(ParkingData.parkingPosition)
                            .title("Your car is here")
                            .snippet("...")
            );
        }
        /* Normal case. Just update the marker position */
        else{
            mParkingPositionMarker.setPosition(ParkingData.parkingPosition);
        }
        mParkingPositionMarker.showInfoWindow();
    }

    private void shareAtSocialMedia(){

    }

}
