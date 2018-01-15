package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ionut.tirlea on 27/11/2017.
 */
public class HistoryBuilder {

    private final String regex = "(([rw][\\d]+\\([a-z]\\))||([ca][\\d]+))+";

    private boolean validate(String history){
        return history.matches(regex);
    }

    public History getHistoryFromLine(String historyAsString){

        if(!validate(historyAsString)){
            System.out.println("History <" + historyAsString + "> is not valid!");
            return null;
        }

        History history = new History();
        Pattern pattern = Pattern.compile("([rw])([\\d])+\\(([a-z])\\)+|([ca])([\\d])+");
        Pattern readWritePattern = Pattern.compile("([rw])([\\d])+\\(([a-z])\\)+");
        Pattern commitAbortPattern = Pattern.compile("([ca])([\\d])+");

        Matcher matcher = pattern.matcher(historyAsString);
        Matcher operationMatcher;

        while (matcher.find()){
            Operation operation = new Operation();
            String operationString = matcher.group(0);
            if(operationString.startsWith("c") || operationString.startsWith("a")){
                operationMatcher = commitAbortPattern.matcher(operationString);
                while (operationMatcher.find()) {
                    operation.setOperationType(OperationType.getOperationType(operationMatcher.group(1)));
                    operation.setTransactionID(Integer.parseInt(operationMatcher.group(2)));
                }
            }else{
                operationMatcher = readWritePattern.matcher(operationString);
                while (operationMatcher.find()) {
                    operation.setOperationType(OperationType.getOperationType(operationMatcher.group(1)));
                    operation.setTransactionID(Integer.parseInt(operationMatcher.group(2)));
                    operation.setVariable(operationMatcher.group(3));
                }
            }
            history.addOperation(operation);
        }
        return history;

    }

}
