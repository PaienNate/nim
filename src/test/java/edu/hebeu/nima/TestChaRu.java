package edu.hebeu.nima;

import edu.hebeu.nima.config.SpringContextUtil;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import edu.hebeu.nima.pojo.MessagePojo;
import edu.hebeu.nima.service.BackupService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static edu.hebeu.nima.config.SpringContextUtil.getSqlSessionFacBean;

@SpringBootTest
public class TestChaRu {

    @Test
    public void test() throws IOException, SQLException {
        BackupService mapper = (BackupService) SpringContextUtil.getBackupBean();
        mapper.vacuum();
        mapper.backup("merge");
    }

}
