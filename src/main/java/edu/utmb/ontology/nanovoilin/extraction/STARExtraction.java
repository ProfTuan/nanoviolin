/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.extraction;

import java.util.Set;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 *
 * @author mac
 */
public interface STARExtraction {
    
    public Set<OWLAxiom> extractClass(OWLClass owl_class);
    public Set<OWLAxiom> extractClasses(Set <OWLClass> owl_classes);
    
    public Set<OWLAxiom> extractInstance(OWLIndividual individual);
    public Set<OWLAxiom> extractInstances(Set <OWLIndividual> individuals);
    
}
