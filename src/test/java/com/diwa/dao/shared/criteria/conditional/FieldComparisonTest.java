package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.Role;
import com.diwa.dao.domain.User;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

public class FieldComparisonTest extends TestCase {

    public void testEq () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        FieldComparison expression = (FieldComparison) FieldComparison.eq(field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.EQ, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testEqWithEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        FieldComparison expression = (FieldComparison) FieldComparison.eq(entity1, field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.EQ, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testEqWithBothEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        DomainEntity entity2 = new DomainEntity(Role.class);
        FieldComparison expression = (FieldComparison) FieldComparison.eq(entity1, field1, entity2, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.EQ, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertEquals(entity2, expression.getEntity2());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testNe () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        FieldComparison expression = (FieldComparison) FieldComparison.ne(field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.NE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testNeWithEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        FieldComparison expression = (FieldComparison) FieldComparison.ne(entity1, field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.NE, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testNeWithBothEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        DomainEntity entity2 = new DomainEntity(Role.class);
        FieldComparison expression = (FieldComparison) FieldComparison.ne(entity1, field1, entity2, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.NE, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertEquals(entity2, expression.getEntity2());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testGt () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        FieldComparison expression = (FieldComparison) FieldComparison.gt(field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.GT, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testGtWithEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        FieldComparison expression = (FieldComparison) FieldComparison.gt(entity1, field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.GT, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testGtWithBothEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        DomainEntity entity2 = new DomainEntity(Role.class);
        FieldComparison expression = (FieldComparison) FieldComparison.gt(entity1, field1, entity2, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.GT, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertEquals(entity2, expression.getEntity2());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLt () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        FieldComparison expression = (FieldComparison) FieldComparison.lt(field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.LT, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLtWithEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        FieldComparison expression = (FieldComparison) FieldComparison.lt(entity1, field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.LT, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLtWithBothEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        DomainEntity entity2 = new DomainEntity(Role.class);
        FieldComparison expression = (FieldComparison) FieldComparison.lt(entity1, field1, entity2, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.LT, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertEquals(entity2, expression.getEntity2());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testGe () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        FieldComparison expression = (FieldComparison) FieldComparison.ge(field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.GE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testGeWithEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        FieldComparison expression = (FieldComparison) FieldComparison.ge(entity1, field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.GE, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testGeWithBothEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        DomainEntity entity2 = new DomainEntity(Role.class);
        FieldComparison expression = (FieldComparison) FieldComparison.ge(entity1, field1, entity2, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.GE, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertEquals(entity2, expression.getEntity2());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLe () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        FieldComparison expression = (FieldComparison) FieldComparison.le(field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.LE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLeWithEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        FieldComparison expression = (FieldComparison) FieldComparison.le(entity1, field1, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.LE, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLeWithBothEntity () throws Exception {
        String field1 = "filed1Name";
        String field2 = "field2Name";
        DomainEntity entity1 = new DomainEntity(User.class);
        DomainEntity entity2 = new DomainEntity(Role.class);
        FieldComparison expression = (FieldComparison) FieldComparison.le(entity1, field1, entity2, field2);
        Assert.assertEquals(field1, expression.getName());
        Assert.assertEquals(field2, expression.getField());
        Assert.assertEquals(ConditionalOperator.LE, expression.getOperator());
        Assert.assertEquals(entity1, expression.getEntity());
        Assert.assertEquals(entity2, expression.getEntity2());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testHashCode() {
        FieldComparison v1 = (FieldComparison) FieldComparison.eq("name", "field");
        FieldComparison v2 = (FieldComparison) FieldComparison.eq("name", "field");
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }
}