package com.diwa.dao;

import com.diwa.dao.shared.criteria.conditional.CaseSensitiveConditional;
import com.diwa.dao.shared.criteria.conditional.Conditional;
import com.diwa.dao.shared.criteria.conditional.ValueComparison;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ConditionalTest {

    @Test
    public void testValueComparison_as_conditional() {
        Date date = new Date();
        Conditional v1 = ValueComparison.eq("startdate", date);
        Conditional v2 = ValueComparison.eq("startdate", date);
        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testValueComparison_as_caseSensitive_fail() {
        Date date = new Date();
        CaseSensitiveConditional v1 = (CaseSensitiveConditional) ValueComparison.eq("startdate", new Date());
        v1.setCaseSensitive(true);
        CaseSensitiveConditional v2 = (CaseSensitiveConditional) ValueComparison.eq("startdate", new Date());
        v2.setCaseSensitive(false);
        Assert.assertNotSame("HashCode", v1, v2);
        Assert.assertNotSame("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testValueComparison_as_caseSensitive() {
        Date date = new Date();
        CaseSensitiveConditional v1 = (CaseSensitiveConditional) ValueComparison.eq("startdate", new Date());
        v1.setCaseSensitive(true);
        CaseSensitiveConditional v2 = (CaseSensitiveConditional) ValueComparison.eq("startdate", new Date());
        v2.setCaseSensitive(true);
        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testValueComparison_as_ValueComparison() {
        Date date = new Date();
        ValueComparison v1 = (ValueComparison) ValueComparison.eq("startdate", date);
        v1.setCaseSensitive(true);
        ValueComparison v2 = (ValueComparison) ValueComparison.eq("startdate", date);
        v2.setCaseSensitive(true);
        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testValueComparison_as__ne_ValueComparison() {
        Date date = new Date();
        ValueComparison v1 = (ValueComparison) ValueComparison.ne("startdate", date);
        v1.setCaseSensitive(true);
        ValueComparison v2 = (ValueComparison) ValueComparison.ne("startdate", date);
        v2.setCaseSensitive(true);
        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

}
