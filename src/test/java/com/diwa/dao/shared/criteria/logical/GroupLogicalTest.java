package com.diwa.dao.shared.criteria.logical;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.conditional.Conditional;
import com.diwa.dao.shared.criteria.conditional.ValueComparison;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;

public class GroupLogicalTest extends TestCase {

    public void testAnd () throws Exception {
        Conditional expression = ValueComparison.eq("property", Integer.MAX_VALUE);
        Conditional expression2 = ValueComparison.eq("property2", Integer.MIN_VALUE);
        ArrayList<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(expression);
        criterias.add(expression2);
        GroupLogical andLogical = GroupLogical.and(criterias);
        Assert.assertEquals(criterias, andLogical.getContitionals());
        Assert.assertEquals(LogicalOperator.AND, andLogical.getOperator());
    }

    public void testOr () throws Exception {
        Conditional expression = ValueComparison.eq("property", Integer.MAX_VALUE);
        Conditional expression2 = ValueComparison.eq("property2", Integer.MIN_VALUE);
        ArrayList<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(expression);
        criterias.add(expression2);
        GroupLogical andLogical = GroupLogical.or(criterias);
        Assert.assertEquals(criterias, andLogical.getContitionals());
        Assert.assertEquals(LogicalOperator.OR, andLogical.getOperator());
    }
}