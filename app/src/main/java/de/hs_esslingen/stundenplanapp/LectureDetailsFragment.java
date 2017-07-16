package de.hs_esslingen.stundenplanapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LectureDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LectureDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LectureDetailsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LECTURE_ID = "lectureId";

    private int lectureId;

    // Instances of DbHelper and database itself
    ScheduleDbHelper mDbHelper;
    SQLiteDatabase db;

    private OnFragmentInteractionListener mListener;

    public int getLectureId() {
        return lectureId;
    }


    public LectureDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lectureId Parameter 1.
     * @return A new instance of fragment LectureDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LectureDetailsFragment newInstance(int lectureId) {
        LectureDetailsFragment fragment = new LectureDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LECTURE_ID, lectureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lectureId = getArguments().getInt(ARG_LECTURE_ID);
            System.out.println(lectureId);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item= menu.findItem(R.id.action_take_course);
        item.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_details, container, false);
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
                                       ScheduleContract.TableLecture._ID,
                                       ScheduleContract.TableLecture.COLUMN_NAME_NAME,
                                       ScheduleContract.TableLecture.COLUMN_NAME_LECTURER,
                                       ScheduleContract.TableLecture.COLUMN_NAME_ROOM,
                                       ScheduleContract.TableLecture.COLUMN_NAME_DAY_OF_WEEK,
                                       ScheduleContract.TableLecture.COLUMN_NAME_TIME,
                                       ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE,
                                       ScheduleContract.TableLecture.COLUMN_NAME_SEMESTER,
                                       ScheduleContract.TableLecture.COLUMN_NAME_COURSE_OF_STUDIES,
                                       ScheduleContract.TableLecture.COLUMN_NAME_CANCELLED_ON
                               };

                               // Filter results WHERE "title" = 'My Title'
                               String selection = ScheduleContract.TableLecture._ID + " = ?";
                               String[] selectionArgs = {String.valueOf(lectureId)};

                               Cursor cursor = db.query(
                                       ScheduleContract.TableLecture.TABLE_NAME, // The table to query
                                       projection,                               // The columns to return
                                       selection,                             // The columns for the WHERE clause
                                       selectionArgs,                        // The values for the WHERE clause
                                       null,                                     // don't group the rows
                                       null,                                     // don't filter by row groups
                                       null                                      // don't sort
                               );

                               if(cursor != null) {
                                   if (cursor.moveToFirst()) {
                                       // Get all values and cache them in variables
                                       final long itemId = cursor.getLong(
                                               cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture._ID));
                                       String itemTime = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_TIME));
                                       String itemLecturer = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_LECTURER));
                                       String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_NAME));
                                       String itemRoom = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_ROOM));
                                       String itemCancelledOn = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_CANCELLED_ON));
                                       String itemCourseOfStudies = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_COURSE_OF_STUDIES));
                                       String itemSemester = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_SEMESTER));
                                       int itemIsTakenByStudent = cursor.getInt(cursor.getColumnIndexOrThrow(ScheduleContract.TableLecture.COLUMN_NAME_STUDENT_TAKES_COURSE));

                                       // Set action bar title
                                       ((MainActivity) getActivity()).setActionBarTitle(itemDescription);

                                       // Set values for the layout elements
                                       ((TextView) getView().findViewById(R.id.textViewLectureName)).setText(itemDescription);
                                       ((TextView) getView().findViewById(R.id.textViewLehrperson)).setText(itemLecturer);
                                       ((TextView) getView().findViewById(R.id.textViewRaum)).setText(itemRoom);
                                       ((TextView) getView().findViewById(R.id.textViewFaelltAusAm)).setText(itemCancelledOn);
                                       ((TextView) getView().findViewById(R.id.textViewStudiengang)).setText(itemCourseOfStudies);
                                       ((TextView) getView().findViewById(R.id.textViewSemester)).setText(itemSemester);

                                       if (itemIsTakenByStudent == 1){
                                           ((TextView) getView().findViewById(R.id.textViewBelegt)).setText("Ja");
                                       }
                                       else if (itemIsTakenByStudent == 0){
                                           ((TextView) getView().findViewById(R.id.textViewBelegt)).setText("Nein");
                                       }
                                   }
                               }
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

        // initialize DbHelper and get the database
        mDbHelper = new ScheduleDbHelper(context);
        db = mDbHelper.getReadableDatabase();

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
