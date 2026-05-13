/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.data.ExtractedClassInformation;
import edu.utmb.ontology.nanovoilin.extraction.ExtractAxioms;
import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 *
 * @author mac
 */
public class NanoViolin {
    
    private OWLHandler owl_controller = null;
    private ExtractAxioms extractor = null;
    private NanoViolinEncoder encoder = null;
    private Set<OWLClass> vaccine_entities = null;
    private Set<ExtractedClassInformation> vaccine_class_information = null;
    
    public NanoViolin(){
        if(owl_controller == null) owl_controller = OWLHandler.getInstance();
        
        if(encoder == null) encoder = NanoViolinEncoder.getInstance();
    }
    
    public NanoViolin(IRI ontology_iri){
        owl_controller = OWLHandler.getInstance();
        owl_controller.init(ontology_iri);
        
        extractor = new ExtractAxioms(owl_controller);

    }
    
    public Set<OWLClass> retrieveAllVaccineIRIs(){
        
        //capture all that have vaccine violin id

        OWLAnnotationProperty violin_id_annotation = owl_controller.getOWLAnnotationProperty(VaccineOntologyIRI.violin_vaccine_id());
        
        Set<OWLClass> allClasses = owl_controller.getAllClasses();
       
        vaccine_entities = new HashSet<OWLClass>();
        
        for(OWLClass oc : allClasses){
            
            Stream<OWLAnnotation> annotations = EntitySearcher.getAnnotations(oc, owl_controller.getOntology(), violin_id_annotation);
            if (annotations.count()>0) vaccine_entities.add(oc);
            
            
        }
        
       return vaccine_entities;
    }
    
    public void batchVaccineNanopubCreation(Set<OWLClass> vaxxes) {

        for (OWLClass vax : vaxxes) {

            ExtractedClassInformation vaccine_class_information = extractor.extractClassExpressions(vax.getIRI());
            
        }

    }
    
    public void setUpExtraction(){
        
    }

    public static void main(String[] args) {
        

        NanoViolin nv = new NanoViolin(VaccineOntologyIRI.IRI());
        
        Set<OWLClass> retrieveAllVaccineIRIs = nv.retrieveAllVaccineIRIs();
        
        nv.batchVaccineNanopubCreation(retrieveAllVaccineIRIs);


    }
}
