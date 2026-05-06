/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.IRI;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author mac
 */
public class VaccineOntologyIRI {
    
    final public static String IRI_string = "http://purl.obolibrary.org/obo/vo.owl";
    
    //final public static String class_test = "http://purl.obolibrary.org/obo/VO_0015077";
    
    final public static String class_test = "http://purl.obolibrary.org/obo/VO_0003156";
    
    static public IRI IRI(){
        return IRI.create(IRI_string);
    }
    
    static public IRI class_test(){
        return IRI.create(class_test);
    }
    
}
