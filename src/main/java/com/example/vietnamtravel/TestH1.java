package com.example.vietnamtravel;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.*;
import javax.naming.*;

public class TestH1 {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            //create value
            User user = new User();
            user.setHoTen("Hieu DZ");
            user.setSoDienThoai("01111112");
            user.setEmail("hieudz@dzsieucappro.com");
            user.setMatKhau("1234");

            //save
            session.save(user);

            //
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            System.out.println(e);
        }
        finally {
            //close
            sessionFactory.close();
        }
    }
}
