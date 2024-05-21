package com.example.fourthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourthapp.model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessengerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        Intent in=getIntent();
        String name=in.getStringExtra("name");
        Toast.makeText(this,name, Toast.LENGTH_SHORT).show();
         String receiveruid=in.getStringExtra("uid");
         String sendderuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
         ImageView profile=findViewById(R.id.profile_image);
        Picasso.get()
                .load(in.getStringExtra("pic"))
                   .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(profile);

        TextView txt_receivername=findViewById(R.id.txt_receivername);
        txt_receivername.setText(name);

        EditText et_mymessage=findViewById(R.id.et_mymessage);
        ImageView img_sendmassage=findViewById(R.id.img_sendmessage);

        //***btn send click event------------------
        img_sendmassage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mymessage=et_mymessage.getText().toString();
                if (!mymessage.isEmpty())
                {
                    HashMap<String,String> message=new HashMap<>();
                    message.put("msg",mymessage);
                    message.put("senderid",sendderuid);
                    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat time=new SimpleDateFormat("hh:mm:aa");
                    message.put("date",date.format(new Date()));
                    message.put("time", time.format(new Date()));
                    FirebaseDatabase.getInstance().getReference().child("message").child(sendderuid+receiveruid).push().setValue(message);
                    FirebaseDatabase.getInstance().getReference().child("message").child(receiveruid+sendderuid).push().setValue(message);
                    et_mymessage.setText("");
                }
            }
        });

        //****back button event----------------------------
        ImageView img_back1=findViewById(R.id.img_back1);
        img_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            MessengerActivity.super.onBackPressed();
            }
        });
        //*********select all message between sender+recevier and bind in Recyclerview----------------
        ArrayList<MessageModel> message=new ArrayList<>();
        RecyclerView recycler_msg=findViewById(R.id.recycler_msg);
        MessageListAdapter adapter=new MessageListAdapter(this,message,sendderuid+receiveruid);
        recycler_msg.setAdapter(adapter);
        recycler_msg.setLayoutManager(new LinearLayoutManager(this));


        FirebaseDatabase.getInstance().getReference().child("message").child(sendderuid+receiveruid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                message.clear();
             for (DataSnapshot data:snapshot.getChildren())
             {
                 MessageModel m=new MessageModel();
                 m.id=data.getKey();
                 m.message=data.child("msg").getValue(String.class);
                 m.senderid=data.child("senderid").getValue(String.class);
                 m.date=data.child("date").getValue(String.class);
                 m.time=data.child("time").getValue(String.class);
                 message.add(m);
             }
             adapter.notifyDataSetChanged();
             if (message.size()>3)
                 recycler_msg.scrollToPosition(message.size()-1);
             Toast.makeText(MessengerActivity.this,message.size()+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void logout(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Intent in=new Intent(this, Login.class);
        startActivity(in);
    }
}