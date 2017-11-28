package com;

public class Main {

    public static void main(String[] args) {
        String historyAsString = "r1(x)w1(x)c1";
        HistoryBuilder historyBuilder = new HistoryBuilder();
        History history = historyBuilder.getHistoryFromLine(historyAsString);
        if(history!=null) {
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
    }
}
