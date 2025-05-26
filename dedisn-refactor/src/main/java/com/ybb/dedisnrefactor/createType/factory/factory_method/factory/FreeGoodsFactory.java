package com.ybb.dedisnrefactor.createType.factory.factory_method.factory;

import com.ybb.dedisnrefactor.createType.factory.simple_factory.service.IFreeGoods;

/**
 * 抽象工厂
 * @author spikeCong
 * @date 2022/9/9
 **/
public interface FreeGoodsFactory {

    IFreeGoods getInstance();
}
