package edu.hebeu.nima;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cn.dev33.satoken.quick.SaQuickManager;
import edu.hebeu.nima.command.Record;
import edu.hebeu.nima.command.StopRecord;
import edu.hebeu.nima.event.CheckStory;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.beans.ExceptionListener;

@SpringBootApplication
public class NimaApplication {

    private final static Logger logger= LoggerFactory.getLogger(NimaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NimaApplication.class, args);
        logger.info("\n------ 启动成功 ------");
//        logger.info("name: " + SaQuickManager.getConfig().getName());
//        logger.info("pwd:  " + SaQuickManager.getConfig().getPwd());
    }






}
