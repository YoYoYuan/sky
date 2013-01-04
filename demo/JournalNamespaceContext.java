/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.lucene.demo;

import java.util.Iterator;
import javax.xml.*;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author ZHAO XIN
 */
public class JournalNamespaceContext implements NamespaceContext {

    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null Prefix");
        }
        else if ("pre".equals(prefix)) {
            return "http://www.nssd.org/journal";
        }
        
        return XMLConstants.NULL_NS_URI;
    }

    @Override
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
