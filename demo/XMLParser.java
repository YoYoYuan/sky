/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.lucene.demo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.NodeList;
//import org.apache.lucene.document.Document;

/**
 *
 * @author ZHAO XIN
 */
public class XMLParser {

    /**
     * @param args the command line arguments
     */
    
    private String docPath = null;
    private DocumentBuilderFactory domFactory;
    private DocumentBuilder builder;
    private Document doc;
    private XPathFactory factory;
    private XPath xpath;
    private String suffixOfPath = "X_0000.xml";
    private String paperID;
        
    //constructor of XMLParser
    public XMLParser(String path){
        
        System.out.println(".txt docPath:" + path);
        
        try {
            this.docPath = path;
            domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //get paper ID of the pdf
        paperID = path.substring(path.length() - 8, path.length() - 4);
        
        //convert the doc path to xml file path       
        docPath 
                = docPath.replace(docPath.substring(docPath.length() - 10, docPath.length()), suffixOfPath);
        
        System.out.println(".xml docPath:" + docPath);
        System.out.println("PaperID:" + paperID);
        
        try {
            doc = builder.parse(docPath);
            factory = XPathFactory.newInstance();
            xpath = factory.newXPath();
            xpath.setNamespaceContext(new JournalNamespaceContext());
        } catch (SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getPaprTitl(){
        
        String paprTitl = null;
        String queryExpr = "//pre:PaprElem[pre:PaperID = '"+ paperID + "']//pre:PaprTitl/text()";
        
        try {
            XPathExpression exprPaprTitl
                       = xpath.compile(queryExpr); 
            paprTitl = exprPaprTitl.evaluate(doc);

               
            } catch (XPathExpressionException ex) {
                Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return paprTitl;
    }
    
    public String getAbstract(){        
        String paprAbstr = null;
        String queryExpr = "//pre:PaprElem[pre:PaperID = '"+ paperID +"']//pre:Abstract/text()";
        try {
            
            XPathExpression exprAbstr
                    = xpath.compile(queryExpr);
            
            paprAbstr = exprAbstr.evaluate(doc);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paprAbstr;
    }
    
    public String getJNamCN(){
        String JNamCN = null;
        String queryExpr = "//pre:BscElem/pre:JNamCN/text()";
        try {
            XPathExpression exprJNamCN
                    = xpath.compile(queryExpr);
            JNamCN = exprJNamCN.evaluate(doc);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JNamCN = "《" + JNamCN + "》";
        
        return JNamCN;
    }
    public String getJIss() {
        String JIss = null;
        String queryExpr = "//pre:BscElem/pre:JIss/text()";
        try {
            XPathExpression exprJIss
                    = xpath.compile(queryExpr);
            JIss = exprJIss.evaluate(doc);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JIss;
    }
    
    public String getIssNbr() {
        String IssNbr = null;
        String queryExpr = "//pre:BscElem/pre:IssNbr/text()";
        try {
            XPathExpression exprJIss
                    = xpath.compile(queryExpr);
            IssNbr = exprJIss.evaluate(doc);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IssNbr.length() == 1) {
            IssNbr = "0" + IssNbr;
        }
        return IssNbr;
    }
    
    public String getAuthor() {
        String Author = "";
        String queryExpr = "//pre:PaprElem[pre:PaperID = '"+ paperID +"']//pre:AuthName/text()";
        
        try {
            XPathExpression exprAuthor 
                    = xpath.compile(queryExpr);
            Object resultOfAuthSet = exprAuthor.evaluate(doc, XPathConstants.NODESET);
            NodeList AuthorList = (NodeList) resultOfAuthSet;
            for(int i = 0; i < AuthorList.getLength(); i ++) {
            Author += AuthorList.item(i).getNodeValue() + " ";
        }

        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return Author;
    }
    
    public String getKWrd() {
        String KWrd = "";
        String queryExpr = "//pre:PaprElem[pre:PaperID = '"+ paperID +"']//pre:KWrd/text()";
        
        try {
            XPathExpression exprKWrd 
                    = xpath.compile(queryExpr);
            Object resultOfKWrdSet = exprKWrd.evaluate(doc, XPathConstants.NODESET);
            NodeList KWrdList = (NodeList) resultOfKWrdSet;
            for(int i = 0; i < KWrdList.getLength(); i ++) {
            KWrd += KWrdList.item(i).getNodeValue() + " ";
        }

        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return KWrd;
    }
    /*
    public static void main(String[] args) {
        String path = "E:/ZHAOXIN/Data/Dataset/PDF/11-1450-G2/2012001/11-1450-G2_2012001_P_0014.pdf";
        XMLParser parser = new XMLParser(path);
        
        System.out.println(parser.getPaprTitl());
        System.out.println(parser.getAbstract());
        System.out.println(parser.getJNamCN());
        System.out.println(parser.getJIss());
        System.out.println(parser.getIssNbr());
        System.out.println(parser.getAuthor());
        System.out.println(parser.getKWrd());
    }
    * */
}
