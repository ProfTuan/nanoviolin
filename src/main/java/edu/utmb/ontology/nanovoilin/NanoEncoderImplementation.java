/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import net.trustyuri.TrustyUriException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.base.CoreDatatype;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubAlreadyFinalizedException;
import org.nanopub.NanopubCreator;
import org.nanopub.NanopubUtils;
import org.nanopub.extra.security.MakeKeys;
import org.nanopub.extra.security.SignNanopub;
import org.nanopub.extra.security.SignatureAlgorithm;
import org.nanopub.extra.security.TransformContext;
import org.nanopub.extra.server.PublishNanopub;

/**
 *
 * @author mac
 */
public abstract class NanoEncoderImplementation implements NanoEncoder {

    final ValueFactory value_factory = SimpleValueFactory.getInstance();
    protected NanopubCreator creator = null;
    protected IRI nanoViolinIRI = null;
    protected Nanopub nanoViolinPub = null;
    protected Nanopub signing = null;

    public NanoEncoderImplementation() {

    }

    public void encodeAssertionStatement(IRI subject, IRI predicate, String object_value){
        
        Literal value = value_factory.createLiteral(object_value, CoreDatatype.XSD.STRING);
        
        try {
            creator.addAssertionStatement(subject, predicate, value);
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoEncoderImplementation.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void encodeAssertionStatement(IRI subject, IRI predicate, IRI object) {

        try {
            
            creator.addAssertionStatement(subject, predicate, object);
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void encodeProvenanceStatement(IRI predicate, String string_value) {

        Literal value_object = value_factory.createLiteral(string_value, CoreDatatype.XSD.STRING);

        try {
            creator.addProvenanceStatement(predicate, value_object);
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
    
    public void encodePublicationInfoStatement(String iri_predicate, String value_string){
        
        Literal value = value_factory.createLiteral(value_string);
        
        IRI predicate = value_factory.createIRI(iri_predicate);
        
        try {
            creator.addPubinfoStatement(predicate, value);
            //creator.addPubinfoStatement(predicate, predicate);
            
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoEncoderImplementation.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public void encodeAuthorPublicationInfoStatement(String iri_value){
        
        IRI predicate = value_factory.createIRI(VaccineOntologyIRI.dc_creator);
        
        IRI value = value_factory.createIRI(iri_value);
        
        
        try {
            creator.addPubinfoStatement(predicate, value);
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoEncoderImplementation.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    

    public void createKey() {
        try {
            MakeKeys.make("~/.nanopub/id", SignatureAlgorithm.RSA);
        } catch (IOException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public void signOffNanoViolinPub() {

        try {

            nanoViolinPub = creator.finalizeNanopub(true);
            
            signing = SignNanopub.signAndTransform(nanoViolinPub, TransformContext.makeDefault());

            NanopubUtils.writeToStream(signing, System.err, RDFFormat.TRIG);

        } catch (TrustyUriException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (InvalidKeyException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SignatureException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (MalformedNanopubException ex) {
            System.getLogger(NanoEncoderImplementation.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoEncoderImplementation.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public void publishNanoViolinAsExport(String file_export_path) {
        
        this.signOffNanoViolinPub();
        
        try {
            NanopubUtils.writeToStream(signing, new FileOutputStream(new File(file_export_path)), RDFFormat.TRIG);
        } catch (FileNotFoundException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void publishNanoViolinPubForTestServer() {

        try {
            PublishNanopub.publishToTestServer(signing);
        } catch (IOException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

}
