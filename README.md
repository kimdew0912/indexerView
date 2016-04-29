# indexerView


# ListView, RecyclerView 등등의 출력 view계열에서 빠른 데이터 검색을 위한 indexerView
### 1. 기능
  1. 최상단으로 이동할 수 있는 indexer 표출(useTopIndicator)
  2. 숫자, 알파벳등 ascii 코드는 하나의 indexer 로 표출(useMergeAscii)
  3. 한글 - 초성, 완성형 indexer 설정(자동 파싱)
  4. indexer Text Color 설정(setIndexTextColor)
  5. indexer 선택 시 콜백 기능(setOnClickIndexBar)
 
****

### 2. 사용 예
```
 [layout]
  <com.kimdew.indexerview.IndexerView
        android:id="@+id/indexer"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignBottom="@id/lvSample"
        android:layout_alignParentRight="true"
        android:layout_alignTop="@id/lvSample"
        android:paddingRight="8dp" />
```

```
 [source]
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
          // ListView 위치 변경
          lvSample.setSelection(nFirstIndex);
          
          // 선택된 indexer Text Show
          showIndexToast(strText);
      }
  });
```

### 3. 저작권


### 4. 사용한 Open Library
  1. 초성 필터 : https://github.com/bangjunyoung/KoreanTextMatcher
