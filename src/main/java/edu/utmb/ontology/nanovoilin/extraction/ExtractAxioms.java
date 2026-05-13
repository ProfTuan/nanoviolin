/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.extraction;

import edu.utmb.ontology.nanovoilin.data.ExtractedClassInformation;
import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 *
 * @author mac
 */
public class ExtractAxioms extends ExtractionImplementation {
    
    private OWLOntology ontology = null;
    private OWLDataFactory factory = null;
    private OWLHandler owl_controller = null;
    
    private Set<OWLClass> sub_classes = null;
    private Set<OWLClassExpression> class_expressions = null;
    private Set<OWLAnnotation> class_annotations = null;
    
    public ExtractAxioms(){
        
    }
    
    public ExtractAxioms(OWLHandler owl_controller){
        this.owl_controller = owl_controller;
        this.ontology = owl_controller.getOntology();
        this.factory = owl_controller.getOWLDataFactory();
    }
    
    
    public ExtractedClassInformation extractClassExpressions(IRI class_iri){
        
        
        OWLClass owl_class = owl_controller.getOWLClass(class_iri);
        
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(Collections.singleton(this.ontology));
        
        ontology.subClassAxiomsForSubClass(owl_class).forEach(ax -> ax.getSuperClass().accept(restrictionVisitor));
        
        //Annotations to extract
        //EntitySearcher.getAnnotationObjects(owl_class, ontology).forEach(System.out::println);
        class_annotations = this.extractClassAnnotations(owl_class, ontology);
        
        ExtractedClassInformation eci = this.extractClassInformation(restrictionVisitor);
       
        sub_classes = eci.subclasses;
        class_expressions = eci.class_expressions;
        eci.class_annotations = class_annotations;
        
        return eci;
    }
    
    public ExtractedClassInformation getPackagedInstructions(){
        ExtractedClassInformation eci = new ExtractedClassInformation();
        
        eci.subclasses = sub_classes;
        eci.class_expressions = class_expressions;
        eci.class_annotations = class_annotations;
        
        return eci;
    }
    
    public Set<OWLClass> getSubclasses(){
        
        return this.sub_classes;
    }
    
    public Set<OWLClassExpression> getBasicClassDefinitions(){
        
        
        return this.class_expressions;
    }
    
    public Set<OWLAnnotation> getClassAnnotations(){
        
        return this.class_annotations;
    }
    
    public void test(IRI class_iri){
        
        OWLHandler controller = OWLHandler.getInstance();
        OWLClass owl_class = controller.getOWLClass(class_iri);
        
        OWLOntology ontology = OWLHandler.getInstance().getOntology();
        
        OWLDataFactory factory = OWLHandler.getInstance().getOWLDataFactory();
        
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(Collections.singleton(ontology));
        
        
        ontology.subClassAxiomsForSubClass(owl_class).forEach(ax -> ax.getSuperClass().accept(restrictionVisitor));
        
        //ontology.axioms(owl_class).forEach(ax->ax.accept(restrictionVisitor));
        //Stream<OWLClassAxiom> axioms = ontology.axioms(owl_class);
        //axioms.forEach(System.out::println);
        System.out.println(owl_class.toString());
        
        EntitySearcher.getAnnotationObjects(owl_class, ontology).forEach(System.out::println);
        
        
        Set<OWLClassExpression> processesExpressions = restrictionVisitor.getOWLClassExpression();
        Set<OWLClass> processesClasses = restrictionVisitor.getProcessesClasses();
        
        for(OWLClassExpression oc : processesExpressions){
            System.out.println(oc + " with " + oc.signature().count());
        }
        
        for(OWLClass oc : processesClasses){
            System.out.println(oc);
        }
        
        OWLOntologyManager m = OWLManager.createConcurrentOWLOntologyManager();
        
        //Set<OWLClassExpression> collect = processesClasses.stream().filter(pc -> (pc.signature().count() > 2)).collect(toSet());
        
        //collect.forEach(System.out::println);
        
        try {

            OWLOntology tempOntology = m.createOntology();
            //tempOntology.add(factory.getOWLEquivalentClassesAxiom(
            //        processesClasses.stream().filter(pc -> (pc.signature().count() < 3)).collect(toSet())
            //));
            
            tempOntology.add(factory.getOWLEquivalentClassesAxiom(
                    processesClasses
            ));
            
            TurtleDocumentFormat turtle = new TurtleDocumentFormat();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tempOntology.saveOntology(turtle, new FileOutputStream(new File("testthis.ttl")));


        } catch (OWLOntologyCreationException ex) {
            System.getLogger(ExtractAxioms.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (OWLOntologyStorageException ex) {
            System.getLogger(ExtractAxioms.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (FileNotFoundException ex) {
            System.getLogger(ExtractAxioms.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
        
    }
    
    
    
    
    public static void main(String[] args) {
        
        ExtractAxioms a = new ExtractAxioms();
        
        OWLHandler.getInstance().init(VaccineOntologyIRI.IRI());
        
        a.test(VaccineOntologyIRI.class_test());
        //a.extractAxiomsFromClass(VaccineOntologyIRI.class_test());
        
        
    }

   
    

    
}



