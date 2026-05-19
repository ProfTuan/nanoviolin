/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.util;

import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import java.io.File;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;

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
    
    public Set<OWLClass> getAllClasses(){
        
        Set<OWLClass> all_classes = ontology.classesInSignature().collect(toSet());
        
        
        
        return all_classes;
        
    }
    
    public String getOWLClassLabel(OWLClass oc){
        
        StringBuilder label = new StringBuilder();
        
        Stream<OWLAnnotation> annotations = EntitySearcher.getAnnotations(oc, ontology, factory.getRDFSLabel());
        
        annotations.forEach(anno->{
            
            OWLAnnotationValue value = anno.getValue();
            if(value instanceof OWLLiteral){
                
                String literal = value.asLiteral().get().getLiteral();
                label.append(literal);
                
            }
            
        });
        
        return label.toString();
    }
    
    public OWLAnnotationProperty getOWLAnnotationProperty(IRI iri){
        
        OWLAnnotationProperty owlAnnotationProperty = factory.getOWLAnnotationProperty(iri);
        
        return owlAnnotationProperty;
    }
    
    public Map<String, String> getNamespaces(){
        
        Map<String, String> prefixName2PrefixMap = null;
        OWLDocumentFormat ontologyFormat = manager.getOntologyFormat(ontology);
        
        if(ontologyFormat.isPrefixOWLDocumentFormat()){
            prefixName2PrefixMap = ontologyFormat.asPrefixOWLDocumentFormat().getPrefixName2PrefixMap();
        }

        return prefixName2PrefixMap;
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
    
    
    public static void main(String[] args) {
        
       
        
    }
}
