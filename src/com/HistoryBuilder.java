package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ionut.tirlea on 27/11/2017.
 */
public class HistoryBuilder {

    private final String regex = "([rw][\\d]+\\([a-z]\\)||[ca][\\d]+)+";

    private boolean validate(String history){
        return history.matches(regex);
    }

    public History getHistoryFromLine(String historyAsString){

        if(!validate(historyAsString)){
            System.out.println("History <" + historyAsString + "> is not valid!");
            return null;
        }

        History history = new History();
        Pattern pattern = Pattern.compile("([rw])([\\d])+\\(([a-z])\\)");
        Matcher matcher = pattern.matcher(historyAsString);

        while (matcher.find()){
            Operation operation = new Operation();
            operation.setOperationType(OperationType.getOperationType(matcher.group(1)));
            operation.setTransactionID(Integer.parseInt(matcher.group(2)));
            operation.setVariable(matcher.group(3));
            history.addOperation(operation);
        }

        pattern = Pattern.compile("([ca])([\\d])+");
        matcher = pattern.matcher(historyAsString);

        while (matcher.find()){
            Operation operation = new Operation();
            operation.setOperationType(OperationType.getOperationType(matcher.group(1)));
            operation.setTransactionID(Integer.parseInt(matcher.group(2)));
            history.addOperation(operation);
        }

        return history;

    }

}
