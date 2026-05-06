/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.IRI.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.extraction.STARExtraction;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;

/**
 *
 * @author mac
 */
public class NanoVoilin {
    
    private OWLHandler owl_controller = null;
    
    public NanoVoilin(){
        
    }

    public static void main(String[] args) {
        
        OWLHandler owl_controller = OWLHandler.getInstance();
        
        owl_controller.init(VaccineOntologyIRI.IRI());
       
        //http://purl.obolibrary.org/obo/VO_0015077
        
        
    }
}
