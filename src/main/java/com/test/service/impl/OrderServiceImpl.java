package com.test.service.impl;

import com.test.dao.OrderDao;
import com.test.pay.pay.entity.dto.NotifyDTO;
import com.test.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lx
 * @version 1.0
 * @date 2020/9/3 22:19
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Override
    public int insertOrder(NotifyDTO notifyDTO) {
        return orderDao.insertOrder(notifyDTO);
    }

    @Override
    public int updateOrder(NotifyDTO notifyDTO) {
        return orderDao.updateOrder(notifyDTO);
    }
}
