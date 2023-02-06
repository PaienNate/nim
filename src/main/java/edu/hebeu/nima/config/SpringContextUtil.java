package edu.hebeu.nima.config;


import edu.hebeu.nima.mapper.MessageIndexMapper;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import edu.hebeu.nima.service.BackupService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        SpringContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean() throws BeansException{
        return applicationContext.getBean(MessageInfoMapper.class);
    }

    public static Object getBackupBean() throws BeansException{
        return applicationContext.getBean(BackupService.class);
    }

    public static Object getIndexBean() throws BeansException{
        return applicationContext.getBean(MessageIndexMapper.class);
    }
}