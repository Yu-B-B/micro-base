package com.ybb.dedisnrefactor.structure.decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    @Test
    void testBaseCake() {
        Cake cake = new BaseCake();
        assertEquals("原味", cake.getName());
        assertEquals(10, cake.getPrice());
    }

    @Test
    void testCakeDecorator() {
        Cake baseCake = new BaseCake();
        ChocolateDecorator decorator = new ChocolateDecorator(baseCake);
        System.out.println(decorator.getDes());
        System.out.println(decorator.getPrice());
    }

} 