/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.util;

import java.io.File;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author mac
 */
public class OWLHandler {
    
    private OWLOntology ontology;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    
    private OWLHandler() {
    }
    
    public void init (IRI iri_ontology){
        try {
            
            manager = OWLManager.createConcurrentOWLOntologyManager();
            ontology = manager.loadOntology(iri_ontology);
            factory = manager.getOWLDataFactory();
            
        } catch (OWLOntologyCreationException ex) {
            System.getLogger(OWLHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public void init (File file_ontology){
        
        try {
            manager= OWLManager.createConcurrentOWLOntologyManager();
            ontology = manager.loadOntologyFromOntologyDocument(file_ontology);
            factory = manager.getOWLDataFactory();
        } catch (OWLOntologyCreationException ex) {
            System.getLogger(OWLHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public OWLOntology getOntology(){
        return ontology;
    }
    
    public OWLOntologyManager getOWLOntologyManager(){
        return manager;
    }
    
    public OWLDataFactory getOWLDataFactory(){
        
        return factory;
    }
    
    public OWLClass getOWLClass(IRI iri_class){
        
        OWLClass owlClass = factory.getOWLClass(iri_class);
        return owlClass;
    }
    
    /*public void getClassSignature(OWLClass owl_class){
        
        Set<OWLEntity> signature = owl_class.getSignature();
        
        signature.forEach(System.out::println);
        
    }*/
    
    public static OWLHandler getInstance() {
        return OWLHandlerHolder.INSTANCE;
    }
    
    private static class OWLHandlerHolder {

        private static final OWLHandler INSTANCE = new OWLHandler();
    }
}
