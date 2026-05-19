/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.data.ExtractedClassInformation;
import edu.utmb.ontology.nanovoilin.db.SQLiteDBConnection;
import edu.utmb.ontology.nanovoilin.extraction.ExtractAxioms;
import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.semanticweb.owlapi.model.ClassExpressionType;
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


    private String orcid = "https://orcid.org/0000-0003-4333-1857";
    
    private OWLHandler owl_controller = null;
    private ExtractAxioms extractor = null;
    private NanoViolinEncoder encoder = null;
    private Set<OWLClass> vaccine_entities = null;
    private Map<OWLClass, ExtractedClassInformation> vaccine_class_dataset = null;
    private Map<String, Integer> uncovered = null;
    
    private String database_path_name = "temp_nano.db";
    
    public NanoViolin(){
        if(owl_controller == null) owl_controller = OWLHandler.getInstance();
        
        if(encoder == null) encoder = NanoViolinEncoder.getInstance();
    }
    
    public NanoViolin(IRI ontology_iri){
        owl_controller = OWLHandler.getInstance();
        owl_controller.init(ontology_iri);
        
        extractor = new ExtractAxioms(owl_controller);

    }
    
    public void printUncovered(){
        
        for(var e : uncovered.entrySet()){
            
            System.out.println(e.getKey() + " - " + e.getValue());
            
        }
        
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
        
        uncovered = new HashMap<>();
        
        SQLiteDBConnection sqlite = SQLiteDBConnection.getInstance();
        
        long timestamp = new Date().getTime();
        
        String connection = database_path_name.replace(".db", "_" +timestamp +".db");
        
        System.out.println(connection);
        
        sqlite.initConnection(connection);
        
        OWLAnnotationProperty rdfsLabel = owl_controller.getOWLDataFactory().getRDFSLabel();
        
        for(var i : vaccine_class_dataset.entrySet())
        {
            
            OWLClass vax = i.getKey(); 
            //System.out.println(vax.toString());
            ExtractedClassInformation vax_info = i.getValue();
            
            NanoViolinEncoder nve = NanoViolinEncoder.getInstance();
            nve.instantiateNewNanoViolinPub(vax.getIRI().toString());
            
            vax_info.subclasses.forEach(o->{
                nve.writeTypeOfStatement(vax.getIRI().toString(), o.getIRI().toString());
            });
            
            
            
            
            vax_info.class_annotations.forEach(a->{
                
                if(a.getProperty().isLabel()){
                    
                   nve.writeLabelStatement(
                           vax.getIRI().toString(), 
                           a.getValue().asLiteral().get().getLiteral());
                   
                }
                else if(a.getProperty().getIRI().equals(VaccineOntologyIRI.has_cross_reference())){
                    
                    
                    nve.writeCrossReferenceStatement(a.getValue().asLiteral().get().getLiteral());
                }
                else if(a.getProperty().getIRI().equals(VaccineOntologyIRI.definition_source())){
                    
                    
                    nve.writeDefinitionSource(a.getValue().asLiteral().get().getLiteral());
                    
                }
                else if(a.getProperty().getIRI().equals(VaccineOntologyIRI.definition())){
                    nve.writeDefinitionStatement(a.getValue().asLiteral().get().getLiteral());
                }
                else if(a.getProperty().getIRI().equals(VaccineOntologyIRI.violin_vaccine_id())){
                    nve.writeViolinVaccineID(a.getValue().asLiteral().get().getLiteral());
                }
                else if(a.getProperty().getIRI().equals(VaccineOntologyIRI.term_editor())){
                    nve.writeTermEditorStatement(a.getValue().asLiteral().get().getLiteral());
                    
                }
                else{
                    
                    
                    if(uncovered.containsKey(a.getProperty().toString())){
                        
                        Integer current = uncovered.get(a.getProperty().toString());
                        current = Integer.valueOf(current.intValue()+1);
                        
                        uncovered.put(a.getProperty().toString(), current);
                        
                    }
                    else{
                        //Integer.valueOf(1);
                        uncovered.put(a.getProperty().toString(), Integer.valueOf(1));
                        
                    }
                }
                
                
            });
         
            
            vax_info.class_expressions.forEach(owe->{
             
                
                //TODO: Need to support more complex class expressions.
                
                if(owe.signature().count() == 2){
                    
                    
                    
                    
                    if(owe.getClassExpressionType().equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM)){
                        
                        String property_string = owe.objectPropertiesInSignature().findFirst().get().toStringID();
                        //System.out.println("string id: "+ owe.objectPropertiesInSignature().findFirst().get().toStringID());
                        if(owe.getClassesInSignature().size()==1){
                            
                            String object = owe.getClassesInSignature().stream().findFirst().get().toStringID();
                            
                            nve.writeAssertionStatementForSubject(property_string, object);
                            
                        }
                        else if(owe.getIndividualsInSignature().size() == 1){
                            
                            String object = owe.getIndividualsInSignature().stream().findFirst().get().toStringID();
                            
                            nve.writeAssertionStatementForSubject(property_string, object);
                            
                        }
                        else{
                           
                            
                            new NanoViolinException("Found a specific issue with a class expression: " + owe);
                        }
                                
                        
                    }
                    else{

                        new NanoViolinException("There exist triple whose class expression is not SOME VALUES FROM");
                    }
                    
                
                    
                }
                
                else if(owe.signature().count() > 2){
                    
                    
                    if(uncovered.containsKey("complex class expressions")){
                        Integer value = uncovered.get("complex class expressions");
                        
                        value = Integer.valueOf(value.intValue() + 1);
                        
                        uncovered.put("complex class expressions", value);
                        
                    }
                    else{
                        uncovered.put("complex class expressions", Integer.valueOf(1));
                    }
                    
                }
                
                
            });
            
            //Publication Info
            
            nve.writeAuthorOfThisNanopublication(orcid);
            
            
            //Dump into temporary database
            sqlite.insertViolinNanoPub(nve.nanoViolinIRI.toString(), owl_controller.getOWLClassLabel(vax), nve.getAssertionContent(), nve.getProvenanceContent(), 
                    nve.getPublicationInfoContent());
            
            nve.publishNanoViolinAsExport("nano-export");
            
        }
        
        
    }
    
    

    public static void main(String[] args) {
        

        NanoViolin nv = new NanoViolin(VaccineOntologyIRI.IRI());
        
        Set<OWLClass> retrieveAllVaccineIRIs = nv.retrieveAllVaccineIRIs();
        
        nv.batchVaccineNanopubCreation(retrieveAllVaccineIRIs);

        nv.vaccine_NanoViolinPublication();
        
        nv.printUncovered();
        
    }
}
