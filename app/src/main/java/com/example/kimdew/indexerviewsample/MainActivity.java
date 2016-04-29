package com.example.kimdew.indexerviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kimdew.indexerview.IndexerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public final static int DATA_COUNT = 100;

    private final static char[] rands;
    static {
        StringBuilder sb = new StringBuilder();
        for( char c = 'a' ; c <= 'z' ; c++ ) {
            sb.append(c);
        }
        for( char c = '0' ; c <= '9' ; c++ ) {
            sb.append(c);
        }

        char[] hangul = new char[]{
                '가', '나', '다', '라', '마'
                , '바', '사', '아', '자', '차'
                , '카', '타', '파', '하'};
        for( int i = 0 ; i < hangul.length ; i++ ) {
            sb.append(hangul[i]);
        }

        rands = sb.toString().toCharArray();
    }

    private TextView m_tvIndexToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arrData = generateData();

        Collections.sort(arrData);

        ArrayAdapter adt = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                arrData.toArray(new String[]{""}));

        m_tvIndexToast = (TextView) findViewById(R.id.tvToast);
        m_tvIndexToast.setVisibility(View.GONE);

        // listview
        final ListView lvSample = (ListView) findViewById(R.id.lvSample);
        lvSample.setAdapter(adt);

        // indexer view
        IndexerView indexer = (IndexerView) findViewById(R.id.indexer);
        indexer.setIndexType(IndexerView.INDEX_TYPE_CHOSUNG);   // or INDEX_TYPE_COMPLETE_WORD(default)
        indexer.setIndexTextColor(Color.RED);   // default : 0xff9d9d9d
        indexer.setTopIndicator('^');           // default : @
        indexer.useTopIndicator(true);          // default : true
        indexer.useMergeAscii(true);            // default : true
        indexer.setData(arrData);
        indexer.setOnClickIndexBar(new IndexerView.onClickIndexBar() {
            @Override
            public void onClickIndex(String strText, int nFirstIndex) {
                lvSample.setSelection(nFirstIndex);

                showIndexToast(strText);
            }
        });
    }

    private ArrayList<String> generateData() {
        ArrayList<String> arrData = new ArrayList<>(DATA_COUNT);
        Random randLen = new Random();
        Random randStr = new Random();

        for( int i = 0 ; i < DATA_COUNT ; i++ ) {
            int nRandLen = randLen.nextInt(10) + 1;// max 10
            StringBuilder sb = new StringBuilder(nRandLen);
            for (int len = 0; len < nRandLen; len++) {
                sb.append(rands[randStr.nextInt(rands.length)]);
            }
            arrData.add(sb.toString());
        }

        return arrData;
    }

    void showIndexToast(String strIndex) {
        m_tvIndexToast.removeCallbacks(runHideToast);

        m_tvIndexToast.setText(strIndex);
        m_tvIndexToast.setVisibility(View.VISIBLE);

        m_tvIndexToast.postDelayed(runHideToast, 500);
    }

    final Runnable runHideToast = new Runnable() {
        @Override
        public void run() {
            m_tvIndexToast.setVisibility(View.GONE);
        }
    };
}
