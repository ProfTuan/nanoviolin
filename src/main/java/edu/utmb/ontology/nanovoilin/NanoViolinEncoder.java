/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.ontology.nanovoilin;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import net.trustyuri.TrustyUriException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.nanopub.Nanopub;
import org.nanopub.NanopubAlreadyFinalizedException;
import org.nanopub.NanopubCreator;
import org.nanopub.NanopubUtils;
import org.nanopub.extra.security.SignNanopub;
import org.nanopub.extra.security.TransformContext;
import org.nanopub.extra.server.PublishNanopub;

/**
 *
 * @author mac
 */
public class NanoViolinEncoder {
    
    
    final ValueFactory simple_value = SimpleValueFactory.getInstance();
    private NanopubCreator creator = null;
    private IRI nanoViolinIRI = null;
    private Nanopub nanoViolinPub = null;
    private Nanopub signing = null;
    
    private NanoViolinEncoder() {
    }
    
    public void instantiateNewNanoViolinPub(String iri_string){
        
        
        
        try {
            
            creator = new NanopubCreator(true);
            
            nanoViolinIRI = simple_value.createIRI(iri_string);
            
        } catch (NanopubAlreadyFinalizedException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
    public void writeAssertionStatement(){
        
    }
    
    public void writeProvenanceStatement(){
        
    }
    
    public void writePublicationInfoStatement(){
        
    }
    
    public void signOffNanoViolinPub(){
        
        try {
            
            signing = SignNanopub.signAndTransform(nanoViolinPub, TransformContext.makeDefault());
            
            NanopubUtils.writeToStream(signing, System.err, RDFFormat.TRIG);
            
        } catch (TrustyUriException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (InvalidKeyException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SignatureException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public void publishNanoViolinPubTestServer(){
        
        try {
            PublishNanopub.publishToTestServer(signing);
        } catch (IOException ex) {
            System.getLogger(NanoViolinEncoder.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public static NanoViolinEncoder getInstance() {
        return NanoViolinEncoderHolder.INSTANCE;
    }
    
    private static class NanoViolinEncoderHolder {

        private static final NanoViolinEncoder INSTANCE = new NanoViolinEncoder();
    }
}
