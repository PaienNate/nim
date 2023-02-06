package edu.hebeu.nima.command;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.GroupUser;
import edu.hebeu.nima.config.SpringContextUtil;
import edu.hebeu.nima.service.BackupService;

import java.util.ArrayList;

public class BackupMyFile implements GroupCommand {
    @Override
    public String groupMessage(EventGroupMessage eventGroupMessage, GroupUser groupUser, Group group, String s, ArrayList<String> arrayList) {
        try{
            BackupService service = (BackupService) SpringContextUtil.getBackupBean();
            service.vacuum();
            service.backup();
        }
        catch (Exception e)
        {
            return "我敲，炸了：" + e.getMessage();
        }
        return "备份已完成";
    }

    @Override
    public CommandProperties properties() {
        return new CommandProperties("备份");
    }
}
