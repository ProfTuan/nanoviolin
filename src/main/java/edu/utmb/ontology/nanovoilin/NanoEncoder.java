/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin;

import org.eclipse.rdf4j.model.IRI;

/**
 *
 * @author mac
 */
public interface NanoEncoder {

    public void encodeAssertionStatement(IRI subject, IRI predicate, String object_value);
    public void encodeAssertionStatement(IRI subject, IRI predicate, IRI object);
    public void encodeProvenanceStatement(IRI predicate, String string_value);
    public void encodePublicationInfoStatement(String iri_predicate, String value_string);
    public void encodeAuthorPublicationInfoStatement(String iri_value);
    public void createKey() ;
    //void signOffNanoViolinPub();
    public void publishNanoViolinAsExport(String file_export_path);
    public void publishNanoViolin();
}
