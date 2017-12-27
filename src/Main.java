import gaotong.lucene.Indexer;
import gaotong.lucene.LuceneConstants;
import gaotong.lucene.Searcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        String indexPath = "../index/";
        String dataFile = "../data/CNKI_toy.txt";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Use SmartCnAnalyzer instead of Bigram CJK Analyzer (Y/n)? ");
            boolean isUseSmartCn = parseBoolean(br.readLine().toLowerCase());
            System.out.print("Keep original index? (Y/n)? ");

            if (!parseBoolean(br.readLine().toLowerCase())) {
                Indexer indexer = new Indexer(indexPath, isUseSmartCn);
                indexer.createIndex(dataFile);
                indexer.closeWriter();
            }

            String query;
            while(true) {
                System.out.print("Input Query: ");
                query = br.readLine();
                if (query.equals("EXIT"))
                    break;
                Searcher searcher = new Searcher(indexPath, isUseSmartCn);
                TopDocs topDocs = searcher.search(query);
                System.out.println("Found a total of " + topDocs.totalHits + " hits:");
                System.out.println("--------------------------");
                for (ScoreDoc sd : topDocs.scoreDocs) {
                    System.out.println("Title: " + searcher.getDocument(sd).get(LuceneConstants.TITLE) + " " + sd.score);
                }
                System.out.println("--------------------------");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private static boolean parseBoolean(String str) {
        return !str.equals("no") && !str.equals("n");
    }

}
