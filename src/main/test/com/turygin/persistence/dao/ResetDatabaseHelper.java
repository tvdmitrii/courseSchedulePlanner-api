package com.turygin.persistence.dao;

import com.turygin.persistence.SessionFactoryProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.InputStream;
import java.util.Scanner;

/**
 * A helper class that will reset the database using a provided SQL script.
 */
public class ResetDatabaseHelper {

    private static final Logger logger = LogManager.getLogger(ResetDatabaseHelper.class);

    /** Name of the database reset script resource. */
    private static final String DB_RESET_RESOURCE = "/reset_database.sql";

    /**
     * Resets the database.
     * @return true if database was reset successfully, false otherwise.
     */
    public static boolean reset() {
        try(InputStream stream = UserDaoTest.class.getResourceAsStream(DB_RESET_RESOURCE)) {

            if (stream == null) {
                logger.error("Database reset script '{}' was not found.", DB_RESET_RESOURCE);
                return false;
            }

            try(Scanner input = new Scanner(stream)) {
                // Use standard SQL command delimiter
                input.useDelimiter(";");

                SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();
                Session session = sessionFactory.openSession();
                session.beginTransaction();

                // Execute commands one at a time
                while (input.hasNext()) {
                    String sqlCommand = input.next().trim();
                    if (sqlCommand.isEmpty()) continue;

                    // Execute command
                    session.createNativeQuery(sqlCommand).executeUpdate();
                }

                session.getTransaction().commit();
                session.close();

                return true;
            }
        } catch (Exception exception) {
            logger.error("Could not reset the database.", exception);
        }

        return false;
    }
}
