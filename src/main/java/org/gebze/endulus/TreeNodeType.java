/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

/**
 *
 * @author sinan1
 */
public enum TreeNodeType {
 
    LEAF("leaf"), NODE("node");
    private String type;
 
    private TreeNodeType(final String type) {
        this.type = type;
    }
 
    @Override
    public String toString() {
        return type;
    }
 
    public String getType() {
        return type;
    }
}
