package de.hs_esslingen.stundenplanapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SemesterListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SemesterListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SemesterListFragment extends Fragment implements View.OnClickListener,
        SemesterScheduleFragment.OnFragmentInteractionListener{
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "studiengang";

    private String studiengang;

    private OnFragmentInteractionListener mListener;

    public SemesterListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param studiengang Parameter 1.
     * @return A new instance of fragment SemesterListFragment.
     */
    public static SemesterListFragment newInstance(String studiengang) {
        SemesterListFragment fragment = new SemesterListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, studiengang);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studiengang = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.semesters_list, container, false);

        // Set action bar title
        ((MainActivity) getActivity()).setActionBarTitle(studiengang);

        // Loop through the 6 RelativeLayout elements
        for (int i = 1; i <= 7; i++) {
            if (i == 5){
                // Semester 5 has no schedule because it is the students internship
                continue;
            }
            else {
                // First we populate the texts of the "List"
                // Therefore we concatinate the identifier for the TextView
                String textViewIdentifier ="textViewSemester" + Integer.toString(i);
                int idTextView = getResources().getIdentifier(
                        textViewIdentifier, "id", getActivity().getPackageName());
                // Now find the TextView in the layout and change its text
                TextView listItem = (TextView) view.findViewById(idTextView);
                listItem.setText(studiengang + Integer.toString(i));

                // Add the onClickListener to the surrounding relativeLayout-Element
                // Therefore we concatinate the id of the relativeLayout to find it
                String relativeLayoutIdentifier ="semester" + Integer.toString(i) + "Row";
                int idRelativeLayout = getResources().getIdentifier(
                        relativeLayoutIdentifier, "id", getActivity().getPackageName());
                // Find the RelativeLayout in the layout
                RelativeLayout relativeLayoutElement =
                        (RelativeLayout) view.findViewById(idRelativeLayout);
                // Now we set the onClickListener to this
                relativeLayoutElement.setOnClickListener(this);
            }
        }
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
    public void onClick(View v) {

        // Handle semester navigation item clicks here.
        Fragment newFragment = new Fragment();
        FragmentManager fragmentManager = getFragmentManager();

        if (v.getId() == R.id.semester1Row) {
            newFragment = SemesterScheduleFragment.newInstance(studiengang, "1");
        } else if (v.getId() == R.id.semester2Row) {
            newFragment = SemesterScheduleFragment.newInstance(studiengang, "2");
        } else if (v.getId() == R.id.semester3Row) {
            newFragment = SemesterScheduleFragment.newInstance(studiengang, "3");
        } else if (v.getId() == R.id.semester4Row) {
            newFragment = SemesterScheduleFragment.newInstance(studiengang, "4");
        } else if (v.getId() == R.id.semester6Row) {
            newFragment = SemesterScheduleFragment.newInstance(studiengang, "6");
        } else if (v.getId() == R.id.semester7Row) {
            newFragment = SemesterScheduleFragment.newInstance(studiengang, "7");
        }

        fragmentManager.beginTransaction().replace(R.id.flContent, newFragment).addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
