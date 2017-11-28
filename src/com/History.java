package com;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by ionut.tirlea on 27/11/2017.
 */
public class History {

    private List<Operation> operationList;

    public History(){
        operationList = new ArrayList<>();
    }

    public boolean addOperation(Operation operation){
        return getOperationList().add(operation);
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    @Override
    public String toString() {
        String s = "";
        for(Operation o: operationList){
            s+= o.toString();
        }
        return s;
    }

    public boolean isCSR(){ return false;}

    public boolean isOCSR(){ return false;}

    public boolean isCOCSR(){ return false;}

    /**
     * Checks if the history is valid.
     * A history is valid if has maximum 10 transactions,
     * each of the transaction has maximum 10 steps,
     * and each of transaction is finished by a commit and transaction does not have aborts.
     * @return true if the History is valid, false otherwise.
     */
    public boolean isValid(){

        /* TODO is a transaction valid if contains only commits? commits should be always the last operation of transaction/last operation of history */

        long transactionsNo = operationList.stream().filter(distinctByKey(Operation::getTransactionID)).count();
        Map<Integer, Long> transactionSteps = operationList.stream().collect(Collectors.groupingBy(Operation::getTransactionID, Collectors.counting()));
        long maximumStepsNo = Collections.max(transactionSteps.entrySet(), Map.Entry.comparingByValue()).getValue();
        long distinctCommitsNo = operationList.stream().filter(operation -> operation.getOperationType().equals(OperationType.COMMIT)).filter(distinctByKey(Operation::getTransactionID)).count();
        long abortsNo =  operationList.stream().filter(operation -> operation.getOperationType().equals(OperationType.ABORT)).count();

        return transactionsNo <= Constants.MAX_TRANSACTIONS_NO && maximumStepsNo <= Constants.MAX_STEPS_PER_TRANSACTION_NO && distinctCommitsNo == transactionsNo && abortsNo == 0;

    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
