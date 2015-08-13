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
 * @author Ömer
 */
public class SdtpTreeNode extends DefaultTreeNode {
    
    private JSdtpModel sdtpmodel;
    private boolean opened=false;
    private boolean dizin=false;
    private int dizin_id;

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
    public SdtpTreeNode(int dizin_id,Object data) {
        super(data);
        dizin=true;
        this.dizin_id=dizin_id;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isDizin() {
        return dizin;
    }

    public void setIsDizin(boolean isDizin) {
        this.dizin = isDizin;
    }

    public int getDizin_id() {
        return dizin_id;
    }

    public void setDizin_id(int dizin_id) {
        this.dizin_id = dizin_id;
    }
    
    
    
    
}
