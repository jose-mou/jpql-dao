package com.diwa.dao;

import com.diwa.dao.shared.criteria.conditional.BetweenConditional;
import com.diwa.dao.shared.criteria.conditional.FieldComparison;
import org.junit.Assert;
import org.junit.Test;

public class FieldComparisonTest {

    @Test
    public void test_basic() {
        FieldComparison v1 = (FieldComparison) FieldComparison.eq("name", "field");
        FieldComparison v2 = (FieldComparison) FieldComparison.eq("name", "field");

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_basic_fail() {
        FieldComparison v1 = (FieldComparison) FieldComparison.eq("name1", "field");
        FieldComparison v2 = (FieldComparison) FieldComparison.eq("name1", "field");

        v1.setCaseSensitive(false);

        Assert.assertNotSame("HashCode", v1, v2);
        Assert.assertNotSame("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_basic_f() {
        FieldComparison v1 = (FieldComparison) FieldComparison.eq("name1", "field");
        FieldComparison v2 = (FieldComparison) FieldComparison.eq("name1", "field");

        v1.setCaseSensitive(false);
        v2.setCaseSensitive(false);

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testBetweenConditional() {
        BetweenConditional v1 = new BetweenConditional("name", 1, 2);
        BetweenConditional v2 = new BetweenConditional("name", 1, 2);

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testBetweenConditional_fail() {
        BetweenConditional v1 = new BetweenConditional("name", 1, 2);
        BetweenConditional v2 = new BetweenConditional("name", 2, 2);

        Assert.assertNotSame("HashCode", v1, v2);
        Assert.assertNotSame("HashCode", v1.hashCode(), v2.hashCode());
    }

}
