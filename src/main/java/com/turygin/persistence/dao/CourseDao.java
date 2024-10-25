package com.turygin.persistence.dao;

import com.turygin.persistence.entity.Course;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that extends generic Dao class implements course specific functionality.
 */
public class CourseDao extends Dao<Course> {

    public CourseDao() {
        super(Course.class);
    }

    /**
     * Find courses by their partial title (case-insensitive) and/or department ID.
     * @param title partial course title (case-insensitive) (null or empty for missing)
     * @param departmentId unique department ID (negative value for missing)
     * @return a list of courses found
     */
    public List<Course> findCourses(String title, long departmentId) {
        LOG.debug("Searching for courses with title '{}' and departmentId '{}'", title, departmentId);
        Session session = SESSION_FACTORY.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<Course> q = b.createQuery(ENTITY_CLASS);
        Root<Course> root = q.from(ENTITY_CLASS);

        List<JpaPredicate> predicates = new ArrayList<>();

        // Create a predicate for searching by title
        if(title != null && !title.isEmpty()) {
            predicates.add(b.like(b.lower(root.get("title")), String.format("%%%s%%", title.toLowerCase())));
        }

        // Create a predicate for searching by departmentId
        if(departmentId > 0) {
            predicates.add(b.equal(root.get("department").get("id"), departmentId));
        }

        List<Course> entities = new ArrayList<>();
        if (!predicates.isEmpty()){
            q.select(root).where(b.and(predicates.toArray(new JpaPredicate[0])));
        } else {
            q.select(root);
        }
        entities = session.createQuery(q).getResultList();

        LOG.debug("Found {} entities.", entities.size());
        session.close();
        return entities;
    }
}
