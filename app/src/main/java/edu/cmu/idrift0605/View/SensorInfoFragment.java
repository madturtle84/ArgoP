package edu.cmu.idrift0605.View;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import edu.cmu.idrift0605.R;


public class SensorInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SensorManager mSensorManager;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorInfoFragment newInstance(String param1, String param2) {
        SensorInfoFragment fragment = new SensorInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SensorInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView sensorInfoView = (TextView) view.findViewById(R.id.sensorInfoTitle);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());//.getTimeZone("America/New_York"));
        /* Get Current Time */
        String currentTime =
                (1+cal.get(Calendar.MONTH))+"/"+cal.get(Calendar.DATE) +"/"+cal.get(Calendar.YEAR)+" "+
                cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)
                ;

        /* Get SensorInfo */
        String sensorInfo = "Your phone is not smart enough. It has no sensor.";
        Context ctx = view.getContext();
        ctx.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorInfo="";
        for(int i=0; i<sensorList.size(); i++){
            sensorInfo += "  - " + sensorList.get(i).getName() +"\n";
        }

        /* Print the result on text view and logcat*/
        String result =
                "AndrewID: chihwei2 \n" +
                "Current Time: " + currentTime + "\n"+
                "Available Sensors: \n" + sensorInfo;
        sensorInfoView.setText(result);
        Log.i("[SensorInfo]", result);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
