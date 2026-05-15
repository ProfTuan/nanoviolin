/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.utmb.ontology.nanovoilin;

import edu.utmb.ontology.nanovoilin.data.ExtractedClassInformation;
import edu.utmb.ontology.nanovoilin.extraction.ExtractAxioms;
import edu.utmb.ontology.nanovoilin.vocabulary.VaccineOntologyIRI;
import edu.utmb.ontology.nanovoilin.util.OWLHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.search.EntitySearcher;
import static org.semanticweb.owlapi.vocab.Namespaces.RDF;
import static org.semanticweb.owlapi.vocab.Namespaces.RDFS;

/**
 *
 * @author mac
 */
public class NanoViolin {
    
    private OWLHandler owl_controller = null;
    private ExtractAxioms extractor = null;
    private NanoViolinEncoder encoder = null;
    private Set<OWLClass> vaccine_entities = null;
    private Map<OWLClass, ExtractedClassInformation> vaccine_class_dataset = null;
    
    private String database_path_name = "~";
    
    public NanoViolin(){
        if(owl_controller == null) owl_controller = OWLHandler.getInstance();
        
        if(encoder == null) encoder = NanoViolinEncoder.getInstance();
    }
    
    public NanoViolin(IRI ontology_iri){
        owl_controller = OWLHandler.getInstance();
        owl_controller.init(ontology_iri);
        
        extractor = new ExtractAxioms(owl_controller);

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
        
        //OWLAnnotationProperty rdfs_label = owl_controller.getOWLAnnotationProperty();
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
                    
                    //System.out.println("\t"+a);
                }
                
                
            });
         
            
            vax_info.class_expressions.forEach(owe->{
             
                
                //TODO: Need to support more complex class expressions.
                
                if(owe.signature().count() == 2){
                    
                    
                    
                    if(owe.getClassExpressionType().equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM)){
                        
                        String property_string = owe.objectPropertiesInSignature().findFirst().get().toString();
                        
                        if(owe.getClassesInSignature().size()==1){
                            
                            String object = owe.getClassesInSignature().stream().findFirst().get().toString();
                            
                            nve.writeAssertionStatementForSubject(property_string, object);
                            
                        }
                        else if(owe.getIndividualsInSignature().size() == 1){
                            
                            String object = owe.getIndividualsInSignature().stream().findFirst().get().toString();
                            
                            nve.writeAssertionStatementForSubject(property_string, object);
                            
                        }
                        else{
                           
                            
                            new NanoViolinException("Found a specific issue with a class expression: " + owe);
                        }
                                
                        
                    }
                    
                    else{
                        
                        
                        new NanoViolinException("There exist triple whose class expression is not SOME VALUES FROM");
                    }
                    
                
                    //nve.writeAssertionStatementForSubject(predicate, object);
                    
                }
                
                
            });
            
            System.out.println("\n\n---------------------\n\n");
            System.out.println(nve.getContents());
            
            //TODO: export or publish
            
            //nve.signOffNanoViolinPub();
            //nve.publishNanoViolinAsExport("/");
        }
        
        
    }
    
    private void transform(){
        
    }
    
    public void setUpExtraction(){
        
    }

    public static void main(String[] args) {
        

        NanoViolin nv = new NanoViolin(VaccineOntologyIRI.IRI());
        
        Set<OWLClass> retrieveAllVaccineIRIs = nv.retrieveAllVaccineIRIs();
        
        nv.batchVaccineNanopubCreation(retrieveAllVaccineIRIs);

        nv.vaccine_NanoViolinPublication();
        
    }
}
