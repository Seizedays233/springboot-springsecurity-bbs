package com.seizedays.ideasharingprovider.mappers;

import com.seizedays.beans.Memo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MemoMapper {
    List<Memo> selectMemo(String date, Long uid);
    int updateMemo(Memo memo);
    int deleteMemoById(Long id, Long uid);
    int insertMemo(Memo memo);
}
