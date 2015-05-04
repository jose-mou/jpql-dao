package com.diwa.dao.shared.order;


import com.diwa.dao.shared.entity.JoinEntity;
import junit.framework.TestCase;
import org.junit.Assert;

public class OrderByTest extends TestCase {

    public void testAsc () throws Exception {
        String property = "propertyName";
        OrderBy order = OrderBy.asc(property);
        Assert.assertEquals(property, order.getName());
        Assert.assertEquals(OrderDirection.ASC, order.getDirection());
        Assert.assertNull(order.getEntity());
    }

    public void testDesc () throws Exception {
        String property = "propertyName";
        OrderBy order = OrderBy.desc(property);
        Assert.assertEquals(property, order.getName());
        Assert.assertEquals(OrderDirection.DESC, order.getDirection());
        Assert.assertNull(order.getEntity());
    }

    public void testAsc1 () throws Exception {
        String property = "propertyName";
        JoinEntity entity = JoinEntity.join("user");
        OrderBy order = OrderBy.asc("propertyName", entity);
        Assert.assertEquals("propertyName", order.getName());
        Assert.assertEquals(OrderDirection.ASC, order.getDirection());
        Assert.assertEquals(entity, order.getEntity());
    }

    public void testDesc1 () throws Exception {
        String property = "propertyName";
        JoinEntity entity = JoinEntity.join("user");
        OrderBy order = OrderBy.desc(property, entity);
        Assert.assertEquals(property, order.getName());
        Assert.assertEquals(OrderDirection.DESC, order.getDirection());
        Assert.assertEquals(entity, order.getEntity());
    }
}