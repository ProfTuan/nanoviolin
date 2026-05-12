/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.extraction.ExtractAxioms;
import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author mac
 */
public class NanoVoilin {
    
    private OWLHandler owl_controller = null;
    private ExtractAxioms extractor = null;
    private NanoViolinEncoder encoder = null;
    
    
    public NanoVoilin(){
        if(owl_controller == null) owl_controller = OWLHandler.getInstance();
        
        if(encoder == null) encoder = NanoViolinEncoder.getInstance();
    }
    
    public NanoVoilin(IRI ontology_iri){
        owl_controller = OWLHandler.getInstance();
        owl_controller.init(ontology_iri);
        
        extractor = new ExtractAxioms(owl_controller);
        
        
        
    }
    
    public void setUpExtraction(){
        
    }

    public static void main(String[] args) {
        
        OWLHandler owl_controller = OWLHandler.getInstance();
        
        owl_controller.init(VaccineOntologyIRI.IRI());
       
        //http://purl.obolibrary.org/obo/VO_0015077
        
        
    }
}
