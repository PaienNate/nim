package edu.hebeu.nima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hebeu.nima.pojo.MessagePojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MessageInfoMapper extends BaseMapper<MessagePojo> {
    @Update("vacuum")
    public void vacuum();

    @Update("backup main C:\\user.db")
    public void backup(@Param("absolutepath") String where);
}
