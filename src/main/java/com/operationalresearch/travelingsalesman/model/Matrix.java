package com.operationalresearch.travelingsalesman.model;

import java.util.ArrayList;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class models the matrix of adjancentes. It is represents the points and their costs to move
 * to another point.
 *
 * @author - @alangomes7
 */
@Data
@NoArgsConstructor
public class Matrix {
  private int size;
  private ArrayList<int[]> elements;

  /**
   * Parse a String array into a matrix.
   *
   * @param strings matrix lines.
   * @param matrixSize matrix size/order.
   */
  public Matrix(String[] strings, int matrixSize) {
    size = matrixSize;
    elements = new ArrayList<>();
    for (String string : strings) {
      String[] line = string.split(" ");
      if (line.length == 1) {
        continue;
      }
      int[] matrixLine = new int[size];
      int indexMatrix = -1;
      for (int i = 0; i < line.length; i++) {
        if (line[i] == " " || line[i].isEmpty()) {
          continue;
        } else {
          indexMatrix++;
        }
        Integer itemInteger = Integer.parseInt(line[i].replaceAll("\\s+", ""));
        matrixLine[indexMatrix] = itemInteger;
      }
      elements.add(matrixLine);
    }
  }

  /**
   * Prepare a string that represents a matrix.
   *
   * @return the string matrix to print.
   */
  public String toString() {
    StringBuilder matrixString = new StringBuilder("");
    matrixString.append("" + size + "\n");
    for (int[] element : elements) {
      for (int i = 0; i < element.length; i++) {
        matrixString.append("" + element[i] + " ");
      }
      matrixString.append("\n");
    }
    return matrixString.toString();
  }
}
