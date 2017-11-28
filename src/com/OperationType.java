package com;

/**
 * Created by ionut.tirlea on 27/11/2017.
 */
public enum OperationType {
    READ,
    WRITE,
    ABORT,
    COMMIT;

    public static OperationType getOperationType(String operationType) {
        switch (operationType) {
            case "r":
                return READ;
            case "w":
                return WRITE;
            case "c":
                return COMMIT;
            case "a":
                return ABORT;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case READ:
                return "r";
            case ABORT:
                return "a";
            case COMMIT:
                return "c";
            case WRITE:
                return "w";
            default:
                return "null";
        }
    }
}
