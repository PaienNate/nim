package edu.hebeu.nima.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.poi.word.Word07Writer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hebeu.nima.Const;
import edu.hebeu.nima.pojo.DownloadBean;
import edu.hebeu.nima.pojo.MessagePojo;
import edu.hebeu.nima.service.BackupService;
import edu.hebeu.nima.service.MessageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Controller
//@SaCheckLogin
public class TextController {
    @Autowired
    public MessageInfoService messageInfoService;
    @Autowired
    public BackupService backupService;
    //考虑到Linux没宋体……
    public static String fontname = "文泉驿正黑";
    @SaIgnore
    @GetMapping("/")
    public String login()
    {
        return "login";
    }
    @SaIgnore
    @GetMapping("/login")
    public ModelAndView getLogin()
    {
        return new ModelAndView("login");
    }

    @GetMapping("/index")
    public ModelAndView getindex()
    {

        return new ModelAndView("index");
    }


    @SaIgnore
    @RequestMapping("/login/dologin")
    public String doLogin(@RequestParam String email, @RequestParam String password)
    {
        String name = "860034825";
        String name2 = "624095558";
        if(name.equals(email)&&name.equals(password))
        {
            StpUtil.login(name);
            return "redirect:/index";
        }
        if(name2.equals(email)&&name2.equals(password))
        {
            StpUtil.login(name2);
            return "redirect:/index";
        }
        return "redirect:/login";
    }


    @ResponseBody
    @PostMapping("/download")
    public void getTable(@RequestBody DownloadBean downloadBean, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<MessagePojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessagePojo::getStoryname,downloadBean.getStoryname());
        if(downloadBean.getQqs()!=null && downloadBean.getQqs().size()!=0)
        {
        wrapper.and(tableLambdaQueryWrapper -> {
            for(Long qq_num:downloadBean.getQqs())
            {
                tableLambdaQueryWrapper.eq(MessagePojo::getQq,qq_num);
                if(!qq_num.equals(downloadBean.getQqs().get(downloadBean.getQqs().size() - 1)))
                {
                    tableLambdaQueryWrapper.or();
                }
            }});
        }
        //升序排列
        wrapper.orderByAsc(MessagePojo::getTime);
        // path是指想要下载的文件的路径
        Word07Writer writer = new Word07Writer();
        List<MessagePojo> messagePojoList = messageInfoService.list(wrapper);
        for(MessagePojo MessagePojo : messagePojoList)
        {
            // 添加段落（标题）
            writer.addText(new Font(fontname, Font.BOLD, 12), DateUtil.formatDateTime(MessagePojo.getTime()));
            // 添加段落（正文）
            if(MessagePojo.getMessage().contains("CQ:image"))
            {
                writer.addText(new Font(fontname, Font.PLAIN, 16), String.valueOf(MessagePojo.getQq()),"发送了一张图片。");
            }
            else
            {
                writer.addText(new Font(fontname, Font.PLAIN, 10), String.valueOf(MessagePojo.getQq()));
                writer.addText(new Font(fontname, Font.PLAIN, 12), "说:");
                writer.addText(new Font(fontname, Font.PLAIN, 16), MessagePojo.getMessage());
            }
       }
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setContentType("application/x-download;charset=utf-8");
        String pinyin = PinyinUtil.getPinyin(downloadBean.getStoryname(), "_");
        String today= DateUtil.today();
        response.setHeader("Content-Disposition", "attachment;filename="+pinyin+"_"+today+"_download.docx");
        // 写出到流里
        writer.flush(os);
        // 关闭
        writer.close();
        response.getOutputStream().flush();
    }

    //获取名称合集
    @ResponseBody
    @GetMapping("/getStoryNames")
    public List<String> getStoryNames() {
        QueryWrapper<MessagePojo> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT storyname");
        List<MessagePojo> messagePojoList = messageInfoService.list(wrapper);
        List<String> stringList = new ArrayList<>();
        for(MessagePojo MessagePojo : messagePojoList)
        {
            if(MessagePojo==null)
                continue;
            stringList.add(MessagePojo.getStoryname());
        }
        return stringList;
    }
    @ResponseBody
    @GetMapping("/getCommitsByStoryName")
    public List<MessagePojo> getCommits(@RequestParam String storyname)
    {
        LambdaQueryWrapper<MessagePojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessagePojo::getStoryname,storyname);
        wrapper.orderByAsc(MessagePojo::getTime);
        return messageInfoService.list(wrapper);
    }

    @ResponseBody
    @GetMapping("/deletebyStory")
    @SaIgnore
    public String DeleteByStoryNameList(@RequestParam List<String> storylist) throws IOException, SQLException {
        //如果现在正在记录，不能删除，因为没办法备份
        if(Const.isrecord)
        {
            return "删除失败，正在记录，请先关闭";
        }
        //数据库的备份语句
        backupService.vacuum();
        //这个是上传到webdav的语句，delete代表上传到delete文件夹里
        backupService.backup("delete");

        List<String> idlist = new ArrayList<>();
        //通过storylist里的每一个去寻找所有的条目对应的主键ID
        for(String story:storylist)
        {
            LambdaQueryWrapper<MessagePojo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MessagePojo::getStoryname,story);
            List<MessagePojo> deletelist = messageInfoService.list();
            for(MessagePojo pojo:deletelist)
            {
                idlist.add(pojo.getUuid());
            }
        }
        //使用Mybatis-plus的方法移除他们
       if(messageInfoService.removeByIds(idlist))
       {
           //成功返回OK
           return "OK";
       }
        return "NO";
    }

    @ResponseBody
    @GetMapping("/MergeShuju")
    @SaIgnore
    //合并故事集（选择两个或者多个故事集，传入故事集的新名称，从而将这几个故事集里的条目全部修改为对应的名称）
    public String MergeShuju(@RequestParam List<String> storylist, @RequestParam String newname) throws IOException, SQLException {
        //如果判断还在记录，此时不能合并，因为数据库还在更新
        if(Const.isrecord)
        {
            return "删除失败，正在记录，请先关闭";
        }
        //下面两句话用于数据库保存，做备份使用，为了防止误删，最好的办法就是删除之前先备份。
        //针对SQLITE，能减少数据库的体积
        backupService.vacuum();
        //备份的名称，会被上传到webdav上，不必在意他的实现机制
        backupService.backup("merge");
        //Mybatis的查询语句，查询所有符合的条目
        LambdaQueryWrapper<MessagePojo> wrapper = new LambdaQueryWrapper<>();
        for(String storyname:storylist)
        {
            wrapper.eq(MessagePojo::getStoryname,storyname);
        }
        List<MessagePojo> messagepojolist = messageInfoService.list(wrapper);
        //修改每一个message的故事名称，这样原有的故事集相当于变成了新的
        for(MessagePojo messagePojo:messagepojolist)
        {
            messagePojo.setStoryname(newname);
        }
        //如果成功这里就会返回OK
        if(messageInfoService.saveBatch(messagepojolist))
        {
            return "OK";
        }
        //否则返回NO（这部分您可以自行定制）
        return "NO";
    }

}
