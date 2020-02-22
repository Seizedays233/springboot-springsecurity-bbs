package com.seizedays.ideasharingprovider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seizedays.beans.Diary;
import com.seizedays.ideasharingprovider.mappers.DiaryMapper;
import com.seizedays.services.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //将当前Services变成spring的bean
@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    DiaryMapper diaryMapper;

    @Override
    public Diary selectDiary(String date, Long uid) {
        Diary selectedDiary = diaryMapper.selectDiary(date,uid);
        return selectedDiary;
    }

    @Override
    public Integer addDiary(Diary diary) {
        Integer addNum = diaryMapper.addDiary(diary);
        System.out.println("新增了" + addNum + "条数据");
        return addNum;
    }

    @Override
    public Integer deleteDiary(String date, Long uid) {
        Integer deleteNum = diaryMapper.deleteDiary(date,uid);
        System.out.println("删除了 " + deleteNum +"条数据");
        return deleteNum;
    }

    @Override
    public Integer updateDiary(Diary diary) {
        Integer updateNum = diaryMapper.updateDiary(diary);
        System.out.println("更新了 "+ updateNum + " 条数据");
        return updateNum;
    }

    @Override
    public Integer saveImage(Diary diary) {
        Diary select = selectDiary(diary.getDate(),diary.getUid());
        if(select != null) {
            System.out.println("当天存在数据");
            return diaryMapper.saveImage(diary);
        }else {
            System.out.println("当天无数据");
            return diaryMapper.addDiary(diary);
        }
    }
}
