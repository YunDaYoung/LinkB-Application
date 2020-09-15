package com.example.linkb.mainFrag.homeInternalFrag;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.example.linkb.Adapter.RecyclerviewEventAdapter;
import com.example.linkb.Class.RecommendEvent;
import com.example.linkb.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainRecommendedEventFrag extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecommendEvent> eventList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_main_recommended_event, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recommend_event_list);

        new RestAPITaskRecommend("http://101.101.161.189/api/index.php/linkb_event/select_recommend_event_list", recyclerView).execute();

        return view;
    }

    public class RestAPITaskRecommend extends AsyncTask<Integer, Void, ArrayList<RecommendEvent>> {

        protected String mURL;
        RecyclerView recyclerView;

        public RestAPITaskRecommend(String mURL, RecyclerView recyclerView){
            this.mURL = mURL;
            this.recyclerView = recyclerView;
        }


        @Override
        protected ArrayList<RecommendEvent> doInBackground(Integer... params) {
            try{
                URL url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("apikey", "starthub");

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while((str = reader.readLine()) != null){
                    builder.append(str+ "\n");
                }
                String result = builder.toString();

                Log.e("data1", result);

                JSONObject getKey = new JSONObject(result);

                JSONArray jsonArray = (JSONArray)getKey.get("event_list");


                eventList = new ArrayList<>();
                for(int i =0; i< jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    eventList.add(new RecommendEvent(
                            jsonObject.get("event_name").toString(),
                            jsonObject.get("event_host").toString(),
                            jsonObject.get("event_start_date").toString(),
                            jsonObject.get("event_end_date").toString(),
                            jsonObject.get("event_location").toString(),
                            jsonObject.get("event_image").toString()
                    ));
                }

                return eventList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecommendEvent> eventList) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerviewEventAdapter adapter = new RecyclerviewEventAdapter(getContext(), eventList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

        }
    }
}