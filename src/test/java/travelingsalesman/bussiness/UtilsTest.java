package travelingsalesman.bussiness;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.operationalresearch.travelingsalesman.bussiness.Utils;
import com.operationalresearch.travelingsalesman.bussiness.WayMaker;
import com.operationalresearch.travelingsalesman.model.Matrix;
import com.operationalresearch.travelingsalesman.model.Pathway;
import java.io.File;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests the support operations.
 *
 * @author - @alangomes7
 */
@Slf4j
public class UtilsTest {
  private static Utils utils;
  private static WayMaker wayMaker;
  private static File matrixFile;
  private static Matrix matrixTests;
  private static int matrixSize;

  /** Here we initialize the main variables. This method is also called by "setup". */
  @BeforeAll
  public static void initialize() {
    log.debug("Tests started.\n");
    utils = new Utils();
    wayMaker = new WayMaker();
    String pathTestResources =
        Paths.get("src", "test", "/resources/").toFile().getAbsolutePath() + "/";
    matrixFile = new File(pathTestResources + "matrixForTests.txt");
    matrixSize = 10;
    createDatabase_shouldBeSuccess();
    matrixTests = utils.fileToMatrix(matrixFile);
  }

  /** Create a database creation is success. */
  public static void createDatabase_shouldBeSuccess() {
    // delete matrix file to avoid append
    if (matrixFile.exists()) {
      log.debug("Database deleted: " + matrixFile.delete());
    }
    assertDoesNotThrow(() -> utils.createDatabase(matrixFile, matrixSize));
  }

  /** Tests the readMatrix operation. */
  @Test
  public void readMatrix_shouldBeSuccess() {
    assertEquals(matrixSize, matrixTests.getSize());
    log.debug("The matrix:\n" + matrixTests.toString());
  }

  /** Tests the exhaustiveSearch operation. */
  @Test
  public void exhaustiveSearch_shouldBeSuccess() {
    assertDoesNotThrow(() -> wayMaker.exhaustiveSearch(matrixTests));
  }

  /** Tests random search operation. */
  @Test
  public void randomSearch_shouldBeSuccess() {
    assertDoesNotThrow(() -> wayMaker.randomSearch(matrixTests));
  }

  /** Tests mix search operation: exhaustive search + random search. */
  @Test
  public void mixArraysExhaustive_shouldBeSuccess() {
    Pathway pathwayParent = wayMaker.exhaustiveSearch(matrixTests);
    assertDoesNotThrow(
        () -> wayMaker.mixArrays(pathwayParent, matrixTests, WayMaker.PathMode.EXHAUSTIVE));
  }

  /** Tests mix search operation: random search + exhaustive search. */
  @Test
  public void mixArraysRandom_shouldBeSuccess() {
    Pathway pathwayParent = wayMaker.exhaustiveSearch(matrixTests);
    assertDoesNotThrow(
        () -> wayMaker.mixArrays(pathwayParent, matrixTests, WayMaker.PathMode.RANDOM));
  }
}
