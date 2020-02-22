package com.seizedays.ideasharingprovider.mappers;

import com.seizedays.beans.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DiaryMapper {
    Diary selectDiary(String date, Long uid);
    Integer addDiary(Diary diary);
    Integer updateDiary(Diary diary);
    Integer deleteDiary(String date, Long uid);
    Integer saveImage(Diary diary);
}
