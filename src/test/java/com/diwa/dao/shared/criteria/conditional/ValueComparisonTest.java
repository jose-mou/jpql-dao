package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.logical.GroupLogical;
import com.diwa.dao.shared.criteria.logical.LogicalOperator;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;

public class ValueComparisonTest extends TestCase {

    public void testEq () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        ValueComparison expression = (ValueComparison) ValueComparison.eq(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.EQ, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testEqWithEntity () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.eq(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.EQ, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testEqWithNotCaseSensitive () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.eq(property, propertyValue, entity);
        expression.setCaseSensitive(false);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.EQ, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
        Assert.assertFalse(expression.isCaseSensitive());
    }

    public void testNe () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        ValueComparison expression = (ValueComparison) ValueComparison.ne(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.NE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testNeWithEntity () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.ne(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.NE, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testNeWithNotCaseSensitive () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.ne(property, propertyValue, entity);
        expression.setCaseSensitive(false);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.NE, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
        Assert.assertFalse(expression.isCaseSensitive());
    }

    public void testGt () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MIN_VALUE;
        ValueComparison expression = (ValueComparison) ValueComparison.gt(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.GT, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testGtWithEntity () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MIN_VALUE;
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.gt(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.GT, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }


    public void testGe () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MIN_VALUE;
        ValueComparison expression = (ValueComparison) ValueComparison.ge(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.GE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testGeWithEntity () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MIN_VALUE;
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.ge(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.GE, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }

    public void testLe () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MAX_VALUE;
        ValueComparison expression = (ValueComparison) ValueComparison.le(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testLeWithEntity () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MAX_VALUE;
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.le(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LE, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }

    public void testLt () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MAX_VALUE;
        ValueComparison expression = (ValueComparison) ValueComparison.lt(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LT, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testLtWithEntity () throws Exception {
        String property = "propertyName";
        Integer propertyValue = Integer.MAX_VALUE;
        DomainEntity entity = new DomainEntity(User.class);
        ValueComparison expression = (ValueComparison) ValueComparison.lt(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LT, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }
}