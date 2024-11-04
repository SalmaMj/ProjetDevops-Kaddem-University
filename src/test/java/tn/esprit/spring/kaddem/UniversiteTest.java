package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UniversiteTest {
    private Universite universite;

    @BeforeEach
    void setUp() {
        universite = new Universite("Test University");
    }

    @Test
    void testGettersAndSetters() {
        universite.setNomUniv("New University");
        assertEquals("New University", universite.getNomUniv());

        universite.setIdUniv(1);
        assertEquals(1, universite.getIdUniv());

        HashSet<Departement> departements = new HashSet<>();
        universite.setDepartements(departements);
        assertEquals(departements, universite.getDepartements());
    }

    @Test
    void testConstructorWithParameters() {
        Universite universiteWithId = new Universite(1, "University with ID");

        assertEquals(1, universiteWithId.getIdUniv());
        assertEquals("University with ID", universiteWithId.getNomUniv());
    }

    @Test
    void testAddDepartement() {
        Departement departement = new Departement();
        HashSet<Departement> departements = new HashSet<>();
        departements.add(departement);
        universite.setDepartements(departements);

        assertEquals(1, universite.getDepartements().size());
        assertTrue(universite.getDepartements().contains(departement));
    }

}
