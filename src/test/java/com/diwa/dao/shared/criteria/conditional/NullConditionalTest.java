package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

public class NullConditionalTest extends TestCase {

    public void testIsNull () throws Exception {
        String property = "propertyName";
        NullConditional expression = NullConditional.isNull(property);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_NULL, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testIsNullWithEntity () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        NullConditional expression = NullConditional.isNull(property, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_NULL, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }

    public void testIsNotNull () throws Exception {
        String property = "propertyName";
        NullConditional expression = NullConditional.isNotNull(property);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_NOT_NULL, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testIsNotNullWithEntity () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        NullConditional expression = NullConditional.isNotNull(property, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(ConditionalOperator.IS_NOT_NULL, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }

    public void testIsNotNullHashCode() {
        NullConditional v1 = NullConditional.isNotNull("myList");
        NullConditional v2 = NullConditional.isNotNull("myList");

        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }

    public void testIstNullHashCode() {
        NullConditional v1 = NullConditional.isNull("myList");
        NullConditional v2 = NullConditional.isNull("myList");
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }
}