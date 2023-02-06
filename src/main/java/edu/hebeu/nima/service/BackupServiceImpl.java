package edu.hebeu.nima.service;

import cn.hutool.core.date.DateUtil;
import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineImpl;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class BackupServiceImpl implements BackupService{
    @Resource
    public MessageInfoMapper messageInfoMapper;
    @Override
    public String backup(String param) throws IOException {
        //https://dav.jianguoyun.com/dav/
        Sardine sardine = new SardineImpl("aouangoii@qq.com", "ajdscvisuga9s9f2");
        byte[] data = FileUtils.readFileToByteArray(new File("cloud.db"));
        sardine.put("https://dav.jianguoyun.com/dav/backupfile/" + param + "/" + DateUtil.format(new Date(),"yyyy年MM月dd日_HH时mm分ss秒") + ".db", data);
        return "success";
    }

    @Override
    public String backup() throws IOException {
        return backup("auto");
    }


    @Override
    public void vacuum()
    {
        messageInfoMapper.vacuum();
    }

}
