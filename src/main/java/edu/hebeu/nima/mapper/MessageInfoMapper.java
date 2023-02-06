package edu.hebeu.nima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hebeu.nima.pojo.MessagePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MessageInfoMapper extends BaseMapper<MessagePojo> {
    @Update("vacuum")
    public void vacuum();
}
