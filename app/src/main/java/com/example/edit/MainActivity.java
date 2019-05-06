package com.example.edit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private Button Button1;
private Button Button2;
private Button Button3;
private Button Button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button1=findViewById(R.id.button1);
        Button2=findViewById(R.id.button2);
        Button3=findViewById(R.id.button3);
        Button4=findViewById(R.id.button4);
        Button1.setOnClickListener(new Button1Listener());
        Button2.setOnClickListener(new Button2Listener());
        Button3.setOnClickListener(new Button3Listener());
        Button4.setOnClickListener(new Button4Listener());

    }
    class Button1Listener implements View.OnClickListener{
        @Override
        public void  onClick(View arg0){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,button1Activity.class);
            MainActivity.this.startActivity(intent);

        }
    }
    class Button2Listener implements View.OnClickListener{
        @Override
        public void  onClick(View arg0){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,button2Activity.class);
            MainActivity.this.startActivity(intent);

        }
    }
    class Button3Listener implements View.OnClickListener{
        @Override
        public void  onClick(View arg0){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,button3Activity.class);
            MainActivity.this.startActivity(intent);

        }
    }
    class Button4Listener implements View.OnClickListener{
        @Override
        public void  onClick(View arg0){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,button4Activity.class);
            MainActivity.this.startActivity(intent);

        }
    }
}
