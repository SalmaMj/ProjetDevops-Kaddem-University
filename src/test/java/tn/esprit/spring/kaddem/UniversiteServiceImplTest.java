package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UniversiteServiceImplTest {

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private DepartementRepository departementRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllUniversites() {
        Universite uni1 = new Universite();
        Universite uni2 = new Universite();
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(uni1, uni2));

        List<Universite> result = universiteService.retrieveAllUniversites();

        assertEquals(2, result.size());
        verify(universiteRepository).findAll();
    }

    @Test
    void testAddUniversite() {
        Universite uni = new Universite();
        when(universiteRepository.save(uni)).thenReturn(uni);

        Universite result = universiteService.addUniversite(uni);

        assertNotNull(result);
        verify(universiteRepository).save(uni);
    }

    @Test
    void testRetrieveUniversite() {
        Integer id = 1;
        Universite uni = new Universite();
        when(universiteRepository.findById(id)).thenReturn(Optional.of(uni));

        Universite result = universiteService.retrieveUniversite(id);

        assertNotNull(result);
        verify(universiteRepository).findById(id);
    }

    @Test
    void testDeleteUniversite() {
        Integer id = 1;
        Universite uni = new Universite();
        when(universiteRepository.findById(id)).thenReturn(Optional.of(uni));

        universiteService.deleteUniversite(id);

        verify(universiteRepository).delete(uni); // Correcting to delete the retrieved entity
    }


}
