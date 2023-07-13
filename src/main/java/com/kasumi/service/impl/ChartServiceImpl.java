package com.kasumi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kasumi.dao.entity.Chart;
import com.kasumi.service.ChartService;
import com.kasumi.dao.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author zhang
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2023-07-13 15:55:16
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




