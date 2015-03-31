package com.diwa.dao;

import com.diwa.dao.shared.criteria.conditional.LikeConditional;
import org.junit.Assert;
import org.junit.Test;

public class LikeConditionalTest {

    @Test
    public void test_LikeConditional() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorld");

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_LikeConditional_fail() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorldAgain");

        Assert.assertNotSame("HashCode", v1, v2);
        Assert.assertNotSame("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_LikeConditional_fail_rightWildcard() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorld");

        v1.setRightWildcard(false);

        Assert.assertNotSame("HashCode", v1, v2);
        Assert.assertNotSame("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_LikeConditional_fail_LeftWildcard() {
        LikeConditional v1 = new LikeConditional("myfield", "helloWorld");
        LikeConditional v2 = new LikeConditional("myfield", "helloWorld");

        v1.setLeftWildcard(false);

        Assert.assertNotSame("HashCode", v1, v2);
        Assert.assertNotSame("HashCode", v1.hashCode(), v2.hashCode());
    }

}
