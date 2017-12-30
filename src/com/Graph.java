package com;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ionut.tirlea on 18/11/2017.
 */
public class Graph {

    private String name;
    private List<Node> nodes;

    public Graph(String name){
        this.setName(name);
        this.setNodes(new ArrayList<>());
    }

    public void addNode(Node v){
        getNodes().add(v);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Node getNode(int id){
         Optional<Node> optionalNode = getNodes().stream().filter(node -> node.getId() == id).findFirst();
         if (optionalNode.isPresent()){
             return optionalNode.get();
         }
         return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> vertices) {
        this.nodes = vertices;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "name='" + name + '\'' +
                ", nodes=\n" + getNodesToString() +
                '}';
    }

    private String getNodesToString(){
        String s = "";
        for(Node n: this.getNodes()){
            s += n.toString() + "\n";
        }
        return s;
    }

    public boolean isAcyclic(){

        boolean hasCycles;

        hasCycles = getNodes().stream()
                    .filter( node -> isCyclic(node, new Path()))
                    .findFirst()
                    .isPresent();

        return !hasCycles;

    }

    private boolean isCyclic(Node node, Path path) {

        if (path.contains(node)) {
            return true;
        }

        path.addNode(node);

        for (Node adjacentNode : node.getAdjacentNodes()) {
            if (isCyclic(adjacentNode, path)) {
                return true;
            }
            path.removeNode();
        }

        return false;

    }

    /*
    * Create a node T in the graph for each participating transaction in the schedule (history).
    * For the conflicting operation read_item(X) and write_item(X)
    * If a Transaction Tj executes a read_item (X) after Ti executes a write_item (X), draw an edge from Ti to Tj in the graph.
    * For the conflicting operation write_item(X) and read_item(X)
    * If a Transaction Tj executes a write_item (X) after Ti executes a read_item (X), draw an edge from Ti to Tj in the graph.
    * For the conflicting operation write_item(X) and write_item(X)
    * If a Transaction Tj executes a write_item (X) after Ti executes a write_item (X), draw an edge from Ti to Tj in the graph.
    * The Schedule S is serializable if there is no cycle in the precedence graph.
    * */

    public static Graph getConflictGraph(History h){

        Graph graph = new Graph("conflictGraph" + h.hashCode());

        h.getOperationList().stream()
                .filter(operation -> operation.getOperationType().equals(OperationType.COMMIT))
                .forEach(transaction -> graph.addNode(new Node(transaction.getTransactionID())));

        for(Node node:graph.getNodes()){

            h.getOperationList().stream()
                    .filter(operation -> operation.getTransactionID() == node.getId())
                    .filter(operation -> operation.getOperationType() == OperationType.WRITE)
                    .forEach(
                            operation -> h.getOperationList().stream()
                                    .filter(otherOperation -> otherOperation.getTransactionID() != operation.getTransactionID())
                                    .filter(otherOperation -> h.getOperationList().indexOf(otherOperation) > h.getOperationList().indexOf(operation))
                                    .filter(otherOperation -> !node.getAdjacentNodes().contains(graph.getNode(otherOperation.getTransactionID())))
                                    .filter(otherOperation -> otherOperation.getOperationType() == OperationType.WRITE || otherOperation.getOperationType() == OperationType.READ )
                                    .filter(otherOperation -> otherOperation.getVariable().equals(operation.getVariable()))
                                    .forEach(otherOperation -> node.getAdjacentNodes().add(graph.getNode(otherOperation.getTransactionID()))));

            h.getOperationList().stream()
                    .filter(operation -> operation.getTransactionID() == node.getId())
                    .filter(operation -> operation.getOperationType() == OperationType.READ)
                    .forEach(
                            operation -> h.getOperationList().stream()
                                    .filter(otherOperation -> otherOperation.getTransactionID() != operation.getTransactionID())
                                    .filter(otherOperation -> h.getOperationList().indexOf(otherOperation) > h.getOperationList().indexOf(operation))
                                    .filter(otherOperation -> !node.getAdjacentNodes().contains(graph.getNode(otherOperation.getTransactionID())))
                                    .filter(otherOperation -> otherOperation.getOperationType() == OperationType.WRITE)
                                    .filter(otherOperation -> otherOperation.getVariable().equals(operation.getVariable()))
                                    .forEach(otherOperation -> node.getAdjacentNodes().add(graph.getNode(otherOperation.getTransactionID()))));

        }

        System.out.println("Conflict graph is :\n" + graph.toString());

        return graph;

    }

}
