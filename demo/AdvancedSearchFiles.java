/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.lucene.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
/**
 *
 * @author ZHAO XIN
 */
public class AdvancedSearchFiles {
    
    private String indexPath = null;
    
    private String fieldOfPaprTitl = "PaprTitl" ;
    private String fieldOfAuthor = "Author";
    private String fieldOfPaprKWrd = "PaprKWrd";
    
    private boolean raw = true;
    private boolean paging = true;
    private int hitsPerPage = 10;
    private IndexReader reader;
    private Searcher searcher;
    
    private CachingWrapperFilter filterOfPaprTitl;
    private CachingWrapperFilter filterOfAuthor;
    private CachingWrapperFilter filterOfPaprKWrd;
    
    private int numTotalHits = 0;

    public AdvancedSearchFiles(String path){
        
        indexPath = path;
        filterOfPaprTitl = null;
        filterOfAuthor = null;
        filterOfPaprKWrd = null;
        try {
            reader = IndexReader.open(FSDirectory.open(new File(indexPath)), true);
            searcher = new IndexSearcher(reader);
        } catch (IOException ex) {
            Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getNumTotalHits(){
        
        return numTotalHits;
    }
    
    public TopDocs doAdvancedSearch(String PaprTitl, String Author, String PaprKWrd, int maxPage) {
    	//public TopDocs doAdvancedSearch(Query PaprTitl, Query Author, Query PaprKWrd, int maxPage) {
        
        TopScoreDocCollector collector 
                = TopScoreDocCollector.create(5 * hitsPerPage, false);
        
        Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_30);
        
        QueryParser queryParserOfPaprTitl = new QueryParser(Version.LUCENE_30, fieldOfPaprTitl, analyzer);
        QueryParser queryParserOfAuthor = new QueryParser(Version.LUCENE_30, fieldOfAuthor, analyzer);
        QueryParser queryParserOfPaprKWrd = new QueryParser(Version.LUCENE_30, fieldOfPaprKWrd, analyzer);
        
        int searchFlag = 0;
        
        Query queryOfPaprTitl = null;
        Query queryOfAuthor = null;
        Query queryOfPaprKWrd = null;
        
        if( PaprTitl != null) {
            try {
                queryOfPaprTitl = queryParserOfPaprTitl.parse(PaprTitl.trim());
                QueryWrapperFilter filter = new QueryWrapperFilter(queryOfPaprTitl);
                filterOfPaprTitl = new CachingWrapperFilter(filter);
                searchFlag = 1;
                
            } catch (ParseException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if (Author != null) {
            try {
                queryOfAuthor = queryParserOfAuthor.parse(Author.trim());
                QueryWrapperFilter filter = new QueryWrapperFilter(queryOfAuthor);
                
                if( filterOfPaprTitl != null) {
                    System.out.println("Search in the result of paper title");
                    filterOfAuthor = new CachingWrapperFilter(filterOfPaprTitl);
                }
                else {
                    filterOfAuthor = new CachingWrapperFilter(filter);
                }
                searchFlag = 2;
                
            } catch (ParseException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
        if (PaprKWrd != null) {
            try {
                queryOfPaprKWrd = queryParserOfPaprKWrd.parse(PaprKWrd.trim());
                QueryWrapperFilter filter = new QueryWrapperFilter(queryOfPaprKWrd);
                
                if( filterOfAuthor != null){
                    filterOfPaprKWrd = new CachingWrapperFilter(filterOfAuthor);
                }
                else if( filterOfPaprTitl != null) {
                    filterOfPaprKWrd = new CachingWrapperFilter(filterOfPaprTitl);
                }
                else {
                    filterOfPaprKWrd = new CachingWrapperFilter(filter);
                }
                
            } catch (ParseException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            searchFlag = 3;
        }
        
        switch(searchFlag) {
            case 0: {
                System.out.println("0");
                System.out.println("Your inputs of search fields are empty.");
            };
                break;
            case 1: {
                System.out.println("1");
            try {
                TopDocs hits;
                hits = searcher.search(queryOfPaprTitl, filterOfPaprTitl, maxPage);
                numTotalHits = hits.totalHits;
                return hits;
                /*for(int i = 0; i < numTotalHits; i ++){
                    Document doc = searcher.doc(hits.scoreDocs[i].doc);
                    System.out.println(doc.get("PaprTitl"));
                }*/
            } catch (IOException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            };
                break;
            case 2: {
                System.out.println("2");
            try {
                TopDocs hits;
                hits = searcher.search(queryOfAuthor, filterOfAuthor, maxPage);
                numTotalHits = hits.totalHits;
                return hits;
            } catch (IOException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            };
                break;
            case 3: {
                System.out.println("3");
            try {
                TopDocs hits;
                hits = searcher.search(queryOfPaprKWrd, filterOfPaprKWrd, maxPage);
                numTotalHits = hits.totalHits;
                return hits;
            } catch (IOException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            };
                break;
        }
        
        return null;
    }
    
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String path = "C:/Users/dengpan/Desktop/index";
        AdvancedSearchFiles aSearch = new AdvancedSearchFiles(path);
        IndexReader reader;
        Searcher searcher = null;
        try {
            reader = IndexReader.open(FSDirectory.open(new File(path)), true);
            searcher = new IndexSearcher(reader);
        } catch (CorruptIndexException ex) {
            Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        TopDocs hits = null;
        hits = aSearch.doAdvancedSearch(null, null, null,100);
        
        if(hits == null){
            System.out.println();
        }
        System.out.println(hits.totalHits);
        for(int i = 0; i < hits.totalHits; i ++){
            try {
                Document doc = searcher.doc(hits.scoreDocs[i].doc);
                System.out.println(doc.get("PaprTitl"));
                System.out.println(doc.get("Author"));
                System.out.println(doc.get("PaprKWrd"));
            } catch (CorruptIndexException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AdvancedSearchFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String d = null;
        
    }
    
}
