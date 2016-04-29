package com.kimdew.indexerview;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogua.localization.KoreanChar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

/*
 * Copyright 2016 SHKIM
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class IndexerView<T> extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    public interface onClickIndexBar {
        void onClickIndex(String strText, int nFirstIndex);
    }

    /**
     * index 출력 방법 : 완성형
     */
    public final static int INDEX_TYPE_COMPLETE_WORD = 1;
    /**
     * index 출력 방법 : 초성
     */
    public final static int INDEX_TYPE_CHOSUNG = 2;

    /**
     * (기본값) 최상위로 이동 indicator
     */
    public final static String DEFAULT_TOP_INDICATOR = "@";
    public final static int INDEX_TEXT_COLOR = 0xff9d9d9d;

    ArrayList<T> m_arrTarget;
    TreeMap<String, IndexData> m_mapFiltered;
    onClickIndexBar m_onClickBar;

    String m_strTopIndicator = DEFAULT_TOP_INDICATOR;
    boolean m_bExistTopIndicator = true;
    boolean m_bMergeAscii = true;
    int m_nIndexType = INDEX_TYPE_COMPLETE_WORD;

    int m_nIndexTextColor = INDEX_TEXT_COLOR;

    public IndexerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        setOnClickListener(this);
        initContainView();

        m_arrTarget = new ArrayList<>();

        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {

                return lhs.compareTo(rhs);
            }
        };

        m_mapFiltered = new TreeMap<String, IndexData>(comparator);
    }

    public IndexerView(Context context) {
        this(context, null);
    }

    void initContainView(){
        setOnTouchListener(this);
    }

    public void setOnClickIndexBar(onClickIndexBar listener) {
        m_onClickBar = listener;
    }

    /**
     *
     * @param arrData
     */
    public void setData(ArrayList<T> arrData) {
        if( arrData == null || arrData.size() == 0 ) {
            return ;
        }

        if( getChildCount() > 0 ) {
            removeAllViews();
        }

        m_arrTarget.clear();
        m_mapFiltered.clear();

        m_arrTarget.ensureCapacity(arrData.size());
        m_arrTarget.addAll(arrData);

        // Index 구성
        performFilter();

        // top button...
        if( m_bExistTopIndicator && (m_mapFiltered.containsKey(m_strTopIndicator) == false) ) {
            IndexData datas = new IndexData();
            datas.strChosung = m_strTopIndicator;
            datas.strChosungForView = m_strTopIndicator;
            datas.nFirstIndex = 0;
            datas.nTotalCount = 1;

            addChosungView(datas);
        }

        Iterator<String> iter = m_mapFiltered.keySet().iterator();
        while ( iter.hasNext() ) {
            addChosungView(m_mapFiltered.get(iter.next()));
        }
    }

    private void addChosungView(IndexData datas) {
        TextView tvChosung = new TextView(getContext());
        LayoutParams ll = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        ll.weight = 1;
        tvChosung.setLayoutParams(ll);
        tvChosung.setText(datas.strChosungForView);
        tvChosung.setGravity(Gravity.CENTER);
        tvChosung.setTextColor(m_nIndexTextColor);
        tvChosung.setDuplicateParentStateEnabled(true);
        tvChosung.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tvChosung.setIncludeFontPadding(false);
        tvChosung.setTag(datas);
        // row click 처리 하지 않고, parent 의 touch 를 잡아서 child onClick 강제 발생
        // index bar 를 drag 시, child 선택을 동적으로 변경해야 하기 때문.
//        tvChosung.setOnClickListener(this);

        addView(tvChosung);
    }

    @Override
    public void onClick(View v) {
        if( v == this ) {
            return ;
        }

        IndexData tag = (IndexData)v.getTag();
        if( m_onClickBar != null ) {
            m_onClickBar.onClickIndex(tag.strChosung, tag.nFirstIndex);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // drag 시, Child 선택된 것처럼 처리
        float fTouchY = event.getRawY();
        int nChildCnt = getChildCount();
        for (int i = 0; i < nChildCnt; i++) {
            View vChild = getChildAt(i);

            Rect rtChild = new Rect();
            vChild.getGlobalVisibleRect(rtChild);

            // touch 영역이 속한 child view를 찾아서 click 처리
            if ((int) fTouchY >= rtChild.top && (int) fTouchY < rtChild.bottom) {
                onClick(vChild);
                break;
            }
        }
        return false;
    }

    /**
     * 데이터들의 첫글자(or 초성)를 뽑아냄
     */
    public void performFilter() {
        int nCount = m_arrTarget.size();
        for( int i = 0 ; i < nCount ; i++ ){
            confirmData(m_arrTarget.get(i), i);
        }
    }

    private void confirmData(T data, int nTargetIndex) {
        if( data == null ) {
            return;
        }
        String strWord = data.toString();
        if( TextUtils.isEmpty(strWord) ) {
            return;
        }

        String strFirstChosung;
        if( m_nIndexType == INDEX_TYPE_COMPLETE_WORD ) {
            strFirstChosung = String.format("%s", strWord.charAt(0));
        }
        else {
//            if( KoreanChar.isCompatChoseong(strWord.charAt(0)) == false ) {
//                return ;
//            }

            char chChosung = KoreanChar.getCompatChoseong(strWord.charAt(0));
            if( chChosung != '\0' ) {
                strFirstChosung = String.format("%s", chChosung);
            }
            else {
                strFirstChosung = String.format("%s", strWord.charAt(0));
            }
        }

        String strChosungForView = strFirstChosung;
        if( TextUtils.isEmpty(strFirstChosung) == false ) {

            // ascii 를 한 문자로 모을지 여부..
            if( m_bMergeAscii ) {
                char ch = strFirstChosung.charAt(0);
                if (ch >= 0 && ch <= 127) {
//                    strFirstChosung = "힝" + "#";
                    strFirstChosung = "#";
                    strChosungForView = "#";
                }
            }
        }
        else {
            return;
        }

        IndexData datas = m_mapFiltered.get(strFirstChosung);
        if( datas == null ) {
            datas = new IndexData();

            datas.strChosung = strFirstChosung;
            datas.strChosungForView = strChosungForView;
            datas.nFirstIndex = nTargetIndex;
            datas.nTotalCount = 1;

            m_mapFiltered.put(strFirstChosung, datas);
        }
        else {
            // 초성의 첫번째 index 를 체크(지금 데이터가 더 빠른 index 이면 바꿈)
            if( datas.nFirstIndex > nTargetIndex ) {
                datas.nFirstIndex = nTargetIndex;
            }
            datas.nTotalCount++;
        }
    }

    public void useTopIndicator(boolean bUse) {
        m_bExistTopIndicator = bUse;
    }

    /**
     * ascii code 0~127 -> #으로 묶는 옵션 on/off
     * @param bUse - true : on, false : off
     */
    public void useMergeAscii(boolean bUse) {
        m_bMergeAscii = bUse;
    }

    public int getChosungIndexCount() {
        return m_mapFiltered == null ? 0 : m_mapFiltered.size();
    }

    public void setIndexTextColor(int nTextColor) {
        m_nIndexTextColor = nTextColor;
    }

    /**
     * 리스트를 최상단으로 이동시켜주는 index 문자
     * @param chIndicator - 하나의 문자(default : @)
     */
    public void setTopIndicator(char chIndicator) {
        m_strTopIndicator = String.format("%s", chIndicator);
    }

    /**
     * INDEX_TYPE_COMPLETE_WORD<br/>
     * INDEX_TYPE_CHOSUNG<br/>
     * @param nType
     */
    public void setIndexType(int nType) {
        m_nIndexType = nType;
    }

    class IndexData {
        /** ㄱ, ㄴ, ㄷ, ㄹ... a, A... */
        String strChosung;
        /** a, A -> # */
        String strChosungForView;
        /** strChosung 이 위치한 첫번째 index */
        int nFirstIndex;
        /** strChosung 을 가진 데이터의 총 개수*/
        int nTotalCount;
    }
}