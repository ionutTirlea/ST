package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ionut.tirlea on 27/11/2017.
 */
public class Transaction {

    private List<Operation> operationList;

    public Transaction(){
        this.setOperationList(new ArrayList<>());
    }

    public boolean addOperation(Operation operation){
        return getOperationList().add(operation);
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    private void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    /**
     * Checks if the transaction is valid.
     * A transaction is valid if has maximum 10 steps and is finished by a commit.
     * @return true if the History is valid, false otherwise.
     */
    public boolean isValid(){
        return operationList!= null && !operationList.isEmpty() && operationList.size() <= 10 && operationList.get(operationList.size() - 1).getOperationType().equals(OperationType.COMMIT);
    }
}
