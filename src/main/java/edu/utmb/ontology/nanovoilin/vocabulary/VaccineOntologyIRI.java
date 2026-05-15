/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.vocabulary;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author mac
 */
public class VaccineOntologyIRI {
    
    final public static String IRI_string = "http://purl.obolibrary.org/obo/vo.owl";
    
    //final public static String class_test = "http://purl.obolibrary.org/obo/VO_0015077";
    
    final public static String class_test = "http://purl.obolibrary.org/obo/VO_0003156";
    
    final public static String violin_vaccine_id = "http://purl.obolibrary.org/obo/VO_0001818";
    
    final public static String term_editor = "http://purl.obolibrary.org/obo/IAO_0000117";
    
    //http://purl.obolibrary.org/obo/IAO_0000115
    final public static String definition = "http://purl.obolibrary.org/obo/IAO_0000115";
    
    final public static String definition_source = "http://purl.obolibrary.org/obo/IAO_0000119";
    
    final public static String has_cross_reference = "http://www.geneontology.org/formats/oboInOwl#hasDbXref";
    
    final public static String dc_creator = "http://purl.org/dc/elements/1.1/creator";
    
    
    static public IRI dc_creator(){
        return IRI.create(dc_creator);
    }
    
    static public IRI IRI(){
        return IRI.create(IRI_string);
    }
    
    static public IRI has_cross_reference(){
        return IRI.create(has_cross_reference);
    }
    
    static public IRI definition(){
        return IRI.create(definition);
    }
    
    static public IRI definition_source(){
        return IRI.create(definition_source);
    }
    
    static public IRI class_test(){
        return IRI.create(class_test);
    }
    
    static public IRI violin_vaccine_id(){
        return IRI.create(violin_vaccine_id);
    }
    
    public static IRI term_editor(){
        return IRI.create(term_editor);
    }
    
}
