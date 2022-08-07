package com.operationalresearch.travelingsalesman.bussiness;

import com.operationalresearch.travelingsalesman.model.Matrix;
import com.operationalresearch.travelingsalesman.model.Node;
import com.operationalresearch.travelingsalesman.model.Pathway;
import java.util.ArrayList;

/**
 * This class is the core of project. Here we have all local searches.
 *
 * @author - @alangomes7
 */
public class WayMaker {

  public enum PathMode {
    EXHAUSTIVE,
    RANDOM
  }

  /**
   * Makes a way using exhaustive search. The initial node is always zero.
   *
   * @param matrix the data to find a way.
   */
  public Pathway exhaustiveSearch(Matrix matrix) {
    Pathway pathway = new Pathway();
    pathway.setPath(new ArrayList<>());
    ArrayList<Integer> nodeFromList = new ArrayList<>();
    // makes way
    int currentlyNode = 0;
    int[] costs = matrix.getElements().get(currentlyNode);
    while (nodeFromList.size() < matrix.getSize() - 1) {
      int[] minCostInfo = minimumCost(costs, 9);
      int minCost = minCostInfo[0];
      int indexMinCost = minCostInfo[1];
      // filter if node is already added
      if (containsNodeEnd(nodeFromList, indexMinCost)) {
        // invalidate path
        costs = invalidatePath(costs, indexMinCost);
        continue;
      } else {
        nodeFromList.add(currentlyNode);
      }
      Node node = new Node(currentlyNode, indexMinCost, minCost);
      // finally add the node in path
      pathway.getPath().add(node);
      // update for the new search
      currentlyNode = indexMinCost;
      costs = matrix.getElements().get(currentlyNode);
    }
    Node lastNode = finalNode(matrix, pathway.getPath());
    pathway.getPath().add(lastNode);
    pathway.setTotalCost(calculateCost(pathway.getPath()));
    return pathway;
  }

  /**
   * Makes a way using sequential search. The initial node is random.
   *
   * @param matrix the data to find a way.
   */
  public Pathway randomSearch(Matrix matrix) {
    Utils utils = new Utils();
    Pathway pathway = new Pathway();
    pathway.setPath(new ArrayList<>());
    ArrayList<Integer> nodeSelected = new ArrayList<>();
    // makes way
    int currentlyNode = utils.randomNumber(0, matrix.getSize());
    int cost;
    while (nodeSelected.size() < matrix.getSize() - 1) {
      int nextNodeIndex = utils.randomNumber(0, matrix.getSize());
      // filter if node index is already added
      if (containsNodeEnd(nodeSelected, nextNodeIndex)) {
        // skip selected node and select again
        continue;
      } else {
        cost = matrix.getElements().get(currentlyNode)[nextNodeIndex];
        if (cost == 0) {
          continue;
        }
        nodeSelected.add(currentlyNode);
      }
      Node node = new Node(currentlyNode, nextNodeIndex, cost);
      // finally add the node in path
      pathway.getPath().add(node);
      // update for the new search
      currentlyNode = nextNodeIndex;
    }
    Node lastNode = finalNode(matrix, pathway.getPath());
    pathway.getPath().add(lastNode);
    pathway.setTotalCost(calculateCost(pathway.getPath()));
    return pathway;
  }

  /**
   * Test if array[int] elements contains item.
   *
   * @param elements array to scam.
   * @param item element to verify existence.
   * @return true if elements exists or false otherwise.
   */
  public boolean containsNodeEnd(ArrayList<Integer> elements, int item) {
    for (int element : elements) {
      if (element == item) {
        return true;
      }
    }
    return false;
  }

  /**
   * Search for the minimum cost.
   *
   * @param costs array of cost for scam.
   * @param maxCost the maxCost for filter.
   * @return String with 'minCost,index'.
   */
  public int[] minimumCost(int[] costs, int maxCost) {
    int indexMinCost = 0;
    int minCost = maxCost + 1;
    for (int i = 0; i < costs.length; i++) {
      int item = costs[i];
      if (item == 0) {
        continue;
      }
      if (item < minCost) {
        minCost = item;
        indexMinCost = i;
      }
    }
    return new int[] {minCost, indexMinCost};
  }

  /**
   * Replace the minimum cost with zero.
   *
   * @param costs array of costs.
   * @param costIndex index of cost to be updated.
   * @return updated array of costs.
   */
  public int[] invalidatePath(int[] costs, int costIndex) {
    costs[costIndex] = 0;
    return costs;
  }

