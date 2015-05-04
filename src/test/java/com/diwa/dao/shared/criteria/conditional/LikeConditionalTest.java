package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

public class LikeConditionalTest extends TestCase {

    public void testLike () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        LikeConditional expression =  new LikeConditional(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LIKE, expression.getOperator());
        Assert.assertNull(expression.getEntity());
        Assert.assertFalse(expression.isCaseSensitive());
    }

    public void testLikeWithEntity () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class);
        LikeConditional expression =  new LikeConditional(property, propertyValue, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LIKE, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
        Assert.assertFalse(expression.isCaseSensitive());
    }

    public void testLikeCaseNotSensitive () throws Exception {
        String property = "propertyName";
        String propertyValue = "propertyValue";
        DomainEntity entity = new DomainEntity(User.class);
        LikeConditional expression =  new LikeConditional(property, propertyValue, true, entity);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValue());
        Assert.assertEquals(ConditionalOperator.LIKE, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
        Assert.assertTrue(expression.isCaseSensitive());
    }

    public void testLikeConditionalHashCode() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorld");
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }

    public void testLikeConditionalHashCode2() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorldAgain");
        Assert.assertNotSame(v1, v2);
        Assert.assertNotSame(v1.hashCode(), v2.hashCode());
    }

    public void testLikeConditionalRightWildcardHashCode() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorld");
        v1.setRightWildcard(false);
        Assert.assertNotSame(v1, v2);
        Assert.assertNotSame(v1.hashCode(), v2.hashCode());
    }

    public void testLikeConditionalLeftWildcardHashCode() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorld");
        v1.setLeftWildcard(false);
        Assert.assertNotSame(v1, v2);
        Assert.assertNotSame(v1.hashCode(), v2.hashCode());
    }
}