package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.domain.User;
import com.diwa.dao.shared.entity.DomainEntity;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InConditionalTest extends TestCase {

    public void testIn () throws Exception {
        String property = "propertyName";
        List<Integer> propertyValue = new ArrayList<Integer>();
        propertyValue.add(1);
        propertyValue.add(9);
        InConditional expression =  new InConditional(property, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValues());
        Assert.assertEquals(ConditionalOperator.IN, expression.getOperator());
        Assert.assertNull(expression.getEntity());
    }

    public void testInWithEntity () throws Exception {
        String property = "propertyName";
        List<Integer> propertyValue = new ArrayList<Integer>();
        propertyValue.add(1);
        propertyValue.add(9);
        DomainEntity entity = new DomainEntity(User.class);
        InConditional expression =  new InConditional(property, entity, propertyValue);
        Assert.assertEquals(property, expression.getName());
        Assert.assertEquals(propertyValue, expression.getValues());
        Assert.assertEquals(ConditionalOperator.IN, expression.getOperator());
        Assert.assertEquals(entity, expression.getEntity());
    }

    public void testInConditionalHashCode() {
        InConditional v1 = new InConditional("myvalues", new ArrayList<Serializable>());
        InConditional v2 = new InConditional("myvalues", new ArrayList<Serializable>());
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }

    public void testInConditionalHashCode2() {
        List<Serializable> arrayList1 = new ArrayList<Serializable>();
        arrayList1.add("myvalue1");
        List<Serializable> arrayList2 = new ArrayList<Serializable>();
        arrayList2.add("myvalue1");
        InConditional v1 = new InConditional("myvalues", arrayList1);
        InConditional v2 = new InConditional("myvalues", arrayList2);
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }

    public void testInConditionalHashCode3() {
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
        Assert.assertEquals(v1, v2);
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }
}