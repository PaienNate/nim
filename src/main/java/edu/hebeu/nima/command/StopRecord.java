package edu.hebeu.nima.command;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.GroupUser;
import edu.hebeu.nima.Const;

import java.util.ArrayList;

public class StopRecord implements GroupCommand // 实现EverywhereCommand就是无论私聊群聊还是讨论组都能收到的指令
{
    // 指令属性
    @Override
    public CommandProperties properties()
    {
        // 这个括号里填指令名和其他名称, 指令名必须至少有一个
        // 这个的话, 用"!v", "!version", 和"!版本"都能触发指令 (感叹号为你设置的前缀, 不一定必须要感叹号)
        return new CommandProperties("停止");
    }

    @Override
    public String groupMessage(EventGroupMessage eventGroupMessage, GroupUser groupUser, Group group, String s, ArrayList<String> arrayList) {
        if(!groupUser.isAdmin())
            return "只有管理员用户才能停止！";
        //判断是否标注记录的故事
        Const.isrecord = false;
        String temp = Const.recordname;
        Const.recordname = "";
        Const.groupid = 0L;
        return temp + "的故事记录已经停止。";
    }
}