/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.util;

/**
 *
 * @author mac
 */
public class OWLHandler {
    
    private OWLHandler() {
    }
    
    public static OWLHandler getInstance() {
        return OWLHandlerHolder.INSTANCE;
    }
    
    private static class OWLHandlerHolder {

        private static final OWLHandler INSTANCE = new OWLHandler();
    }
}
