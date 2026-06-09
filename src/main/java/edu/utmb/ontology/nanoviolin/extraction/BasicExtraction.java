/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.utmb.ontology.nanoviolin.extraction;

import edu.utmb.ontology.nanoviolin.data.ExtractedClassInformation;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author mac
 */
public interface BasicExtraction {
    //public void extractClassExpressionsFromClass(IRI class_iri);
    
    public Set<OWLAnnotation> extractClassAnnotations(OWLClass owl_class, OWLOntology owl_ontology);
    public ExtractedClassInformation extractClassInformation(OWLClass owl_class, OWLOntology owl_ontology);
    public ExtractedClassInformation extractClassInformation(ExtractionImplementation.RestrictionVisitor restrictionVisitor);
    
}
