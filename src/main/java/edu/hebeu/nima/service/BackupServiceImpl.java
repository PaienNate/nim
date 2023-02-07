package edu.hebeu.nima.service;

import cn.hutool.core.date.DateUtil;
import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineImpl;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sqlite.ExtendedCommand;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static edu.hebeu.nima.config.SpringContextUtil.getSqlSessionFacBean;

@Service
public class BackupServiceImpl implements BackupService{
    @Resource
    public MessageInfoMapper messageInfoMapper;

    @Override
    @Transactional
    public String backup(String param) throws IOException, SQLException {
        //https://dav.jianguoyun.com/dav/
        Sardine sardine = new SardineImpl("aouangoii@qq.com", "ajdscvisuga9s9f2");
        File tmpFile = File.createTempFile("backup-test", ".sqlite");
        tmpFile.deleteOnExit();
        String where = tmpFile.getAbsolutePath();
        //SQLITE有自己的备份函数，不需要这么折腾。
        //但是mybatis不能直接执行备份函数，具体原因我也不太懂。
        //所以只能用取出sqlsession然后创建connection来备份了
        SqlSessionFactory factoryBean = getSqlSessionFacBean();
        SqlSession session = factoryBean.openSession();
        Connection conn = session.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("backup to " + tmpFile);
        byte[] data = FileUtils.readFileToByteArray(tmpFile);
        String test = "https://dav.jianguoyun.com/dav/backupfile/" + param + "/" + DateUtil.format(new Date(),"yyyy年MM月dd日_HH时mm分ss秒") + ".db";
        sardine.put(test, data);
        return "success";
    }

    @Override
    public String backup() throws IOException, SQLException {
        return backup("auto");
    }


    @Override
    public void vacuum()
    {
        messageInfoMapper.vacuum();
    }

}
