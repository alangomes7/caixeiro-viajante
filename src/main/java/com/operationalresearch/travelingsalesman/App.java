package com.operationalresearch.travelingsalesman;

import com.operationalresearch.travelingsalesman.bussiness.Utils;
import com.operationalresearch.travelingsalesman.bussiness.WayMaker;
import com.operationalresearch.travelingsalesman.model.Matrix;
import com.operationalresearch.travelingsalesman.model.Pathway;
import com.operationalresearch.travelingsalesman.view.Shower;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

/**
 * Executes the program. Here we find the starts of application.
 *
 * @author @alangomes7
 */
@Slf4j
public class App {
  private static Utils utils;
  private static File pathTestResources;
  private static WayMaker wayMaker;

  /** Heuristic of search used. */
  private enum HEURISTIC {
    RANDOM,
    EXHAUSTIVE,
    RADONEXHAUSTIVE,
    EXHAUSTIVERANDOM
  }

  /** Initialize main variables. Sometimes this method is also called setup. */
  private static void initialize() {
    utils = new Utils();
    pathTestResources = Paths.get("src", "test", "/resources/").toFile();
    wayMaker = new WayMaker();
  }

  /**
   * Generate a solution using the indicated heuristic.
   *
   * @param matrix - The matrix to find a solution (way).
   * @param roundN - The number of execution of solution.
   * @param matrixSize - The matrix size.
   * @param heuristic - The type of heuristic used tp create a solution.
   * @throws IOException - If some error occurs with output file.
   */
  private static void generateSolution(
      Matrix matrix, int roundN, int matrixSize, HEURISTIC heuristic) throws IOException {
    Pathway pathway = new Pathway();
    long initialTime = 0;
    long finalTime = 0;
    String method = "";
    switch (heuristic) {
      case EXHAUSTIVE:
        initialTime = System.currentTimeMillis();
        pathway = wayMaker.exhaustiveSearch(matrix);
        finalTime = System.currentTimeMillis();
        method = heuristic.toString();
        break;
      case EXHAUSTIVERANDOM:
        initialTime = System.currentTimeMillis();
        pathway = wayMaker.exhaustiveSearch(matrix);
        pathway = wayMaker.mixArrays(pathway, matrix, WayMaker.PathMode.RANDOM);
        finalTime = System.currentTimeMillis();
        method = heuristic.toString();
        break;
      case RANDOM:
        initialTime = System.currentTimeMillis();
        pathway = wayMaker.randomSearch(matrix);
        finalTime = System.currentTimeMillis();
        method = heuristic.toString();
        break;
      case RADONEXHAUSTIVE:
        initialTime = System.currentTimeMillis();
        pathway = wayMaker.randomSearch(matrix);
        pathway = wayMaker.mixArrays(pathway, matrix, WayMaker.PathMode.RANDOM);
        finalTime = System.currentTimeMillis();
        method = heuristic.toString();
        break;
      default:
        System.out.println("Method invalid");
    }
    long timeSpent = (finalTime - initialTime);
    Shower.writeResults(
        roundN, matrixSize, method, timeSpent, pathway.getTotalCost(), pathTestResources);
  }

  /**
   * Find a solution.
   *
   * @param roundN - The execution's number. This method can be run multiple times automatically.
   * @param matrixSize - The matrix's size.
   * @param matrixSource - The source matrix file.
   * @param heuristic - The method used to find a solution.
   * @throws IOException - If some error occurs with source file.
   */
  private static void findSolution(
      int roundN, int matrixSize, File matrixSource, HEURISTIC heuristic) throws IOException {
    File matrixFile = new File(matrixSource.getAbsolutePath());
    Matrix matrix = utils.fileToMatrix(matrixFile);
    generateSolution(matrix, roundN, matrixSize, heuristic);
  }

  /**
   * The main class.
   *
   * @param args - arguments to run the application.
   */
  public static void main(String[] args) {
    log.info("--- Started  ---");
    // initialize variables
    initialize();
    // user input methods
    int maxRounds = 10;
    int matrixSize = 10;
    HEURISTIC heuristic = HEURISTIC.RADONEXHAUSTIVE;
    File matrixSource = new File("src/test/resources/matrixTest" + matrixSize + ".txt");
    try {
      // create database
      utils.createDatabase(matrixSource, matrixSize);
      // creates solutions
      int round = 1;
      while (round <= maxRounds) {
        findSolution(round, matrixSize, matrixSource, heuristic);
        round++;
      }
    } catch (IOException e) {
      log.error("error: " + e.getMessage());
      throw new RuntimeException(e);
    }
    log.info("--- Finished  ---");
  }
}
