package com;

/**
 * Created by ionut.tirlea on 27/11/2017.
 */
public class Operation {

    private OperationType operationType;
    private int transactionID;
    private String variable;

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        String variableToString = operationType.equals(OperationType.ABORT) || operationType.equals(OperationType.COMMIT) ? "" : "(" + variable + ")";
        return operationType.toString() + transactionID + variableToString ;
    }
}
