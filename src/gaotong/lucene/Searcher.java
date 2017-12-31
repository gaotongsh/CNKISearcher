package gaotong.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Searcher {

    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private int numDocs;

    public Searcher(String indexPath, boolean isUseSmartCn) throws IOException {
        Path indexDir = Paths.get(indexPath);
        Directory dir = FSDirectory.open(indexDir);
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        numDocs = directoryReader.numDocs();
        indexSearcher = new IndexSearcher(directoryReader);
        queryParser = new QueryParser(LuceneConstants.TITLE, CNKIAnalyzerFactory.getAnalyzer(isUseSmartCn));
    }

    public TopDocs search(String queryString) throws ParseException, IOException {
        Query query = queryParser.parse(queryString);
        return indexSearcher.search(query, numDocs);
    }

    public TopDocs search(String queryString, int queryNum) throws ParseException, IOException {
        Query query = queryParser.parse(queryString);
        return indexSearcher.search(query, queryNum);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }
}
