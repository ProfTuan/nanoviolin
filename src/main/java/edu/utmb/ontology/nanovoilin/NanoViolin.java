/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.data.ExtractedClassInformation;
import edu.utmb.ontology.nanovoilin.extraction.ExtractAxioms;
import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    private Map<OWLClass, ExtractedClassInformation> vaccine_class_dataset = null;
    
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

        vaccine_class_dataset = new HashMap<>();
        
        for (OWLClass vax : vaxxes) {

            ExtractedClassInformation vaccine_class_information = extractor.extractClassExpressions(vax.getIRI());
            vaccine_class_dataset.put(vax, vaccine_class_information);
            //vaccine_class_dataset.add(vaccine_class_information);
        }

    }
    
    public void vaccine_NanoViolinPublication(){
        
        
        for(var i : vaccine_class_dataset.entrySet())
        {
            
            OWLClass vax = i.getKey();
            ExtractedClassInformation vax_info = i.getValue();
            
            NanoViolinEncoder nve = NanoViolinEncoder.getInstance();
            nve.instantiateNewNanoViolinPub(vax.getIRI().toString());
            
            vax_info.subclasses.forEach(o->{
                nve.writeTypeOfStatement(vax.getIRI().toString(), o.getIRI().toString());
            });
        }
        
        
    }
    
    private void transform(){
        
    }
    
    public void setUpExtraction(){
        
    }

    public static void main(String[] args) {
        

        NanoViolin nv = new NanoViolin(VaccineOntologyIRI.IRI());
        
        Set<OWLClass> retrieveAllVaccineIRIs = nv.retrieveAllVaccineIRIs();
        
        nv.batchVaccineNanopubCreation(retrieveAllVaccineIRIs);

        nv.vaccine_NanoViolinPublication();
        
    }
}
