package com.example.fourthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ChatBox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        // let's set adopter inti viewpager----------
        ViewPager2 views=findViewById(R.id.viewpager1);
        chatFragmentAdapter adapter=new chatFragmentAdapter(getSupportFragmentManager(),getLifecycle());
        views.setAdapter(adapter);

        // let's position of tabs according to the position of viewpager

        TabLayout tabs=findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                views.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // changes  position of tabs on change of viewpager pages
        views.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                tabs.getTabAt(position);//.select();
            }
        });
    }
    protected void onStaart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Toast.makeText(this, "Please Login first", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, Login.class);
            startActivity(in);
        }
    }
}