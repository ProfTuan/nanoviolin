/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.extraction;

import edu.utmb.ontology.nanovoilin.IRI.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author mac
 */
public class ExtractAxioms extends ExtractionImplementation {
    
    public ExtractAxioms(){
        
    }
    
    
    
    public void extractAxiomsFromClass(IRI class_iri){
        
        OWLHandler controller = OWLHandler.getInstance();
        OWLClass owl_class = controller.getOWLClass(class_iri);
        
        
        //controller.getClassSignature(owl_class);
        
        Set<OWLAxiom> extracted_results = this.extractClass(owl_class);
        
        extracted_results.forEach(System.out::println);
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
        
        
        
        Set<OWLClassExpression> processesClasses = restrictionVisitor.getOWLClassExpression();
        
        for(OWLClassExpression oc : processesClasses){
            //System.out.println(oc + " with " + oc.signature().count());
            //if(oc.signature().count() < 3) processesClasses.remove(oc);
        }
        
        OWLOntologyManager m = OWLManager.createConcurrentOWLOntologyManager();
        
        Set<OWLClassExpression> collect = processesClasses.stream().filter(pc -> (pc.signature().count() > 2)).collect(toSet());
        
        collect.forEach(System.out::println);
        
        try {

            OWLOntology tempOntology = m.createOntology();
            tempOntology.add(factory.getOWLEquivalentClassesAxiom(
                    processesClasses.stream().filter(pc -> (pc.signature().count() < 3)).collect(toSet())
            ));
            TurtleDocumentFormat turtle = new TurtleDocumentFormat();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tempOntology.saveOntology(turtle, baos);
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
    
    
    
    //borrowed from Github OWLAPI
    private static class RestrictionVisitor implements OWLClassExpressionVisitor {

        private final Set<OWLClass> processedClasses;
        private final Set<OWLOntology> onts;
        private Set<OWLClassExpression> oce;
        
        
        RestrictionVisitor(Set<OWLOntology> onts) {
            processedClasses = new HashSet<OWLClass>();
            this.onts = onts;
            
            oce = new HashSet<OWLClassExpression>();
        }

        @Override
        public void visit(OWLClass ce) {
            if (!processedClasses.contains(ce)) {
                // If we are processing inherited restrictions then we
                // recursively visit named supers. Note that we need to keep
                // track of the classes that we have processed so that we don't
                // get caught out by cycles in the taxonomy
                processedClasses.add(ce);
                for (OWLOntology ont : onts) {
                    ont.subClassAxiomsForSubClass(ce)
                        .forEach(ax -> ax.getSuperClass().accept(this));
                }
            }
        }

        @Override
        public void visit(OWLObjectSomeValuesFrom ce) {
            // This method gets called when a class expression is an existential
            // (someValuesFrom) restriction and it asks us to visit it
            
            //System.out.println(ce);
            
            //ce.components().forEach(System.out::println);
            
            oce.add(ce);
        }
        
        public Set<OWLClass> getProcessesClasses(){
            return processedClasses;
        }
        
        public Set<OWLClassExpression> getOWLClassExpression(){
            return oce;
        }
    }
    
    
}



