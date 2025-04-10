package Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import model.*;

public class HibernateUtil {
    private static final SessionFactory factory = buildSessionFactory();
    private static Session session;

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Transportation.class)
                    .addAnnotatedClass(TouristAttraction.class)
                    .addAnnotatedClass(Schedule.class)
                    .addAnnotatedClass(Route.class)
                    .addAnnotatedClass(PassengerInformation.class)
                    .addAnnotatedClass(Images.class)
                    .addAnnotatedClass(Flight.class)
                    .addAnnotatedClass(TransportBooking.class)
                    .addAnnotatedClass(Accommodation.class)
                    .addAnnotatedClass(StayBookings.class)
                    .addAnnotatedClass(TourBooking.class)
                    .addAnnotatedClass(Pay.class)
                    .buildSessionFactory();
        } catch (Throwable throwable)
        {
            throw new ExceptionInInitializerError(throwable);
        }
    }
    public static SessionFactory getSessionFactory(){
        return factory;
    }


    public static void shutdown()
    {
        getSessionFactory().close();
    }

}
