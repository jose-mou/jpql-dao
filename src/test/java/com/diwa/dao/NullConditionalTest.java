package com.diwa.dao;

import com.diwa.dao.shared.criteria.conditional.NullConditional;
import org.junit.Assert;
import org.junit.Test;

public class NullConditionalTest {

    @Test
    public void test_IsNotNull() {
        NullConditional v1 = NullConditional.isNotNull("myList");
        NullConditional v2 = NullConditional.isNotNull("myList");

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_IsNull() {
        NullConditional v1 = NullConditional.isNull("myList");
        NullConditional v2 = NullConditional.isNull("myList");

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

}
