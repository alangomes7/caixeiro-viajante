package com.operationalresearch.travelingsalesman;

import com.operationalresearch.travelingsalesman.bussiness.Utils;
import com.operationalresearch.travelingsalesman.bussiness.WayMaker;
import com.operationalresearch.travelingsalesman.model.Matrix;
import com.operationalresearch.travelingsalesman.model.Pathway;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class App {
    private static Utils utils;
    private static String pathTestResources;
    private static  File matrixFile;
    private static WayMaker wayMaker;
    private static int[] sizeMatrix;

    private static void initialize(){
        utils = new Utils();
        pathTestResources = Paths.get("src", "test", "/resources/").toFile().getAbsolutePath() + "/";
        matrixFile = matrixFile = new File(pathTestResources + "matrixTest.txt");
        wayMaker = new WayMaker();
        sizeMatrix = new int[]{100, 200, 400, 800,1600, 3200};
    }

    private static void createsDatabase() throws IOException {
        for(int size : sizeMatrix){
            File matrixFile = new File(pathTestResources + "matrixTest" + ""+size+".txt");
            utils.createDatabase(matrixFile,size);
        }
    }

    private static void writeResults(int roundN, int matrixSize ,String method, long timeExecutionInSeconds,int totalCost) throws IOException {
        String[] header = new String[]{"round, method, time, total cost"};
        String[] line = new String[]{""+roundN +","+ method+","+timeExecutionInSeconds+","+totalCost};
        File resultsFile = new File(pathTestResources + "results-"+matrixSize+".csv");
        if(!resultsFile.exists()) {
            utils.stringToFile(header, resultsFile);
        }
        utils.stringToFile(line,resultsFile);
    }

    private static void solutionsGenerated(Matrix matrix, int roundN,int matrixSize, int mode) throws IOException {
        Pathway pathway = new Pathway();
        String method = "";
        long initialTime = 0;
        long finalTime = 0;
        long time = 0;
        switch (mode){
            case 1:
                initialTime = System.currentTimeMillis();
                pathway = wayMaker.exhaustiveSearch(matrix);
                finalTime = System.currentTimeMillis();
                method = "exhaustive";
                break;
            case 2:
                initialTime = System.currentTimeMillis();
                pathway = wayMaker.exhaustiveSearch(matrix);
                pathway = wayMaker.mixArrays(pathway,matrix, WayMaker.PathMode.RANDOM);
                finalTime = System.currentTimeMillis();
                method = "ER";
                break;
            case 3:
                initialTime = System.currentTimeMillis();
                pathway = wayMaker.randomSearch(matrix);
                finalTime = System.currentTimeMillis();
                method = "random";
                break;
            case 4:
                initialTime = System.currentTimeMillis();
                pathway = wayMaker.randomSearch(matrix);
                pathway = wayMaker.mixArrays(pathway,matrix, WayMaker.PathMode.RANDOM);
                finalTime = System.currentTimeMillis();
                method = "RE";
                break;
            default:
                System.out.println("Method invalid");
        }
        time = (finalTime - initialTime);
        writeResults(roundN,matrixSize, method,time,pathway.getTotalCost());
    }

    private static void testSolution(int roundN,int matrixSize, String pathMatrix, int heuristic) throws IOException {
        File matrixFile = new File(pathMatrix);
        Matrix matrix = utils.fileToMatrix(matrixFile);
        solutionsGenerated(matrix, roundN, matrixSize, heuristic);
    }

    public static void main(String[] args) throws IOException {
        // initialize variables
        initialize();
        // create database
        //createsDatabase();
        // read matrix from file;
        Matrix matrix = utils.fileToMatrix(matrixFile);
        // creates solutions
        int round = 1;
        while(round <= 5) {
            testSolution(round, 100, "src/test/resources/matrixTest100.txt", 4);
            round++;
        }
        System.out.println("--- Finished:  ---");
    }

}
