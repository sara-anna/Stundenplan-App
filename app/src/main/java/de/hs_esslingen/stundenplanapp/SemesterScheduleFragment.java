package de.hs_esslingen.stundenplanapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SemesterScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SemesterScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SemesterScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE_OF_STUDIES = "courseOfStudies";
    private static final String ARG_SEMESTER = "semester";

    // TODO: Rename and change types of parameters
    private String courseOfStudies;
    private String semester;

    private OnFragmentInteractionListener mListener;

    public SemesterScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param courseOfStudies The Course of studies (SWB, TIB, WKB).
     * @param semester The Semester (1,2,3,...).
     * @return A new instance of fragment SemesterScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SemesterScheduleFragment newInstance(String courseOfStudies, String semester) {
        SemesterScheduleFragment fragment = new SemesterScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_OF_STUDIES, courseOfStudies);
        args.putString(ARG_SEMESTER, semester);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseOfStudies = getArguments().getString(ARG_COURSE_OF_STUDIES);
            semester = getArguments().getString(ARG_SEMESTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Set action bar title
        ((MainActivity) getActivity()).setActionBarTitle(courseOfStudies + " " + semester + ". Semester");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getView().post(new Runnable() {
                           @Override
                           public void run() {
                               // code you want to run when view is visible for the first time

                               // Define a projection that specifies which columns from the database
                               // you will actually use after this query.
                               String[] projection = {
                                       ScheduleContract.TableLecture._ID ,
                                       ScheduleContract.TableLecture.COLUMN_NAME_NAME,
                                       ScheduleContract.TableLecture.COLUMN_NAME_LECTURER,
                                       ScheduleContract.TableLecture.COLUMN_NAME_ROOM,
                                       ScheduleContract.TableLecture.COLUMN_NAME_DAY_OF_WEEK,
                                       ScheduleContract.TableLecture.COLUMN_NAME_TIME,
                                       ScheduleContract.TableLecture.COLUMN_NAME_CANCELLED_ON,
                                       ScheduleContract.TableLecture.COLUMN_NAME_COURSE_OF_STUDIES,
                                       ScheduleContract.TableLecture.COLUMN_NAME_SEMESTER
                               };

                               // Filter results WHERE "title" = 'My Title'
                               String selection = ScheduleContract.TableLecture.COLUMN_NAME_SEMESTER +
                                       " = ? AND " + ScheduleContract.TableLecture.COLUMN_NAME_COURSE_OF_STUDIES + "= ?";
                               String[] selectionArgs = { semester, courseOfStudies };

                               ((MainActivity) getActivity()).queryDbAndaddDbEntriesAsButtonToView(
                                       projection, selection, selectionArgs);
                           }
                       }
        );
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
