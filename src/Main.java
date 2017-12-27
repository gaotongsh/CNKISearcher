import gaotong.lucene.Indexer;
import gaotong.lucene.LuceneConstants;
import gaotong.lucene.Searcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String indexPath = "../index/";
        String dataFile = "../data/CNKI_toy.txt";
        String query = "设计";
        try {
            Indexer indexer = new Indexer(indexPath);
            indexer.createIndex(dataFile);
            indexer.closeWriter();

            Searcher searcher = new Searcher(indexPath);
            TopDocs topDocs = searcher.search(query);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                System.out.println("Hit: " + searcher.getDocument(sd).get(LuceneConstants.TITLE) + " " + sd.score);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
