package com.randyperrone.randyperrone.positivityshare.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import static com.randyperrone.randyperrone.positivityshare.Model.Consts.API_OBJECT_COMPLIMENT;

public class ComplimentDownloadService {
    private final String TAG = "CompDownloadService";
    private RequestQueue requestQueue;
    private String compliment;

    public ComplimentDownloadService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void downloadCompliment(String URL, final VolleyCallBack callBack){
        final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try{
                        compliment = response.getString(API_OBJECT_COMPLIMENT);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    Log.i(TAG, "Volley Success");
                    callBack.onSuccess(compliment);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        requestQueue.add(objectRequest);
    }

    public interface VolleyCallBack{
        void onSuccess(String compliment);
    }


}
