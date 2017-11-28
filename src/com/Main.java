package com;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        History history;
        if(args.length !=0 && args[0].equals("randomGeneration")){
            history = RandomHistoryGenerator.generate();
            System.out.println("Generating random history!");
            System.out.println(history.toString());
        } else {
            System.out.print("Reading History = ");
            String historyAsString = s.nextLine();
            HistoryBuilder historyBuilder = new HistoryBuilder();
            history = historyBuilder.getHistoryFromLine(historyAsString);
        }

        if(history != null) {
            if(history.isValid()){
                System.out.println("The history is valid!");
                if(history.isCSR())  System.out.println("The history is CSR!"); else System.out.println("The history is not CSR!");
                if(history.isOCSR())  System.out.println("The history is OCSR!"); else System.out.println("The history is not OCSR!");
                if(history.isCOCSR())  System.out.println("The history is COCSR!"); else System.out.println("The history is not COCSR!");
            }
            else {
                System.out.println("The history is not valid!");
            }
        }
        else{
            System.out.println("An error occurred in parsing the input history!");
        }

        System.out.println("Close console to exit!");
        while(s.hasNext()){

        }

    }
}
