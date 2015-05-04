package com.diwa.dao.shared.criteria.logical;

import com.diwa.dao.shared.criteria.conditional.Conditional;
import com.diwa.dao.shared.criteria.conditional.ValueComparison;
import junit.framework.TestCase;
import org.junit.Assert;

public class NotLogicalTest extends TestCase {

    public void testNot () throws Exception {
        Conditional expression = ValueComparison.eq("property", Integer.MAX_VALUE);
        NotLogical notLogical = new NotLogical(expression);
        Assert.assertEquals(expression, notLogical.getExpresion());
        Assert.assertEquals(LogicalOperator.NOT, notLogical.getOperator());
    }
}