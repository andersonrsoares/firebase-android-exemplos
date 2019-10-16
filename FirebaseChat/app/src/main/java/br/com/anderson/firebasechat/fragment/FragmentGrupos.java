package br.com.anderson.firebasechat.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.anderson.firebasechat.ChatActivity;
import br.com.anderson.firebasechat.R;
import br.com.anderson.firebasechat.adapter.AdapterGroup;
import br.com.anderson.firebasechat.model.Group;
import br.com.anderson.firebasechat.model.User;

public class FragmentGrupos extends Fragment  {

    private RelativeLayout activityMain;
    private ListView list;

    private DatabaseReference mFirebaseDatabaseReference;
    FirebaseAuth auth;
    String token;
    Map<String,JSONObject> groups = new HashMap<>();

    AdapterGroup adapter;
    private List<Group> objects = new ArrayList<Group>();

  //  Map<String,Group> groups = new HashMap<String,Group>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grupos, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityMain = (RelativeLayout) view.findViewById(R.id.activity_main);
        list = (ListView) view.findViewById(R.id.list);

        adapter = new AdapterGroup(getActivity(),objects);
        this.list.setAdapter(adapter);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group = (Group) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra("group",group);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = preferences.getString("token","");


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("group")
                .orderByChild(token)
                .equalTo(true);
//                .orderByChild("name")
//                .startAt("anderson").endAt("anderson");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             //   objects.clear();
                for (DataSnapshot data :dataSnapshot.getChildren()) {
                    Log.d("mFirebase", "onDataChange: "+ data.getValue().toString());

                    try {
                        //Group group = data.getValue(Group.class);
                        groups.put(data.getKey(),new JSONObject(data.getValue().toString()));
                    }catch (Exception ex){
                        Log.d("mFirebase", "onDataChange: "+ ex);
                    }
                    updateusers();
                }
              //  adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("mFirebase", "onCancelled: "+ databaseError.getMessage());
            }
        });

    }


    void updateusers(){
        Query queryusers = mFirebaseDatabaseReference.child("users");
        queryusers.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  objects.clear();
                objects.clear();
                for (DataSnapshot data :dataSnapshot.getChildren()) {
                     for (Map.Entry<String, JSONObject> entry : groups.entrySet())
                     {
                         if(entry.getValue().has(data.getKey()))
                         {
                             if(entry.getValue().optBoolean("single")){
                                 if(!data.getKey().equalsIgnoreCase(token)){
                                     User user = data.getValue(User.class);
                                     Group group = new Group();
                                     group.setId(entry.getKey());
                                     if(entry.getValue().has("name")){
                                         group.setName(entry.getValue().optString("name"));
                                     }else{
                                         group.setName(user.getEmail());
                                     }
                                     objects.add(group);
                                 }
                             }else{
                                 User user = data.getValue(User.class);
                                 Group group = new Group();
                                 group.setId(entry.getKey());
                                 if(entry.getValue().has("name")){
                                     group.setName(entry.getValue().optString("name"));
                                 }else{
                                     group.setName(user.getEmail());
                                 }
                                 objects.add(group);
                             }

                         }
                     }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("mFirebase", "onDataChange: "+ databaseError.getMessage());
            }
        });
    }


}
