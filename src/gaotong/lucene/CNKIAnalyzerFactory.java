package gaotong.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.util.HashMap;
import java.util.Map;

public class CNKIAnalyzerFactory {

    public static Analyzer getAnalyzer(boolean isUseSmartCn) {

        Map<String, Analyzer> analyzerPerField = new HashMap<>();

        Analyzer cnAnalyzer, enAnalyzer, stdAnalyzer;
        if (isUseSmartCn) {
            cnAnalyzer = new SmartChineseAnalyzer();
        } else {
            cnAnalyzer = new CJKAnalyzer();
        }
        enAnalyzer = new EnglishAnalyzer();
        stdAnalyzer = new StandardAnalyzer();

        analyzerPerField.put(LuceneConstants.TITLE, cnAnalyzer);
        analyzerPerField.put(LuceneConstants.AUTHOR, stdAnalyzer);
        analyzerPerField.put(LuceneConstants.FIRST_AUTHOR, stdAnalyzer);
        analyzerPerField.put(LuceneConstants.KEYWORD, cnAnalyzer);
        analyzerPerField.put(LuceneConstants.ABSTRACT, cnAnalyzer);
        analyzerPerField.put(LuceneConstants.INSTITUTE, cnAnalyzer);
        analyzerPerField.put(LuceneConstants.SOURCE, cnAnalyzer);

        analyzerPerField.put(LuceneConstants.TITLE_ENGLISH, enAnalyzer);
        analyzerPerField.put(LuceneConstants.AUTHOR_ENGLISH, enAnalyzer);
        analyzerPerField.put(LuceneConstants.KEYWORD_ENGLISH, enAnalyzer);
        analyzerPerField.put(LuceneConstants.ABSTRACT_ENGLISH, enAnalyzer);

        return new PerFieldAnalyzerWrapper(new SimpleAnalyzer(), analyzerPerField);
    }

}
