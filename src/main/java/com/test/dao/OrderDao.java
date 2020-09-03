package com.test.dao;

import com.test.domain.Title;
import com.test.pay.pay.entity.dto.NotifyDTO;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/26 9:50
 */
public interface OrderDao {
    int insertOrder(NotifyDTO notifyDTO);

    int updateOrder(NotifyDTO notifyDTO);
}
