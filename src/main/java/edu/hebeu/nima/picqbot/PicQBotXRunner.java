package edu.hebeu.nima.picqbot;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import edu.hebeu.nima.command.GetAllStoryName;
import edu.hebeu.nima.command.Record;
import edu.hebeu.nima.command.StopRecord;
import edu.hebeu.nima.event.CheckStory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PicQBotXRunner implements CommandLineRunner {
    private final static Logger logger= LoggerFactory.getLogger(PicQBotXRunner.class);
    private final static boolean start = false;

    @Override
    public void run(String... args) throws Exception {
        logger.info("CommandLineRunner 启动任务 - PicqBotX开始运行");
        if(start)
        {
        // 创建机器人对象 ( 传入配置 )
        PicqBotX bot = new PicqBotX(new PicqConfig(5901).setDebug(true).setLogPath(""));
        // 添加一个机器人账户 ( 名字, 发送URL, 发送端口 )
        bot.addAccount("Bot01", "127.0.0.1", 5900);
        // 启用指令管理器
        // 这些字符串是指令前缀, 比如指令"!help"的前缀就是"!"
        bot.enableCommandManager( "!","！");
        bot.getCommandManager().registerCommands(new Record(),new StopRecord(),new GetAllStoryName());
        bot.getEventManager().registerListeners(new CheckStory());
        // 启动机器人, 不会占用主线程
        bot.startBot();
        }
    }
}


