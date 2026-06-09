/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanoviolin.extraction;

import edu.utmb.ontology.nanoviolin.data.ExtractedClassInformation;
import edu.utmb.ontology.nanoviolin.util.OWLHandler;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

/**
 *
 * @author mac
 */
public abstract class ExtractionImplementation implements STARExtraction, BasicExtraction {
    
    private SyntacticLocalityModuleExtractor SLE;
    
    private Set<OWLEntity>signatures = new HashSet<OWLEntity>();

    @Override
    public Set<OWLAnnotation> extractClassAnnotations(OWLClass owl_class, OWLOntology owl_ontology) {
        Set<OWLAnnotation> collect = EntitySearcher.getAnnotationObjects(owl_class, owl_ontology).collect(toSet());
        
        return collect;
    }

    @Override
    public ExtractedClassInformation extractClassInformation(OWLClass owl_class, OWLOntology owl_ontology){
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(Collections.singleton(owl_ontology));
        
        
        
        ExtractedClassInformation extracted = new ExtractedClassInformation();
        extracted.class_expressions.addAll(restrictionVisitor.getOWLClassExpression());
        extracted.subclasses.addAll(restrictionVisitor.getProcessesClasses());
        
        return extracted;
    }

    @Override
    public ExtractedClassInformation extractClassInformation(RestrictionVisitor restrictionVisitor) {
        
        ExtractedClassInformation extracted = new ExtractedClassInformation();
        
        extracted.class_expressions.addAll(restrictionVisitor.getOWLClassExpression());
        extracted.subclasses.addAll(restrictionVisitor.getProcessesClasses());
        
        return extracted;
        
    }

    
    
    @Override
    public Set<OWLAxiom> extractClass(OWLClass owl_class) {
        
        OWLHandler owl = OWLHandler.getInstance();
        
        OWLOntologyManager manager = owl.getOWLOntologyManager();
        OWLOntology ontology = owl.getOntology();
        
        SLE = new SyntacticLocalityModuleExtractor(manager, ontology, ModuleType.BOT);
        
        signatures.clear();
        signatures.add(owl_class);
        
        Set<OWLAxiom> extract_results = SLE.extract(signatures);
        
        
        return extract_results;
        
    }

    @Override
    public Set<OWLAxiom> extractClasses(Set<OWLClass> owl_classes) {
        
        
        OWLHandler owl = OWLHandler.getInstance();
        
        OWLOntologyManager manager = owl.getOWLOntologyManager();
        OWLOntology ontology = owl.getOntology();
        
        SLE = new SyntacticLocalityModuleExtractor(manager, ontology, ModuleType.STAR);
        
        signatures.clear();
        signatures.addAll(owl_classes);
        
        Set<OWLAxiom> extracted_results = SLE.extract(signatures);
        
        return extracted_results;
    }

    @Override
    public Set<OWLAxiom> extractInstance(OWLIndividual individual) {
        
        OWLHandler owl = OWLHandler.getInstance();
        
        OWLOntologyManager manager = owl.getOWLOntologyManager();
        OWLOntology ontology = owl.getOntology();
        
        SLE = new SyntacticLocalityModuleExtractor(manager, ontology, ModuleType.STAR);
        
        signatures.clear();
        signatures.add((OWLEntity) individual);
        
        Set<OWLAxiom> extracted_results = SLE.extract(signatures);
        
        return extracted_results;
    }

    @Override
    public Set<OWLAxiom> extractInstances(Set<OWLIndividual> individuals) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    //borrowed from Github OWLAPI
    public class RestrictionVisitor implements OWLClassExpressionVisitor {

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
               
                if(!ce.isOWLThing()) processedClasses.add(ce);
                for (OWLOntology ont : onts) {
                    ont.subClassAxiomsForSubClass(ce).forEach(ax -> ax.getSuperClass().accept(this));
                    
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

        @Override
        public void visit(OWLObjectIntersectionOf ce) {
            OWLClassExpressionVisitor.super.visit(ce); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }
        
        public Set<OWLClass> getProcessesClasses(){
            return processedClasses;
        }
        
        public Set<OWLClassExpression> getOWLClassExpression(){
            return oce;
        }
    }
    
    
}
