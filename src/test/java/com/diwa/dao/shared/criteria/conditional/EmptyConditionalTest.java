package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

public class EmptyConditionalTest extends TestCase {

    public void testIsEmpty () throws Exception {
        String property = "propertyName";
        EmptyConditional expression =  EmptyConditional.isEmpty(property);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_EMPTY, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testIsEmptyWithEntity () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        EmptyConditional expression =  EmptyConditional.isEmpty(property, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_EMPTY, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }

    public void testIsNotEmpty () throws Exception {
        String property = "propertyName";
        EmptyConditional expression =  EmptyConditional.isNotEmpty(property);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_NOT_EMPTY, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testIsNotEmptyWithEntity () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        EmptyConditional expression =  EmptyConditional.isNotEmpty(property, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_NOT_EMPTY, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }
}