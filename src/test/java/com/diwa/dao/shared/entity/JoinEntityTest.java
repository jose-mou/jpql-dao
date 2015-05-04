package com.diwa.dao.shared.entity;

import com.diwa.dao.domain.User;
import junit.framework.TestCase;
import org.junit.Assert;

public class JoinEntityTest extends TestCase {

    public void testJoin () throws Exception {
        String property = "propertyName";
        JoinEntity join = JoinEntity.join(property);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.RIGHT_JOIN, join.getOperator());
        Assert.assertNull(join.getEntity());
        Assert.assertEquals(property + "_" + Math.abs(property.hashCode()), join.getAlias());
    }

    public void testLeftJoin () throws Exception {
        String property = "propertyName";
        JoinEntity join = JoinEntity.leftJoin(property);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.LEFT_JOIN, join.getOperator());
        Assert.assertNull(join.getEntity());
        Assert.assertEquals(property + "_" + Math.abs(property.hashCode()), join.getAlias());
    }

    public void testJoinAlias () throws Exception {
        String property = "propertyName";
        String alias = "joinAlias";
        JoinEntity join = JoinEntity.join(property, alias);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.RIGHT_JOIN, join.getOperator());
        Assert.assertNull(join.getEntity());
        Assert.assertEquals(alias, join.getAlias());
    }

    public void testLeftJoinAlias () throws Exception {
        String property = "propertyName";
        String alias = "joinAlias";
        JoinEntity join = JoinEntity.leftJoin(property, alias);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.LEFT_JOIN, join.getOperator());
        Assert.assertNull(join.getEntity());
        Assert.assertEquals(alias, join.getAlias());
    }

    public void testJoinEntity () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        JoinEntity join = JoinEntity.join(property, entity);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.RIGHT_JOIN, join.getOperator());
        Assert.assertEquals(entity, join.getEntity());
        Assert.assertEquals(property + "_" + Math.abs(property.hashCode()), join.getAlias());
    }

    public void testLeftJoinEntity () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        JoinEntity join = JoinEntity.leftJoin(property, entity);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.LEFT_JOIN, join.getOperator());
        Assert.assertEquals(entity, join.getEntity());
        Assert.assertEquals(property + "_" + Math.abs(property.hashCode()), join.getAlias());
    }

    public void testJoinEntityAndAlias () throws Exception {
        String property = "propertyName";
        String alias = "joinAlias";
        DomainEntity entity = new DomainEntity(User.class);
        JoinEntity join = JoinEntity.join(property, entity, alias);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.RIGHT_JOIN, join.getOperator());
        Assert.assertEquals(entity, join.getEntity());
        Assert.assertEquals(alias, join.getAlias());
    }

    public void testLeftJoinEntityAndAlias () throws Exception {
        String property = "propertyName";
        String alias = "joinAlias";
        DomainEntity entity = new DomainEntity(User.class);
        JoinEntity join = JoinEntity.leftJoin(property, entity, alias);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.LEFT_JOIN, join.getOperator());
        Assert.assertEquals(entity, join.getEntity());
        Assert.assertEquals(alias, join.getAlias());
    }
}