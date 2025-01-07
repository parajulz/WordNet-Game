import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import main.AutograderBuddy;
import ngrams.NGramMap;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import main.HyponymsHandler;

public class HandlerTest {

    private static final String SYNSET_FILE = "data/wordnet/synsets-EECS.txt";
    private static final String HYPONYM_FILE = "data/wordnet/hyponyms-EECS.txt";
    private static final String WORD_FILE = "data/ngrams/frequency-EECS.csv";
    private static final String COUNT_FILE = "data/ngrams/total_counts.csv";
    private static final String TOP_14377 = "data/ngrams/top_14377_words.csv";
    private static final String TOTAL_COUNTS = "data/ngrams/total_counts.csv";
    private static final String SYNSETS_1000 = "data/wordnet/synsets1000-subgraph.txt";
    private static final String HYPONYMS_1000 = "data/wordnet/hyponyms1000-subgraph.txt";

    private static final HyponymsHandler handler = new HyponymsHandler(SYNSET_FILE, HYPONYM_FILE,
            new NGramMap(WORD_FILE, COUNT_FILE));

    @Test
    public void testSingleWordQuery_CS61A() {
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("CS61A")), 2010, 2020, 4);
        String expected = "[CS170, CS61A, CS61B, CS61C]";
        String actual = handler.handle(query);
        assertEquals(expected, actual, "Expected top 4 results for CS61A.");
    }

    @Test
    public void testSingleWordQuery_WithKZero_CS61B() {
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("CS61B")), 2000, 2020, 0);
        String expected = "[CS160, CS162, CS164, CS168, CS169, CS170, CS172, CS174, CS176, CS184, CS186, CS188, CS189, CS191, CS61B, bean, bee]";
        String actual = handler.handle(query);
        assertEquals(expected, actual, "when k = 0 ");
    }

    @Test
    public void nonEmptyIntersection() {
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("CS61A", "CS61B", "CS70")), 2000, 2020, 0);
        String expected = "[CS162, CS170, CS172, CS174, CS176, CS188, CS189, CS191, bean]";
        String actual = handler.handle(query);
        assertEquals(expected, actual, "Expected hyponyms for 'bee' and 'bean'. List should contain all");
    }

    @Test
    public void largeIntersection() {
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("CS61A", "CS61B", "bee", "CS70", "CS170", "CS172")), 2000, 2020, 0);
        String expected = "[CS172]";
        String actual = handler.handle(query);
        assertEquals(expected, actual, "Large Intersection");
    }

    @Test
    public void emptyIntersection() {
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("CS61A", "CS61B", "bee", "CS70", "CS170", "CS172", "CS174")), 2000, 2020, 0);
        String expected = "[]";
        String actual = handler.handle(query);
        assertEquals(expected, actual, "Empty Intersection");
    }


    @Test
    public void testQuery_WithNonExistentYearRange() {
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("CS61A")), 1800, 1801, 3);
        String expected = "[]";
        String actual = handler.handle(query);
        assertEquals(expected, actual, "Expected an empty list when no data exists for the specified year range.");
    }

    @Test
    public void sameHandler() {
        NgordnetQueryHandler customHandle = AutograderBuddy.getHyponymsHandler(
                TOP_14377, TOTAL_COUNTS, SYNSETS_1000, HYPONYMS_1000);
        NgordnetQuery query = new NgordnetQuery(new ArrayList<>(Set.of("os")), 1470, 2019, 0);
        String expected = "[Wormian_bone, adult_tooth, anklebone, anterior, anvil, arcus_zygomaticus, arm_bone, astragal, astragalus, atlas, atlas_vertebra, auditory_ossicle, axis, axis_vertebra, baby_tooth, back_tooth, bare_bone, bicuspid, bone, bonelet, braincase, brainpan, breastbone, bucktooth, calcaneus, calf_bone, calvaria, canine, canine_tooth, cannon_bone, capitate, capitate_bone, carnassial_tooth, carpal, carpal_bone, cartilage_bone, caudal_vertebra, centrum, cervical_vertebra, cheekbone, chop, chopper, clavicle, coccygeal_vertebra, coccyx, collarbone, conodont, corpus_sternum, costa, cranium, cuboid_bone, cuneiform_bone, cuspid, deciduous_tooth, dentin, dentine, diaphysis, dogtooth, dorsal_vertebra, elbow_bone, epiphysis, ethmoid, ethmoid_bone, eye_tooth, eyetooth, fang, femoris, femur, fetter_bone, fibula, fishbone, forehead, front_tooth, frontal_bone, furcula, gladiolus, grinder, hamate, hamate_bone, hammer, heelbone, hipbone, humerus, hyoid, hyoid_bone, ilium, incisor, incus, innominate_bone, ischial_bone, ischium, jaw, jawbone, jowl, jugal_bone, kneecap, kneepan, lacrimal_bone, lantern_jaw, leg_bone, long_bone, lower_jaw, lower_jawbone, lumbar_vertebra, lunate_bone, malar, malar_bone, malleus, malposed_tooth, mandible, mandibula, mandibular_bone, manubrium, marrowbone, maxilla, maxillary, membrane_bone, metacarpal, metacarpal_bone, metatarsal, milk_tooth, modiolus, molar, nasal, nasal_bone, nasal_concha, navicular, neck_bone, occipital_bone, occiput, os, os_breve, os_capitatum, os_frontale, os_hamatum, os_hyoideum, os_ischii, os_longum, os_lunatum, os_nasale, os_palatinum, os_pisiforme, os_pubis, os_scaphoideum, os_sesamoideum, os_sphenoidale, os_tarsi_fibulare, os_temporale, os_trapezium, os_trapezoideum, os_triquetrum, os_zygomaticum, ossicle, ossiculum, palatine, palatine_bone, parietal_bone, pastern, patella, pearly, permanent_tooth, phalanx, pisiform, pisiform_bone, posterior, premolar, primary_tooth, pubic_bone, pubis, pyramidal_bone, radius, ramus, rib, round_bone, sacral_vertebra, sacrum, scaphoid_bone, scapula, semilunar_bone, sesamoid, sesamoid_bone, shaft, shin, shin_bone, shinbone, short_bone, shoulder_blade, shoulder_bone, sinciput, skull, skullcap, sphenoid, sphenoid_bone, splint_bone, stapes, sternum, stirrup, submaxilla, sutural_bone, tail_bone, talus, tarsal, tarsal_bone, temporal_bone, thighbone, thoracic_vertebra, tibia, tooth, trapezium, trapezium_bone, trapezoid, trapezoid_bone, triquetral, triquetral_bone, true_rib, turbinal, turbinate, turbinate_bone, tusk, tympanic_bone, ulna, unciform_bone, upper_jaw, upper_jawbone, vertebra, vomer, wisdom_tooth, wishbone, wishing_bone, wrist_bone, xiphoid_process, zygoma, zygomatic, zygomatic_arch, zygomatic_bone]";
        String actual = customHandle.handle(query);
        assertEquals(expected, actual, "Very Long One");
    }

}
