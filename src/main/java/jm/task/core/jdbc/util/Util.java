package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/sys";
    public static final String USER = "root";
    public static final String PASS = "12345678";
    private static SessionFactory sessionFactory;

    public static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void getClose(){
        try {
            Util.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();
                properties.put(Environment.DRIVER, DRIVER);
                properties.put(Environment.URL, URL);
                properties.put(Environment.USER, USER);
                properties.put(Environment.PASS, PASS);
                properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
                properties.put(Environment.SHOW_SQL, "true");
                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                configuration.setProperties(properties);
                configuration.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (HibernateException e) {
                throw new RuntimeException(e);
            }
        }
        return sessionFactory;
    }

    public static void getSessionClosed() {
        getSessionFactory().close();
    }
}