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

import java.util.ArrayList;
import java.util.List;

import br.com.anderson.firebasechat.ChatActivity;
import br.com.anderson.firebasechat.R;
import br.com.anderson.firebasechat.adapter.AdapterUser;
import br.com.anderson.firebasechat.model.User;

public class FragmentUsuarios extends Fragment  {

    private RelativeLayout activityMain;
    private ListView list;
    AdapterUser adapter;
    private List<User> objects = new ArrayList<User>();

    private DatabaseReference mFirebaseDatabaseReference;
    FirebaseAuth auth;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuarios, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityMain = (RelativeLayout) view.findViewById(R.id.activity_main);
        list = (ListView) view.findViewById(R.id.list);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = preferences.getString("token","");


        adapter = new AdapterUser(getActivity(),objects);
        this.list.setAdapter(adapter);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("users");
//                .orderByChild("name")
//                .startAt("anderson").endAt("anderson");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                objects.clear();
                for (DataSnapshot data :dataSnapshot.getChildren()) {
                    if(!data.getKey().equalsIgnoreCase(token)){
                        Log.d("mFirebase", "onDataChange: "+ data.getValue().toString());
                        User user = data.getValue(User.class);  //new Gson().fromJson(data.getValue().toString(),User.class);
                        user.setId(data.getKey());
                        objects.add(user);
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
