package edu.hebeu.nima.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hebeu.nima.mapper.MessageInfoMapper;
import edu.hebeu.nima.pojo.MessagePojo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessagePojo> implements MessageInfoService {

}
