package de.shiyar.utilities.poeditor.pojo;

import lombok.Data;

/**
 * Generic class to test the methods equals().
 *
 * @author daoud
 * on Aug, 2019
 */
@Data
public class POEditorJson {
    private String term;
    private String definition;
    private String context;
    private String term_plural;
    private String reference;
    private String comment;

}
