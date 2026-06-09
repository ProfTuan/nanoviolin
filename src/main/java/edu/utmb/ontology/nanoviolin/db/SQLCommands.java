/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.ontology.nanoviolin.db;

/**
 *
 * @author mac
 */
public class SQLCommands {
    
    public static String vaccine_nanopub_table = "vaccinenanopub";
    
    public static String create_vaccine_nanopub_table = "create table if not exists vaccinenanopub\n" +
"(\n" +
"    id                     TEXT not null\n" +
"        constraint vaccinenanopub_pk\n" +
"            primary key,\n" +
"    label                  text,\n" +
"    assertion              TEXT,\n" +
"    provenance             text,\n" +
"    publicationinformation text\n" +
");";
    
    
    public static String clear_table = "DELETE FROM table_name";
}
