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

    public Graph conflictGraph;

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

    public boolean isCSR(){

        if(conflictGraph == null){
            conflictGraph = Graph.getConflictGraph(this);
        }

        return conflictGraph.isAcyclic();

    }

    /* TODO [ionut.tirlea] implementation */
    public boolean isOCSR(){

        if(conflictGraph == null){
            conflictGraph = Graph.getConflictGraph(this);
        }

        if(!isCSR())
            return false;

        boolean isOCSR = true;

        return isOCSR;
    }

    /*
    *
    * Schedule (or history) s is commit order preserving conflict serializable if
    * for all ti, tj  transactopns(s): if there are p  ti, q  tj with (p,q)  conf(s) then ci < cj.
    * COCSR denotes the class of all schedules with this property.
    * */

    public boolean isCOCSR(){

        if(conflictGraph == null){
            conflictGraph = Graph.getConflictGraph(this);
        }

        if(!isOCSR())
            return false;

        Map<Integer, Operation > conflictOperations;

        conflictOperations =
                this.getOperationList().stream()
                        .filter(operation -> operation.getOperationType() == OperationType.COMMIT)
                        .collect(Collectors.toMap(o -> o.getTransactionID(), o -> o));

        for(Node conflictNode: conflictGraph.getNodes()){
            for(Node adjacentConflictNode: conflictNode.getAdjacentNodes()){
               if(this.getOperationList().indexOf(conflictOperations.get(conflictNode.getId())) > this.getOperationList().indexOf(conflictOperations.get(adjacentConflictNode.getId()))){
                   return false;
               }
            }
        }

        return true;

    }
    /**
     * Checks if the history is valid.
     * A history is valid if has maximum 10 transactions,
     * each of the transaction has maximum 10 steps,
     * and each of transaction is finished by a commit and transaction does not have aborts.
     * @return true if the History is valid, false otherwise.
     */
    public boolean isValid(){

        long transactionsNo = operationList.stream().filter(distinctByKey(Operation::getTransactionID)).count();
        Map<Integer, Long> transactionSteps = operationList.stream().collect(Collectors.groupingBy(Operation::getTransactionID, Collectors.counting()));
        long maximumStepsNo = Collections.max(transactionSteps.entrySet(), Map.Entry.comparingByValue()).getValue();
        long distinctCommitsNo = operationList.stream().filter(operation -> operation.getOperationType().equals(OperationType.COMMIT)).filter(distinctByKey(Operation::getTransactionID)).count();
        long abortsNo =  operationList.stream().filter(operation -> operation.getOperationType().equals(OperationType.ABORT)).count();

        boolean ok = transactionsNo <= Constants.MAX_TRANSACTIONS_NO && maximumStepsNo <= Constants.MAX_STEPS_PER_TRANSACTION_NO && distinctCommitsNo == transactionsNo && abortsNo == 0;
        if(ok){
            for(Integer transactionID: transactionSteps.keySet()){
                if(!checkCommits(transactionID)) return false;
            }
        }
        return true;
    }

    private boolean checkCommits(int transactionID){

        List<Operation> transaction =  operationList.stream().filter(operation -> operation.getTransactionID() == transactionID).collect(Collectors.toList());

        if(transaction.size() == 1)
            return false;

        if (transaction.stream().filter(operation -> operation.getOperationType().equals(OperationType.COMMIT)).count() != 1)
            return false;

        if(transaction.get(transaction.size() - 1).getOperationType() != OperationType.COMMIT)
            return false;

       return true;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
