package com.example.a233.staringatghost;


import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTool {


    public List<Msg> turnmore(String jsonData){
        List<Msg> res=new ArrayList<Msg>();;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                Msg temp=new Msg();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String score = jsonObject.getString("score");
                temp.setId(id);
                temp.setScore(score);
                res.add(temp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
