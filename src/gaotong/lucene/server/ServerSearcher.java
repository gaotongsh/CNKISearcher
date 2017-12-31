package gaotong.lucene.server;

import gaotong.lucene.Searcher;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Path("/search")
public class ServerSearcher {

    private String indexPath;
    private boolean isUseSmartCn;
    private Searcher searcher;
    private SimpleHTMLFormatter formatter;

    public ServerSearcher() {
        indexPath = "../../index/";
        isUseSmartCn = true;
        formatter = new SimpleHTMLFormatter("<span class=\"em\">", "</span>");
        try {
            searcher = new Searcher(indexPath, isUseSmartCn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result testGetQuery(
            @QueryParam("query") String query,
            @QueryParam("count") String count) {
        if (query == null || count == null) {
            return null;
        }

        int countInt = -1;
        if (!count.equals("all")) {
            countInt = Integer.parseInt(count);
        }

        try {
            System.out.print("Searching " + query);
            // Core Searching Instruction
            TopDocs topDocs;
            if (countInt == -1) {
                topDocs = searcher.search(query);
            } else {
                topDocs = searcher.search(query, countInt);
            }
            // Setup Highlighter
            Highlighter highlighter =
                    new Highlighter(formatter, new QueryScorer(searcher.getQueryParser().parse(query)));
            highlighter.setTextFragmenter(new NullFragmenter());
            List<ResultItem> mapList = new LinkedList<>();
            String fieldValue, highlightedValue;
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document document = searcher.getDocument(sd);
//                System.out.println(document.getField(LuceneConstants.TITLE));
                ResultItem docMap = new ResultItem();
                List<IndexableField> fields = document.getFields();
                Fields tvFields = searcher.getDirectoryReader().getTermVectors(sd.doc);
                for(IndexableField field : fields) {
                    fieldValue = field.stringValue();
                    TokenStream tokenStream =
                            TokenSources.getTokenStream(field.name(), tvFields, fieldValue,
                                    searcher.getQueryParser().getAnalyzer(),
                                    highlighter.getMaxDocCharsToAnalyze() - 1);
                    highlightedValue = highlighter.getBestFragment(tokenStream, fieldValue);
                    if (highlightedValue == null) {
                        highlightedValue = fieldValue;
                    }
                    docMap.getItems().put(field.name(), highlightedValue);
                }
                mapList.add(docMap);
            }
            Result result = new Result(mapList,
                    mapList.size(),
                    -1);
            System.out.println(". Found # " + result.getRetrieved());
            return result;
        } catch (ParseException | IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return null;
    }

}
