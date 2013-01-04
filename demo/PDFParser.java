/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.lucene.demo;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author dp
 */
public class PDFParser {

    private String CMD ;
    public void parsePDFs(File parserDir, File file) throws IOException, InterruptedException{
        
        if(file.canRead()){
            if(file.isDirectory()){
                String[] files = file.list();
                if(files != null){
                    for(int i = 0; i < files.length; i ++){
                        
                        parsePDFs(parserDir, new File(file, files[i]));
                    }
                }
            }else{
                
                if(file.getName().endsWith(".pdf")){
                    CMD = parserDir + " -enc UTF-8 " + file;
                    Process p = Runtime.getRuntime().exec(CMD);
                    p.waitFor();
                    System.out.println(file.getName() + " is parsed to text");
                }
                
            }
        }        
    }
    
 
    public static void main(String[] args) throws IOException{
        
        /*PDFParser parser = new PDFParser();
        File file = new File("D:/dataset/ACE.pdf");
        File parserDir = new File("D:/xpdf/pdftotext");
        System.out.println(file.getName());
        System.out.println(file.getCanonicalPath());
        parser.parsePDF(parserDir, file);*/
        
        final File docDir = new File(args[0]);
        if (!docDir.exists() || !docDir.canRead()) {
          System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
          System.exit(1);
        }
        
        File parserDir = new File("/home/lucene/xpdf/pdftotext");
        PDFParser parser = new PDFParser();
        try {
    		parser.parsePDFs(parserDir, docDir);
    	} catch (IOException e2) {
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	} catch (InterruptedException e2) {
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	}
    }
}
