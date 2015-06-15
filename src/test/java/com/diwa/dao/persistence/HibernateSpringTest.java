package com.diwa.dao.persistence;

import com.diwa.dao.DAO;
import com.diwa.dao.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by josemo on 5/16/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class HibernateSpringTest {

    @Autowired
    @Qualifier(value = "userDAO")
    private DAO<User, Long> userDao;

    @Autowired
    private PlatformTransactionManager txManager;

    @Test
    public void testCRUDEntity() throws  Exception{
        final User user = new User();
        user.setName("Jose");
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction( def );
        User userSaved = userDao.save(user);
        txManager.commit(status);
        User userRead = userDao.read(userSaved.getId());
        Assert.assertEquals(userSaved.getId(), userRead.getId());
        Assert.assertEquals(userSaved.getName(), userRead.getName());
        def = new DefaultTransactionDefinition();
        status = txManager.getTransaction( def );
                userDao.delete(userRead);
        txManager.commit(status);
        Assert.assertNull(userDao.read(userSaved.getId()));
    }
}
