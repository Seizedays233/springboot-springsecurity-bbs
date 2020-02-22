package com.seizedays.ideasharingprovider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seizedays.beans.Memo;
import com.seizedays.ideasharingprovider.mappers.MemoMapper;
import com.seizedays.services.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //将当前Services变成spring的bean
@Service
public class MemoServiceImpl implements MemoService {

    @Autowired
    private MemoMapper memoMapper;

    @Override
    public List<Memo> getMemo(String date, Long uid) {
        return memoMapper.selectMemo(date,uid);
    }

    @Override
    public Integer addMemo(Memo memo) {
        return memoMapper.insertMemo(memo);
    }

    @Override
    public Integer updateMemo(Memo memo) {
        return memoMapper.updateMemo(memo);
    }

    @Override
    public Integer deleteMemoById(Long mid, Long uid) {
        return memoMapper.deleteMemoById(mid, uid);
    }
}
