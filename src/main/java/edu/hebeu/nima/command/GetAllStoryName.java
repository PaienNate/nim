package edu.hebeu.nima.command;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.GroupUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hebeu.nima.config.SpringContextUtil;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import edu.hebeu.nima.pojo.MessagePojo;

import java.util.ArrayList;
import java.util.List;

public class GetAllStoryName implements GroupCommand {
    @Override
    public String groupMessage(EventGroupMessage eventGroupMessage, GroupUser groupUser, Group group, String s, ArrayList<String> arrayList) {
        //获取mybatis对象
        MessageInfoMapper mapper = (MessageInfoMapper) SpringContextUtil.getBean();
        QueryWrapper<MessagePojo> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT storyname");
        List<MessagePojo> messagePojoList = mapper.selectList(wrapper);
        MessageBuilder builder =  new MessageBuilder();
        for(MessagePojo MessagePojo : messagePojoList)
        {
            builder.add(MessagePojo.getStoryname()).newLine();
        }
        return builder.toString();
    }

    @Override
    public CommandProperties properties() {
        return new CommandProperties("故事列表");
    }
}
