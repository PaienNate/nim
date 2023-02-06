package edu.hebeu.nima.event;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import edu.hebeu.nima.Const;
import edu.hebeu.nima.config.SpringContextUtil;
import edu.hebeu.nima.mapper.MessageIndexMapper;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import edu.hebeu.nima.pojo.MessagePojo;
import edu.hebeu.nima.pojo.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckStory extends IcqListener // 必须继承 IcqListener 监听器类
{
    @Autowired
    public MessageInfoMapper messageInfoMapper;

    @EventHandler // 这个注解必须加, 用于反射时判断哪些方法是事件方法的, 不用 @Override
    public void getIfNeedStorage(EventGroupMessage event) // 想监听什么事件就写在事件类名这里, 一个方法只能有一个事件参数
    {
        //需要同时满足下面两个
        if(Const.isrecord && event.getGroup().getId() == Const.groupid)
        {
            try {
                //获取：发言者，发言时间，发言内容
                MessagePojo MessagePojo = new MessagePojo();
                MessagePojo.setMessage(event.getMessage());
                MessagePojo.setQq(event.getGroupSender().getId());
                MessagePojo.setTime(new Date(event.getTime() * 1000L));
                MessagePojo.setStoryname(Const.recordname);
                String message = event.getMessage();
                MessageInfoMapper mapper = (MessageInfoMapper)SpringContextUtil.getBean();
                mapper.insert(MessagePojo);
                //此时UUID会自动填写，我们只需要再次进行一次插入index即可
                Story story = new Story();
                story.setStatus("0");
                story.setMessageid(MessagePojo.getUuid());
                MessageIndexMapper messageIndexMapper = (MessageIndexMapper)SpringContextUtil.getIndexBean();
                messageIndexMapper.insert(story);
                System.out.println(message);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}