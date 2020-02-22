package com.seizedays.services;

import com.seizedays.beans.Diary;


public interface DiaryService {
    Diary selectDiary(String date, Long uid);
    Integer addDiary(Diary diary);
    Integer deleteDiary(String date, Long uid);
    Integer updateDiary(Diary diary);
    Integer saveImage(Diary diary);

}
