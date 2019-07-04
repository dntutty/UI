package com.dntutty.ui.customview.minerecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dntutty.ui.R;

public class MineRecyclerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_recycler);
        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setAdapter(new RecyclerView.Adapter() {
            @Override
            public View onCreateViewHolder( View convertView, ViewGroup parent,int position) {
                convertView = MineRecyclerActivity.this.getLayoutInflater().inflate(R.layout.item_table,parent,false);
                TextView textView = convertView.findViewById(R.id.text);
                textView.setText("第"+position+"行");
                return convertView;

            }

            @Override
            public View onBindViewHolder(View convertView, ViewGroup parent, int position) {
                TextView textView = convertView.findViewById(R.id.text);
                textView.setText("网易课堂"+position);
                return convertView;
            }

            @Override
            public int getItemViewType(int row) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public int getHeight(int index) {
                return 200;
            }
        });
    }
}
