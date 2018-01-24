package br.com.anderson.firebasechat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import br.com.anderson.firebasechat.model.Chat;
import br.com.anderson.firebasechat.model.Group;
import br.com.anderson.firebasechat.model.Message;
import br.com.anderson.firebasechat.model.User;

public class ChatActivity extends AppCompatActivity {



    private DatabaseReference mFirebaseDatabaseReference;
    FirebaseAuth auth;


    private List<User> objects = new ArrayList<User>();
    private android.widget.EditText editTextmensagem;
    private android.widget.Button buttonenviar;
    private RelativeLayout bottomlauyout;
    DatabaseReference database;
    User user;
    private android.widget.TextView textmessage;
    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.textmessage = (TextView) findViewById(R.id.textmessage);
        this.bottomlauyout = (RelativeLayout) findViewById(R.id.bottom_lauyout);
        this.buttonenviar = (Button) findViewById(R.id.buttonenviar);
        this.editTextmensagem = (EditText) findViewById(R.id.editTextmensagem);


        if(getIntent().hasExtra("group")){
            group = (Group) getIntent().getSerializableExtra("group");
        }
        else
        if(getIntent().hasExtra("user")){
            user = (User) getIntent().getSerializableExtra("user");
        }else{
            finish();
            return;
        }

        database = FirebaseDatabase.getInstance().getReference();
//        database.child("groups").setValue(user);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token","");


        //Chat chat = new Chat();
        //chat.getUsers().add(user.getId());
        //chat.getUsers().add(token);


        if(user != null){
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            Query querychat = mFirebaseDatabaseReference.child("group")
                    .orderByChild(token)
                    .equalTo(true);

            querychat.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data :dataSnapshot.getChildren()) {
                        try {
                            JSONObject jsonObject = new JSONObject(data.getValue().toString());
                            if(jsonObject.optBoolean(user.getId()) && jsonObject.optBoolean("single")){
                                id=data.getKey();
                            }
                        }catch (Exception ex){

                        }
                    }
                    if(id == null || id.length() == 0){
                        id = UUID.randomUUID().toString();
                        Map<String,Object> chat = new HashMap<>();
                        chat.put("single",true);
                        chat.put(token,true);
                        chat.put(user.getId(),true);
                        database.child("group").child(id).setValue(chat);
                    }
                    messages();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            id = group.getId();
            messages();
        }

        this.buttonenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.setDate(new Date().getTime());
                message.setFrom(token);
                message.setText(editTextmensagem.getText().toString());

                String idmessage = UUID.randomUUID().toString();
                database.child("chat").child(id).child("menssages").child(idmessage).setValue(message);
            }
        });

    }
    String id;
    String token;

    public void messages(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("chat").child(id).child("menssages");
        // .orderByChild("from").equalTo(token);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textmessage.setText("");
                for (DataSnapshot data :dataSnapshot.getChildren()) {
                    Log.d("mFirebase", "onDataChange: "+ data.getValue().toString());
//                    User user = new Gson().fromJson(data.getValue().toString(),User.class);
//                    user.setId(data.getKey());
//                    objects.add(user);
                    textmessage.setText(textmessage.getText() + data.getValue().toString() +"\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("mFirebase", "onDataChange: "+ databaseError.getMessage());
            }
        });
    }
}
