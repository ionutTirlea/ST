package com;

import java.util.Collections;
import java.util.Random;

/**
 * Created by ionut.tirlea on 28/11/2017.
 */
public class RandomHistoryGenerator {
    public static History generate(){
        Random random = new Random();
        int noOfTransactions = random.nextInt(Constants.MAX_TRANSACTIONS_NO);
        int noOfStepsPerTransaction;
        History history = new History();
        for(int i=1; i<=noOfTransactions; i++){
            noOfStepsPerTransaction = random.nextInt(Constants.MAX_STEPS_PER_TRANSACTION_NO);
            for(int j=0; j<noOfStepsPerTransaction; j++){
                Operation operation = new Operation();
                operation.setTransactionID(i);
                int o = random.nextInt(2);
                if(o == 0) {
                    operation.setOperationType(OperationType.READ);
                } else {
                    operation.setOperationType(OperationType.WRITE);
                }
                /* TODO generate random variable (which are the values) */
                operation.setVariable("x");
                history.addOperation(operation);
            }
        }
        Collections.shuffle(history.getOperationList());
        for(int i=1; i<=noOfTransactions; i++) {
            history.addOperation(new Operation(i, OperationType.COMMIT));
        }
        return history;
    }
}
