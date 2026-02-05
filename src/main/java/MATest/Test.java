package MATest;

import MultilingualAnalyzer.lucene.MLAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public class Test {

    private static void doToken(TokenStream ts) throws IOException {
        ts.reset();
        CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
        while (ts.incrementToken()) {
            System.out.print(cta.toString() + "|");
        }
        System.out.println();
        ts.end();
        ts.close();
    }

    public static void main(String[] args) throws IOException {
        String text = "北京科技大学AI实验室";

        try (Analyzer ik = new MLAnalyzer()) {
            TokenStream ts = ik.tokenStream("content", text);
            System.out.println("IKAnalyzer中文分词器 细粒度切分，英文分词效果：");
            doToken(ts);
        }
    }




}
