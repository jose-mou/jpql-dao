package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

public class BetweenConditionalTest extends TestCase {

    public void testBetween () throws Exception {
        String property = "propertyName";
        Integer topValue = 500;
        Integer downValue = 5;
        BetweenConditional expression =  new BetweenConditional(property,downValue,topValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(downValue, expression.getValue1());
        Assert.assertEquals(topValue, expression.getValue2());
        Assert.assertEquals(ConditionalOperator.BETWEEN, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testBetweenWithEntity () throws Exception {
        String property = "propertyName";
        Integer topValue = 500;
        Integer downValue = 5;
        DomainEntity entity = new DomainEntity(User.class);
        BetweenConditional expression =  new BetweenConditional(property, entity ,downValue, topValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(downValue, expression.getValue1());
        Assert.assertEquals(topValue, expression.getValue2());
        Assert.assertEquals(ConditionalOperator.BETWEEN, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }


    public void testBetweenConditionalHashCode() {
        BetweenConditional v1 = new BetweenConditional("name", 1, 2);
        BetweenConditional v2 = new BetweenConditional("name", 1, 2);
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }
}