package com.venkatesh.foodapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.ResponseBody;
import com.venkatesh.foodapp.Listener;
import com.venkatesh.foodapp.MyApplication;
import com.venkatesh.foodapp.R;
import com.venkatesh.foodapp.RetrofitService;
import com.venkatesh.foodapp.Utils;
import com.venkatesh.foodapp.adapters.OffersAdapter;
import com.venkatesh.foodapp.pojos.Coupans;
import com.venkatesh.foodapp.pojos.OffersPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllOffersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllOffersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllOffersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

/*<<<<<<< HEAD
    public AllOffersFragment() {
        // Required empty public constructor
    }

    *//**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllOffersFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static AllOffersFragment newInstance(String param1, String param2) {
        AllOffersFragment fragment = new AllOffersFragment();
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
        return inflater.inflate(R.layout.fragment_all_offers, container, false);
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
=======*/

    private ArrayList<OffersPojo> list_offers;
    private OffersAdapter adapter;
    private View main_view;
    private RecyclerView my_recycler_view;
    LinearLayoutManager layoutManager;
    ArrayList<Coupans> list;
    public AllOffersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllOffersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllOffersFragment newInstance(String param1, String param2) {
        AllOffersFragment fragment = new AllOffersFragment();
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
        main_view = inflater.inflate(R.layout.fragment_all_offers, container, false);
        return main_view;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        my_recycler_view  = main_view.findViewById(R.id.my_recycler_view);

//       ArrayList<OffersPojo> list = makeAList();
        list = new ArrayList<>();


        layoutManager = new LinearLayoutManager(getActivity());
        my_recycler_view.setLayoutManager(layoutManager);
        my_recycler_view.setItemAnimator(new DefaultItemAnimator());
        my_recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));

        my_recycler_view.setAdapter(adapter);



        getAllOffers();
    }

    private void getAllOffers() {

        Call<ResponseBody> call = MyApplication.getSerivce().getOffers_("1");
        call.enqueue(new Listener(new RetrofitService() {
            @Override
            public void onSuccess(String result, int pos, Throwable t) {

                if (pos==0){

                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    parseJson(result);
                }

            }
        }, "Fetching...", true, getActivity()));

    }

    private void parseJson(String result) {

        try {
            JSONObject obj = new JSONObject(result);



            JSONArray jsonarray = obj.getJSONArray("coupans");
            Type listType = new TypeToken<ArrayList<Coupans>>(){}.getType();
            List<Coupans> allCoupons =
                    new GsonBuilder().create().fromJson(jsonarray.toString(), listType);

            for(Coupans coupon: allCoupons){
                list.add(coupon);
            }



            adapter = new OffersAdapter(list, getActivity());
            my_recycler_view.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<OffersPojo> makeAList() {


        list_offers = new ArrayList<>();
        for (int i=0; i<12; i++){

            OffersPojo pojo = new OffersPojo();
            pojo.title = "Ramboo";
            pojo.description = "fsdkhgsjghjskghkjshgkjshgkjhsjkghjksghjshgjhjkshgjhjkgh";
            list_offers.add(pojo);
        }

        return list_offers;

//>>>>>>> 31fd9367fb03f34551a99c69b6ff4cb6678bbbb3
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Utils.REQ_CODE_EDIT_OFFER){
            getAllOffers();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
