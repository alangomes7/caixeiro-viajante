package com.operationalresearch.travelingsalesman.model;

import java.util.ArrayList;
import lombok.Data;

/** The path model. Represents a solution. */
@Data
public class Pathway {
  private int totalCost;
  private ArrayList<Node> path;
}
