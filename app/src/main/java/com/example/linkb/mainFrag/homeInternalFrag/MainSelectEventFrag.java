package com.example.linkb.mainFrag.homeInternalFrag;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.linkb.Adapter.RecyclerviewEventAdapter;
import com.example.linkb.Adapter.SelectEventListAdapter;
import com.example.linkb.Class.RecommendEvent;
import com.example.linkb.Class.SelectEvent;
import com.example.linkb.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainSelectEventFrag extends Fragment {

    RecyclerView recyclerView;
    ArrayList<SelectEvent> eventList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main_select_event, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.select_event_list);

        new RestAPITaskRecommend("http://101.101.161.189/api/index.php/linkb_event/select_event_list", recyclerView).execute();

        return view;
    }

    public class RestAPITaskRecommend extends AsyncTask<Integer, Void, ArrayList<SelectEvent>> {

        protected String mURL;
        RecyclerView recyclerView;

        public RestAPITaskRecommend(String mURL, RecyclerView recyclerView){
            this.mURL = mURL;
            this.recyclerView = recyclerView;
        }


        @Override
        protected ArrayList<SelectEvent> doInBackground(Integer... params) {
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
                String startLine;
                boolean start = false;
                while((str = reader.readLine()) != null){
                    if(!start){
                        if(str.indexOf("{")!=-1) {
                            startLine = str.substring(str.indexOf("{"));
                            builder.append(startLine + "\n");
                            start = true;
                        }
                    } else{
                        builder.append(str+ "\n");
                    }
                }
                String result = builder.toString();

                Log.e("data2", result);

                JSONObject getKey = new JSONObject(result);

                JSONArray jsonArray = (JSONArray)getKey.get("event_list");

                Log.e("data", String.valueOf(jsonArray.length()));
                eventList = new ArrayList<>();
                for(int i =0; i< jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    eventList.add(new SelectEvent(
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
        protected void onPostExecute(ArrayList<SelectEvent> eventList) {

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            SelectEventListAdapter adapter = new SelectEventListAdapter(getContext(), eventList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

        }
    }
}