/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.data;

import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 *
 * @author mac
 */
public class ExtractedClassInformation {
    
    public Set<OWLClass> subclasses = null;
    public Set<OWLClassExpression> class_expressions= null;
    public Set<OWLAnnotation> class_annotations = null;
    
    public ExtractedClassInformation(){
        subclasses = new HashSet<OWLClass>();
        class_expressions = new HashSet<OWLClassExpression>();
        class_annotations = new HashSet<OWLAnnotation>();
    }
}
