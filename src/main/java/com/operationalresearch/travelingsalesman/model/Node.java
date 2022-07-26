package com.operationalresearch.travelingsalesman.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Node model. Represents a node inside of Pathway(solution). */
@Data
@AllArgsConstructor
public class Node {
  private int from;
  private int to;
  private int cost;
}
