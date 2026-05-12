/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.PROV;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.nanopub.NanopubAlreadyFinalizedException;
import org.nanopub.NanopubCreator;

/**
 *
 * @author mac
 */
public class NanoViolinEncoder extends NanoEncoderImplementation {
    

    private NanoViolinEncoder() {
        
    }

    public void instantiateNewNanoViolinPub(String iri_string){
 
        try {
            
            creator = new NanopubCreator(true);
            
            nanoViolinIRI = value_factory.createIRI(iri_string);
            
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
    public void writeAssertionStatement(RDF predicate, String string_value){
        
        if (creator == null){
            
        }
        //creator.addAssertionStatement(statement);
    }
    
    public void writeTypeOfStatement(String iri_thing_string,String iri_type_string){
        
        IRI type_iri = value_factory.createIRI(iri_type_string);
        IRI subject_iri = value_factory.createIRI(iri_thing_string);
        
        
        encodeAssertionStatement(subject_iri, RDF.TYPE, type_iri);
        
        
    }
    
    public void writeLabelStatement(String iri_subject, String value){
        IRI subject = value_factory.createIRI(iri_subject);
        IRI value_object = value_factory.createIRI(value);
        
        encodeAssertionStatement(subject, RDFS.LABEL, value_object);
    }
    

    public void writeAttributedByStatement(String label){
        
        
        encodeProvenanceStatement(PROV.WAS_ATTRIBUTED_TO, label);
    }
    
    public void writeTermEditorStatement(String value){
        
        IRI term_editor_iri = value_factory.createIRI(VaccineOntologyIRI.term_editor.toString());
        
        encodeProvenanceStatement(term_editor_iri, value);
        
    }
    
    public void writeCrossReferenceStatement(String value){
        IRI x_ref = value_factory.createIRI(VaccineOntologyIRI.has_cross_reference().toString());
        
        encodeProvenanceStatement(x_ref, value);
    }
    
    public void writeDefinitionSource(String value){
        IRI IAO_def_source = value_factory.createIRI(VaccineOntologyIRI.definition().toString());
        
        encodeProvenanceStatement(IAO_def_source, value);
    }
    
    public void writeDefinitionStatement(String value){
        IRI IAO_definition = value_factory.createIRI(VaccineOntologyIRI.definition_source().toString());
        
        encodeProvenanceStatement(IAO_definition, value);
    }
    
   public void writeViolinVaccineID(String value){
       IRI violin_id = value_factory.createIRI(VaccineOntologyIRI.violin_vaccine_id);
       
       encodeProvenanceStatement(violin_id, value);
   }


    public static NanoViolinEncoder getInstance() {
        return NanoViolinEncoderHolder.INSTANCE;
    }
    
    private static class NanoViolinEncoderHolder {

        private static final NanoViolinEncoder INSTANCE = new NanoViolinEncoder();
    }
    
    public static void main(String[] args) {
        
        //System.out.println(VaccineOntologyIRI.term_editor().toString());
        
    }
}
