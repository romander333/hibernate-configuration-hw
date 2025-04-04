package mate.academy.dao;

import mate.academy.lib.Dao;
import mate.academy.model.Movie;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Optional;

@Dao
public class MovieDaoImpl implements MovieDao {

    @Override
    public Movie add(Movie movie) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(movie);
            if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty");
            }
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Movie can not be added to the table");
        } finally {
            session.close();
        }
        return movie;
    }

    @Override
    public Optional<Movie> get(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            if (Optional.ofNullable(session.get(Movie.class, id)).isPresent()) {
                return Optional.of(session.get(Movie.class, id));
            }
            throw new DataProcessingException("Movie can not be found");
        } finally {
            session.close();
        }
    }
}
