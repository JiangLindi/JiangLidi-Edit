package com.example.edit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class button2Activity extends MainActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private EditText row;
    private EditText column;
    private Button bt1;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button2);
        //获取控件Button
        bt1 = findViewById(R.id.button11);
        //获取文本输入框控件
        row = findViewById(R.id.editText1);
        column = findViewById(R.id.editText2);

        //给button按钮绑定单击事件
        bt1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(row.getText().length()>0&&column.getText().length()>0){
                    //把输入的行和列转为整形
                    int row_int=Integer.parseInt(row.getText().toString());
                    int col_int=Integer.parseInt(column.getText().toString());


                    //获取控件tableLayout
                    tableLayout = (TableLayout)findViewById(R.id.table1);
                    //清除表格所有行
                    tableLayout.removeAllViews();
                    //全部列自动填充空白处
                    tableLayout.setStretchAllColumns(true);
                    //生成X行，Y列的表格
                    for(int i=1;i<=row_int;i++)
                    {
                        TableRow tableRow=new TableRow(button2Activity.this);
                        for(int j=1;j<=col_int;j++)
                        {
                            //tv用于显示
                            TextView tv=new TextView(button2Activity.this);
                            //Button bt=new Button(MainActivity.this);
                            tv.setText("("+i+","+j+")");

                            tableRow.addView(tv);
                        }
                        //新建的TableRow添加到TableLayout

                        tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC,1));

                    }
                }else{
                    Toast.makeText(button2Activity.this,"请输入数值",1).show();
                }
            }
        });


    }

}

