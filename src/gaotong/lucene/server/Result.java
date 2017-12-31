package gaotong.lucene.server;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;
import java.util.List;

@XmlRootElement
public class Result {

    private List<ResultItem> records;
    private int retrieved = 0;
    private int available = 0;

    public Result() {}

    public Result(List<ResultItem> records, int retrieved, int available) {
        super();
        this.records = records;
        this.retrieved = retrieved;
        this.available = available;
    }

    public List<ResultItem> getRecords() {
        return records;
    }

    public int getRetrieved() {
        return retrieved;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setRecords(List<ResultItem> records) {
        this.records = records;
    }

    public void setRetrieved(int retrieved) {
        this.retrieved = retrieved;
    }
}