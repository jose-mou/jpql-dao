package com.diwa.dao.persistence;

import com.diwa.dao.DAO;
import com.diwa.dao.DAOImpl;
import com.diwa.dao.domain.Profile;
import com.diwa.dao.domain.Role;
import com.diwa.dao.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by josemo on 5/16/15.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.diwa.dao")
public class SpringTestConfig {

    @Bean(name ="datasource")
    public DataSource dataSource() {
         return new EmbeddedDatabaseBuilder()
                        .setType(EmbeddedDatabaseType.HSQL)
                        .build();
    }

    @Bean
    public SessionFactory entityManagerFactory(DataSource datasource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(datasource);
        sessionBuilder.scanPackages("com.diwa.dao.domain");
        sessionBuilder.setProperty("hibernate.hbm2ddl.auto", "create");
        sessionBuilder.setProperty("hibernate.show_sql", "true");
        return sessionBuilder.buildSessionFactory();
    }

    @Bean
        public PlatformTransactionManager transactionManager(SessionFactory sessionFactory){
            return new HibernateTransactionManager(sessionFactory);
        }

    @Bean(name = "userDAO")
    public DAO<User, Long> userDao(SessionFactory sessionFactory){
        return new DAOImpl<User, Long>(User.class, sessionFactory);
    }

    @Bean(name = "profileDAO")
    public DAO<Profile, Long> profileDao(SessionFactory sessionFactory){
        return new DAOImpl<Profile, Long>(Profile.class, sessionFactory);
    }

    @Bean(name = "roleDAO")
    public DAO<Role, Long> roleDao(SessionFactory sessionFactory){
        return new DAOImpl<Role, Long>(Role.class, sessionFactory);
    }
}
