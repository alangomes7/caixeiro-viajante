package com.operationalresearch.travelingsalesman.bussiness;

import com.operationalresearch.travelingsalesman.model.Matrix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * This class contains all the support operations for the project.
 *
 * @version 1.0
 */
public class Utils {

  /**
   * Makes the database source: a file with matrix.
   *
   * @param database path to salve the file.
   * @param matrixSize size/order of matrix.
   */
  public void createDatabase(File database, int matrixSize) throws IOException {
    // create matrix
    String[][] matrix = new String[matrixSize][matrixSize];
    for (int i = 0; i < matrixSize; i++) {
      for (int j = 0; j < matrixSize; j++) {
        if (i == j) {
          matrix[i][j] = "" + 0 + " ";
        } else {
          matrix[i][j] = "" + randomNumber(1, 9) + " ";
        }
        if (j == matrixSize - 1) {
          matrix[i][j] += "\n";
        }
      }
    }
    // save matrix in String
    String contentFile = "" + matrixSize + "\n";
    for (int i = 0; i < matrixSize; i++) {
      String contentLine = Arrays.toString(matrix[i]);
      contentLine = contentLine.replaceAll("\\[", "").replaceAll("\\]", "");
      contentLine = contentLine.replaceAll(",", "");
      contentFile += contentLine;
    }
    // save database file
    stringToFile(contentFile.split("\n"), database);
  }

  /**
   * Save a stringList into a file.
   *
   * @param stringList the list of lines to be saved in a file.
   * @param fileOutput the output file to save the lines.
   * @throws IOException if something with the file happens.
   */
  public void stringToFile(String[] stringList, File fileOutput) throws IOException {
    try (FileOutputStream fileStream = new FileOutputStream(fileOutput, true);
        OutputStreamWriter writer = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8)) {
      for (String line : stringList) {
        line = line.strip() + "\n";
        writer.write(line);
      }
    }
  }

  /**
   * Convert file to string.
   *
   * @param fileSource file to be converted into string.
   * @return converted string from file.
   */
  public Matrix fileToMatrix(File fileSource) {
    try {
      String content = Files.readString(Paths.get(fileSource.getAbsolutePath()));
      String[] contentArray = content.split("\n");
      int matrixSize = Integer.parseInt(contentArray[0].replaceAll("\\s+", ""));
      return new Matrix(contentArray, matrixSize);
    } catch (IOException | NumberFormatException e) {
      String error = e.getMessage();
      String[] empty = {};
      return new Matrix(empty, 0);
    }
  }

  /**
   * Creates a random nunber in the range given.
   *
   * @param minimum down limit of range.
   * @param maximum up limit of range.
   * @return the random number in range;
   */
  public int randomNumber(int minimum, int maximum) {
    return (int) ((Math.random() * (maximum - minimum)) + minimum);
  }
}
