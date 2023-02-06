package edu.hebeu.nima.command;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.GroupUser;
import edu.hebeu.nima.Const;

import java.util.ArrayList;

public class Record implements GroupCommand // 实现EverywhereCommand就是无论私聊群聊还是讨论组都能收到的指令
{
    // 指令属性
    @Override
    public CommandProperties properties()
    {
        // 这个括号里填指令名和其他名称, 指令名必须至少有一个
        // 这个的话, 用"!v", "!version", 和"!版本"都能触发指令 (感叹号为你设置的前缀, 不一定必须要感叹号)
        return new CommandProperties("记录");
    }

    @Override
    public String groupMessage(EventGroupMessage eventGroupMessage, GroupUser groupUser, Group group, String s, ArrayList<String> arrayList) {
        if(!groupUser.isAdmin())
        {
            return "只有管理员才能记录或停止！";
        }

        //判断是否标注记录的故事
        if(arrayList.size()==0)
            return "您没有设置故事。请用：!记录 故事名称 来设定故事。";
        if((!"".equals(Const.recordname) || Const.isrecord))
        {
            return "当前已经有一个故事在运行，故事名为" + Const.recordname;
        }
        //否则开始记录
        Const.isrecord = true;
        Const.recordname = arrayList.get(0);
        Const.groupid = group.getId();
        return "开始记录啦";
    }
}