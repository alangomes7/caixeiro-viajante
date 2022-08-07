package travelingsalesman.bussiness;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.operationalresearch.travelingsalesman.bussiness.Utils;
import com.operationalresearch.travelingsalesman.bussiness.WayMaker;
import com.operationalresearch.travelingsalesman.model.Matrix;
import com.operationalresearch.travelingsalesman.model.Pathway;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests the support operations.
 *
 * @version 1.0
 */
@Slf4j
public class UtilsTest {
  private static Utils utils;
  private static WayMaker wayMaker;
  private static String pathFileResources;
  private static String pathTestResources;
  private static Matrix matrixTests;

  @BeforeAll
  public static void initialize() throws IOException {
    utils = new Utils();
    wayMaker = new WayMaker();
    pathFileResources = Paths.get("src", "main", "/resources/").toFile().getAbsolutePath() + "/";
    pathTestResources = Paths.get("src", "test", "/resources/").toFile().getAbsolutePath() + "/";
    File matrixFile = new File(pathFileResources + "matrixTest.txt");
    utils.createDatabase(matrixFile, 4);
    matrixTests = utils.fileToMatrix(matrixFile);
  }

  @Test
  public void readMatrix_shouldBeSuccess() {
    assertEquals(10, matrixTests.getSize());
    System.out.println(matrixTests);
  }

  @Test
  public void exhaustiveSearch_shouldBeSuccess() {
    Pathway exhaustiveSearch = wayMaker.exhaustiveSearch(matrixTests);
    System.out.println(exhaustiveSearch.getTotalCost());
  }

  @Test
  public void createDatabase_shouldBeSuccess() {
    File database = new File(pathTestResources + "database.txt");
    assertDoesNotThrow(() -> utils.createDatabase(database, 100));
  }

  @Test
  public void randomSearch_shouldBeSuccess() {
    Pathway randomSearch = wayMaker.randomSearch(matrixTests);
    System.out.println(randomSearch.getTotalCost());
  }

  @Test
  public void mixArraysExhaustive_shouldBeSuccess() {
    Pathway pathwayParent = wayMaker.exhaustiveSearch(matrixTests);
    Pathway pathway = wayMaker.mixArrays(pathwayParent, matrixTests, WayMaker.PathMode.EXHAUSTIVE);
    System.out.println(pathway);
  }

  @Test
  public void mixArraysRandom_shouldBeSuccess() {
    Pathway pathwayParent = wayMaker.exhaustiveSearch(matrixTests);
    Pathway pathway = wayMaker.mixArrays(pathwayParent, matrixTests, WayMaker.PathMode.RANDOM);
    System.out.println(pathway);
  }
}
