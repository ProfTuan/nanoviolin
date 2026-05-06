/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.extraction;

import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

/**
 *
 * @author mac
 */
public abstract class ExtractionImplementation implements STARExtraction {
    
    private SyntacticLocalityModuleExtractor SLE;
    
    private Set<OWLEntity>signatures = new HashSet<OWLEntity>();

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
    
    
}
