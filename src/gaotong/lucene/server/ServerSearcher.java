package gaotong.lucene.server;

import gaotong.lucene.Searcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("/search")
public class ServerSearcher {

    private String indexPath;
    private boolean isUseSmartCn;
    private Searcher searcher;

    public ServerSearcher() {
        indexPath = "../../index/";
        isUseSmartCn = true;
        try {
            searcher = new Searcher(indexPath, isUseSmartCn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result testGetQuery(@QueryParam("query") String query) {
        if (query == null) {
            return null;
        }

        try {
            System.out.print("Searching " + query);
            TopDocs topDocs = searcher.search(query);
            List<ResultItem> mapList = new LinkedList<>();
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document document = searcher.getDocument(sd);
//                System.out.println(document.getField(LuceneConstants.TITLE));
                ResultItem docMap = new ResultItem();
                List<IndexableField> fields = document.getFields();
                for(IndexableField field : fields){
                    docMap.getItems().put(field.name(), field.stringValue());
                }
                mapList.add(docMap);
            }
            Result result = new Result(mapList,
                    mapList.size(),
                    -1);
            System.out.println(". Found # " + result.getRetrieved());
            return result;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
