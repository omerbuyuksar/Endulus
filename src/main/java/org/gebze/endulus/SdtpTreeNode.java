/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author mypc
 */
public class SdtpTreeNode extends DefaultTreeNode {
    
    private JSdtpModel sdtpmodel;
    private boolean opened=false;

    public JSdtpModel getSdtpmodel() {
        return sdtpmodel;
    }

    public void setSdtpmodel(JSdtpModel sdtpmodel) {
        this.sdtpmodel = sdtpmodel;
    }


    public SdtpTreeNode(String type, Object data, TreeNode parent) {
        super(type, data, parent);
    }

    public SdtpTreeNode(Object data, TreeNode parent) {
        super(data, parent);
    }  

    public SdtpTreeNode(JSdtpModel sdtpmodel,Object data) {
        super(data);
        this.sdtpmodel = sdtpmodel;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
    
    
}
