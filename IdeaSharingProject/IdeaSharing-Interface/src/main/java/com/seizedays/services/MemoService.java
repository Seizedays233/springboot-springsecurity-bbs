package com.seizedays.services;

import com.seizedays.beans.Memo;

import java.util.List;

public interface MemoService {
    //查询备忘录
    List<Memo> getMemo(String date, Long uid);

    //增加备忘录
    Integer addMemo(Memo memo);

    //更新备忘录
    Integer updateMemo(Memo memo);

    //删除备忘
    Integer deleteMemoById(Long mid, Long uid);
}
