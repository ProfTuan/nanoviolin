/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

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
    
    
    public NanoViolin(){
        if(owl_controller == null) owl_controller = OWLHandler.getInstance();
        
        if(encoder == null) encoder = NanoViolinEncoder.getInstance();
    }
    
    public NanoViolin(IRI ontology_iri){
        owl_controller = OWLHandler.getInstance();
        owl_controller.init(ontology_iri);
        
        extractor = new ExtractAxioms(owl_controller);
        
        
        
    }
    
    public void retrieveAllVaccineIRIs(){
        
        //capture all that have vaccine violin id
        
        
        OWLAnnotationProperty violin_id_annotation = owl_controller.getOWLAnnotationProperty(VaccineOntologyIRI.violin_vaccine_id());
        
        Set<OWLClass> allClasses = owl_controller.getAllClasses();
        //System.out.println(allClasses.size());
        vaccine_entities = new HashSet<OWLClass>();
        
        for(OWLClass oc : allClasses){
            
            Stream<OWLAnnotation> annotations = EntitySearcher.getAnnotations(oc, owl_controller.getOntology(), violin_id_annotation);
            if (annotations.count()>0) vaccine_entities.add(oc);
            
            
        }
        
       
    }
    
    public void setUpExtraction(){
        
    }

    public static void main(String[] args) {
        
        //OWLHandler owl_controller = OWLHandler.getInstance();
        
        //owl_controller.init(VaccineOntologyIRI.IRI());
       
        NanoViolin nv = new NanoViolin(VaccineOntologyIRI.IRI());
        
        nv.retrieveAllVaccineIRIs();
        
        
        //http://purl.obolibrary.org/obo/VO_0015077
        
        
    }
}
