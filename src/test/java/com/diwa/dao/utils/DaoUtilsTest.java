package com.diwa.dao.utils;

import com.diwa.dao.domain.Role;
import com.diwa.dao.domain.User;
import junit.framework.TestCase;
import org.junit.Assert;

public class DaoUtilsTest extends TestCase {

    public void testGetEntityNameNameAttribute () throws Exception {
        Assert.assertEquals("entityUser", DaoUtils.getEntityName(User.class));
    }

    public void testGetEntityNameClassName () throws Exception {
        Assert.assertEquals("Role", DaoUtils.getEntityName(Role.class));
    }

    public void testHasIdTrue () throws Exception {
        User user = new User();
        user.setId(1L);
        Assert.assertEquals(true, DaoUtils.hasId(user));
    }

    public void testHasIdFalse () throws Exception {
        User user = new User();
        Assert.assertEquals(false, DaoUtils.hasId(user));
    }

    public void testHasIdFalseBasicType () throws Exception {
        Role user = new Role();
        Assert.assertEquals(false, DaoUtils.hasId(user));
    }

    public void testGetPrimaryKeyNameFieldAnnotated () throws Exception {
        Assert.assertEquals("id", DaoUtils.getPrimaryKeyName(User.class));
    }

    public void testGetPrimaryKeyNameMethodAnnotated () throws Exception {
        Assert.assertEquals("roleId", DaoUtils.getPrimaryKeyName(Role.class));
    }
}