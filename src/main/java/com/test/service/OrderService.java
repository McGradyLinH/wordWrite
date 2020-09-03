package com.test.service;

import com.test.pay.pay.entity.dto.NotifyDTO;

/**
 * @author lx
 * @version 1.0
 * @date 2020/9/3 22:19
 */
public interface OrderService {
    int insertOrder(NotifyDTO notifyDTO);

    int updateOrder(NotifyDTO notifyDTO);
}
