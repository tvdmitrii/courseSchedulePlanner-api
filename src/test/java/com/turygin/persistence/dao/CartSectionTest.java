package com.turygin.persistence.dao;

import com.turygin.persistence.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** Cart section DAO tests. */
public class CartSectionTest {
    private static final Logger LOG = LogManager.getLogger(CartSectionTest.class);

    /** List of cart sections. */
    private static final List<CartSection> CART_SECTIONS = new ArrayList<>();

    /** DAO for working with cart sections in the database. */
    private static final Dao<CartSection> CART_SECTION_DAO = new Dao<>(CartSection.class);

    /** DAO for working with cart courses in the database. */
    private static final Dao<CartCourse> CART_COURSE_DAO = new Dao<>(CartCourse.class);

    /** DAO for working with sections in the database. */
    private static final Dao<Section> SECTION_DAO = new Dao<>(Section.class);

    /** Initial cart section count. */
    private static final int INITIAL_CART_SECTION_COUNT = 5;

    /** Reset database before each run. */
    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        CART_SECTIONS.clear();

        // Populate cartSections
        CART_SECTIONS.addAll(CART_SECTION_DAO.getAll());
        assertEquals(INITIAL_CART_SECTION_COUNT, CART_SECTIONS.size());

        // Sort cart courses by id to guarantee order for convenience
        CART_SECTIONS.sort(Comparator.comparingLong(CartSection::getId));
    }

    /** Ensure cart section can be loaded by id. */
    @Test
    void getById() {
        CartSection cartSection1 = CART_SECTIONS.get(0);

        CartSection cartSection = CART_SECTION_DAO.getById(cartSection1.getId());

        assertEquals(cartSection1, cartSection);
    }

    /** Ensure getting cart section by invalid id returns null. */
    @Test
    void getById_InvalidId() {
        CartSection cartSection = CART_SECTION_DAO.getById(INITIAL_CART_SECTION_COUNT + 10);

        assertNull(cartSection);
    }

    /** Ensure a cart section can be inserted. */
    @Test
    void insert() {
        CartCourse cartCourse = CART_COURSE_DAO.getById(2);
        Section section = SECTION_DAO.getById(5);
        CartSection newCartSection = new CartSection(cartCourse, section);

        CART_SECTION_DAO.insert(newCartSection);

        CartSection cartSection = CART_SECTION_DAO.getById(newCartSection.getId());
        cartCourse = CART_COURSE_DAO.getById(2);
        section = SECTION_DAO.getById(5);

        assertEquals(newCartSection, cartSection);
        assertEquals(cartSection.getCourse(), cartCourse);
        assertEquals(cartSection.getSection(), section);
    }

    /** Ensure cart section can be deleted but the underlying section remains. */
    @Test
    void delete() {
        CartSection cartSection1 = CART_SECTIONS.get(1);
        long sectionId = cartSection1.getSection().getId();

        CART_SECTION_DAO.delete(cartSection1);

        // CartSection removed
        CartSection cartSection = CART_SECTION_DAO.getById(cartSection1.getId());
        assertNull(cartSection);

        // The underlying section was not affected
        Section section = SECTION_DAO.getById(sectionId);
        assertNotNull(section);
    }

    /** Ensure all cart sections can be fetched. */
    @Test
    void getAll() {
        List<CartSection> cartSectionsFromDb = CART_SECTION_DAO.getAll();

        assertNotNull(cartSectionsFromDb);
        assertEquals(CART_SECTIONS.size(), cartSectionsFromDb.size());

        cartSectionsFromDb.sort(Comparator.comparingLong(CartSection::getId));

        for(int i = 0; i < CART_SECTIONS.size(); i++) {
            assertEquals(CART_SECTIONS.get(i), cartSectionsFromDb.get(i));
        }
    }
}
