package edu.hebeu.nima;

import edu.hebeu.nima.config.SpringContextUtil;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import edu.hebeu.nima.pojo.MessagePojo;
import edu.hebeu.nima.service.BackupService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
public class TestChaRu {

    @Test
    public void test() throws IOException {

        BackupService mapper = (BackupService) SpringContextUtil.getBackupBean();
        mapper.vacuum();
        mapper.backup();


    }

}
