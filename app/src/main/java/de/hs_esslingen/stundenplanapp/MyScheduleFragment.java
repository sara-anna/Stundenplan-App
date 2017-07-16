package de.hs_esslingen.stundenplanapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MyScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyScheduleFragment newInstance(String param1, String param2) {
        MyScheduleFragment fragment = new MyScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Set action bar title
        ((MainActivity) getActivity()).setActionBarTitle("Mein Stundenplan");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getView().post(new Runnable() {
                   @Override
                   public void run() {
                   // Code you want to run when view is visible for the first time.
                   // We need this, because the lectures are aligned to other elements in the layout
                   // that can only be accessed, if the view is already visible

                   // Define a projection that specifies which columns from the database
                   // we will actually use after this query.
                   String[] projection = {
                           ScheduleContract.TableLecture._ID ,
                           ScheduleContract.TableLecture.COLUMN_NAME_NAME,
                           ScheduleContract.TableLecture.COLUMN_NAME_LECTURER,
                           ScheduleContract.TableLecture.COLUMN_NAME_ROOM,
                           ScheduleContract.TableLecture.COLUMN_NAME_DAY_OF_WEEK,
                           ScheduleContract.TableLecture.COLUMN_NAME_TIME,
                           ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE
                   };

                   // Filter results WHERE "studentTakesCourse" = true
                    String selection = ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE + " = ?";
                    String[] selectionArgs = { "1" };

                   // Call the logic for db query and adding lectures to the schedule
                   ((MainActivity) getActivity()).queryDbAndaddDbEntriesAsButtonToView(
                           projection, selection, selectionArgs);

               }
           }
        );

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
