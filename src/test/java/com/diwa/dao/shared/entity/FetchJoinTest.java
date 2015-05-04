package com.diwa.dao.shared.entity;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.order.OrderBy;
import com.diwa.dao.shared.order.OrderDirection;
import junit.framework.TestCase;
import org.junit.Assert;

public class FetchJoinTest extends TestCase {

    public void testLeftJoinFetch () throws Exception {
        String property = "propertyName";
        FetchJoin join = FetchJoin.leftJoinFetch("propertyName");
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.LEFT_FETCH_JOIN, join.getOperator());
        Assert.assertNull(join.getEntity());
        Assert.assertNull(join.getAlias());
    }

    public void testJoinFetch () throws Exception {
        String property = "propertyName";
        FetchJoin join = FetchJoin.joinFetch(property);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.RIGHT_FETCH_JOIN, join.getOperator());
        Assert.assertNull(join.getEntity());
        Assert.assertNull(join.getAlias());
    }

    public void testLeftJoinFetch1 () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        FetchJoin join = FetchJoin.leftJoinFetch(property, entity);
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.LEFT_FETCH_JOIN, join.getOperator());
        Assert.assertEquals(entity, join.getEntity());
        Assert.assertNull(join.getAlias());
    }

    public void testJoinFetch1 () throws Exception {
        String property = "propertyName";
        DomainEntity entity = new DomainEntity(User.class);
        FetchJoin join = FetchJoin.joinFetch(property,entity );
        Assert.assertEquals(property, join.getName());
        Assert.assertEquals(JoinOperator.RIGHT_FETCH_JOIN, join.getOperator());
        Assert.assertEquals(entity, join.getEntity());
        Assert.assertNull(join.getAlias());

    }
}