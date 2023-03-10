package cn.river.im.service.impl;

import cn.river.im.entity.Group;
import cn.river.im.mapper.GroupMapper;
import cn.river.im.service.GroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 阳名
* @description 针对表【group(群)】的数据库操作Service实现
* @createDate 2022-11-03 15:25:55
*/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
    implements GroupService{

}




