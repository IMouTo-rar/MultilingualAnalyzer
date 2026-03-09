package MATest;

import MultilingualAnalyzer.lucene.MLAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;

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

    /**
     * 使用 MLAnalyzer 建索引并搜索的示例：同一分词器用于建索引和解析查询，保证检索一致。
     */
    private static void searchExample() throws Exception {
        Analyzer analyzer = new MLAnalyzer();
        try (ByteBuffersDirectory dir = new ByteBuffersDirectory()) {
            // 建索引
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            try (IndexWriter writer = new IndexWriter(dir, iwc)) {
                String[] docs = {
                    "北京科技大学AI实验室",
                    "清华大学计算机系",
                    "北京大学人工智能研究院"
                };
                for (int i = 0; i < docs.length; i++) {
                    Document doc = new Document();
                    doc.add(new TextField("content", docs[i], Field.Store.YES));
                    doc.add(new TextField("id", "doc" + i, Field.Store.YES));
                    writer.addDocument(doc);
                }
                writer.commit();
            }

            // 搜索：用同一分词器解析查询词
            try (DirectoryReader reader = DirectoryReader.open(dir)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                QueryParser parser = new QueryParser("content", analyzer);
                Query query = parser.parse("北京 科技");
                TopDocs topDocs = searcher.search(query, 10);
                System.out.println("查询: \"北京 科技\" 命中数: " + topDocs.totalHits.value);
                for (ScoreDoc sd : topDocs.scoreDocs) {
                    Document doc = searcher.storedFields().document(sd.doc);
                    System.out.println("  docId=" + sd.doc + " score=" + sd.score + " content=" + doc.get("content"));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String text = "北京科技大学AI实验室";

        try (Analyzer ik = new MLAnalyzer()) {
            TokenStream ts = ik.tokenStream("content", text);
            System.out.println("IKAnalyzer中文分词器 细粒度切分，英文分词效果：");
            doToken(ts);
        }

        System.out.println("\n--- 使用该分词器搜索示例 ---");
        try {
            searchExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
