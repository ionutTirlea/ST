package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ionut.tirlea on 26/11/2017.
 */
public class Path implements Cloneable {

    private List<Node> path;

    public Path(){
        path = new ArrayList<>();
    }

    public void addNode(Node node){
        path.add(node);
    }

    public void removeNode(){ path.remove(path.size() - 1); }

    public int getLength(){
        return path.size() - 1;
    }

    public boolean contains(Node n){
        return path.contains(n);
    }

    public int indexOf(Node n){
        return path.indexOf(n);
    }

    @Override
    public String toString() {
        String s = "";
        for(Node n: path){
            s += s.isEmpty() ? n.getId() : "," + n.getId();
        }
        String special = "";
        return  "[" + s + "]" + special;
    }

    public Path clone(){
        Path clonedPath = new Path();
        for(Node n: path){
            clonedPath.path.add(n);
        }
        return clonedPath;
    }
}