  /**
   * Finalizes the last node element.
   *
   * @param matrix the matrix to get cost.
   * @param nodes the node list to build last node.
   * @return the last node to return to initial point.
   */
  public Node finalNode(Matrix matrix, ArrayList<Node> nodes) {
    Node initialNode = nodes.get(0);
    Node lastNode = nodes.get(nodes.size() - 1);
    int cost = matrix.getElements().get(lastNode.getTo())[initialNode.getFrom()];
    return new Node(lastNode.getTo(), initialNode.getFrom(), cost);
  }

  /**
   * Calculates the cost of the nodes.
   *
   * @param nodes - nodes that shape the way.
   * @return - the cost of walk the given path.
   */
  public int calculateCost(ArrayList<Node> nodes) {
    int cost = 0;
    for (Node node : nodes) {
      cost += node.getCost();
    }
    return cost;
  }

  /**
   * Gets previous nodes from a given pathway.
   *
   * @param nodesFrom - The arraylist with the previous node.
   * @return - The list with the previous node.
   */
  private ArrayList<Integer> getNodesFrom(ArrayList<Node> nodesFrom) {
    ArrayList<Integer> fromList = new ArrayList<>();
    for (Node nodeItem : nodesFrom) {
      fromList.add(nodeItem.getFrom());
    }
    return fromList;
  }

  /**
   * Creates new pathways using combinations of random and exhaustive solutions.
   *
   * @param parent the parent to provide the first part of way.
   * @param matrix the database to build the pathway.
   * @param mode the mode to use to complete the pathway.
   * @return the mixed and complete pathway.
   */
  public Pathway mixArrays(Pathway parent, Matrix matrix, PathMode mode) {
    ArrayList<Node> child = new ArrayList<>();
    // receives the first part from mother
    for (int i = 0; i < parent.getPath().size() / 2; i++) {
      child.add(parent.getPath().get(i));
    }
    if (mode == PathMode.EXHAUSTIVE) {
      return completesWayExhaustive(child, matrix);
    }
    return completesWayRandom(child, matrix);
  }

  private Pathway completesWayExhaustive(ArrayList<Node> halfWay, Matrix matrix) {
    Pathway pathway = new Pathway();
    pathway.setPath(halfWay);
    ArrayList<Integer> nodeFromList = getNodesFrom(halfWay);
    // completes way
    int currentlyNode = halfWay.get(halfWay.size() - 1).getTo();
    int[] costs = matrix.getElements().get(currentlyNode);
    while (nodeFromList.size() < matrix.getSize() - 1) {
      int[] minCostInfo = minimumCost(costs, 9);
      int minCost = minCostInfo[0];
      int indexMinCost = minCostInfo[1];
      // filter if node is already added
      if (containsNodeEnd(nodeFromList, indexMinCost)) {
        // invalidate path
        costs = invalidatePath(costs, indexMinCost);
        continue;
      } else {
        nodeFromList.add(currentlyNode);
      }
      Node node = new Node(currentlyNode, indexMinCost, minCost);
      // finally add the node in path
      pathway.getPath().add(node);
      // update for the new search
      currentlyNode = indexMinCost;
      costs = matrix.getElements().get(currentlyNode);
    }
    Node lastNode = finalNode(matrix, pathway.getPath());
    pathway.getPath().add(lastNode);
    pathway.setTotalCost(calculateCost(pathway.getPath()));
    return pathway;
  }

  private Pathway completesWayRandom(ArrayList<Node> halfWay, Matrix matrix) {
    Utils utils = new Utils();
    Pathway pathway = new Pathway();
    pathway.setPath(halfWay);
    ArrayList<Integer> nodeSelected = getNodesFrom(halfWay);
    // completes way
    int currentlyNode = halfWay.get(halfWay.size() - 1).getTo();
    int cost;
    while (nodeSelected.size() < matrix.getSize() - 1) {
      int nextNodeIndex = utils.randomNumber(0, matrix.getSize());
      // filter if node index is already added
      if (containsNodeEnd(nodeSelected, nextNodeIndex)) {
        // skip selected node and select again
        continue;
      } else {
        cost = matrix.getElements().get(currentlyNode)[nextNodeIndex];
        if (cost == 0) {
          continue;
        }
        nodeSelected.add(currentlyNode);
      }
      Node node = new Node(currentlyNode, nextNodeIndex, cost);
      // finally add the node in path
      pathway.getPath().add(node);
      // update for the new search
      currentlyNode = nextNodeIndex;
    }
    Node lastNode = finalNode(matrix, pathway.getPath());
    pathway.getPath().add(lastNode);
    pathway.setTotalCost(calculateCost(pathway.getPath()));
    return pathway;
  }
}
