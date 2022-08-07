package com.operationalresearch.travelingsalesman.view;

import com.operationalresearch.travelingsalesman.bussiness.Utils;
import java.io.File;
import java.io.IOException;

/**
 * Makes all output methods data.
 *
 * @author - @alangomes7
 */
public class Shower {

  /** A simple private constructor. */
  private Shower() {}

  /**
   * Makes the main output: a file description about the test.
   *
   * @param currentlyTime - The timeStamp.
   * @param matrixSize - The size of matrix.
   * @param method - The type of method used to solve the problem.
   * @param timeExecutionInSeconds - The time spent to solve the problem.
   * @param totalCost - The total cost of solution.
   * @throws IOException - If some error occurs with output data.
   */
  public static void writeResults(
      long currentlyTime,
      int matrixSize,
      String method,
      long timeExecutionInSeconds,
      int totalCost,
      File pathTestResources)
      throws IOException {
    Utils utils = new Utils();
    String[] header =
        new String[] {
          "time stamp - date, time stamp - time, method, time execution - milliseconds, total cost"
        };
    String timeStamp = utils.timeStampToDate(currentlyTime);
    String[] line =
        new String[] {
          "" + timeStamp + "," + method + "," + timeExecutionInSeconds + "," + totalCost
        };
    File resultsFile =
        new File(pathTestResources.getAbsolutePath() + "/results-" + matrixSize + ".csv");
    if (!resultsFile.exists()) {
      utils.stringToFile(header, resultsFile);
    }
    utils.stringToFile(line, resultsFile);
  }
}
