package com.mateusz.bakingapp2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mateusz.bakingapp2.Adapter.IngredientsAdapter;
import com.mateusz.bakingapp2.Adapter.StepsAdapter;
import com.mateusz.bakingapp2.Model.Ingredient;
import com.mateusz.bakingapp2.Model.Step;

import java.util.List;

public class MasterFragment extends Fragment {

    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    private List<Ingredient> mListIngredients;
    private List<Step> mListSteps;



    public MasterFragment() {
        // Required empty public constructor
    }

    public void setData(List<Ingredient> ingredients, List<Step> steps){
        mListIngredients=ingredients;
        mListSteps=steps;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        LinearLayoutManager ingredientsManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ingredientsManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerViewIngredients = rootView.findViewById(R.id.fragment_master_recycler_view_ingredients);
        recyclerViewIngredients.setLayoutManager(ingredientsManager);
        recyclerViewIngredients.setHasFixedSize(true);
        mIngredientsAdapter = new IngredientsAdapter(getContext(), mListIngredients);
        recyclerViewIngredients.setAdapter(mIngredientsAdapter);

        LinearLayoutManager stepsManager = new LinearLayoutManager(getContext());
        stepsManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_master_recycler_view_steps);
        recyclerView.setLayoutManager(stepsManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                stepsManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        mStepsAdapter = new StepsAdapter(getContext(), mListSteps);
        recyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }































    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static MasterFragment newInstance(String param1, String param2) {
        MasterFragment fragment = new MasterFragment();
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

}
