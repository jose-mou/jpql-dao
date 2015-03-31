package com.diwa.dao;

import com.diwa.dao.shared.criteria.conditional.InConditional;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InConditionalTest {
    @Test
    public void test_InConditional_empty() {
        InConditional v1 = new InConditional("myvalues", new ArrayList<Serializable>());
        InConditional v2 = new InConditional("myvalues", new ArrayList<Serializable>());

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_InConditional_empty_string() {
        List<Serializable> arrayList1 = new ArrayList<Serializable>();
        arrayList1.add("myvalue1");

        List<Serializable> arrayList2 = new ArrayList<Serializable>();
        arrayList2.add("myvalue1");

        InConditional v1 = new InConditional("myvalues", arrayList1);
        InConditional v2 = new InConditional("myvalues", arrayList2);

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

    @Test
    public void test_InConditional_empty_string2values() {
        List<Serializable> arrayList1 = new ArrayList<Serializable>();
        arrayList1.add(1L);
        arrayList1.add(1.54D);
        arrayList1.add(1.54F);
        arrayList1.add("myvalue1");

        List<Serializable> arrayList2 = new ArrayList<Serializable>();
        arrayList2.add(1L);
        arrayList2.add(1.54D);
        arrayList2.add(1.54F);
        arrayList2.add("myvalue1");

        InConditional v1 = new InConditional("myvalues", arrayList1);
        InConditional v2 = new InConditional("myvalues", arrayList2);

        Assert.assertEquals("HashCode", v1, v2);
        Assert.assertEquals("HashCode", v1.hashCode(), v2.hashCode());
    }

}
