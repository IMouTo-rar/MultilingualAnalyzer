/**
 * 基于 IKAnalyzer 实现的多语言分词
 * https://code.google.com/archive/p/ik-analyzer/
 * 兼容 Java 17 | Lucene 9.x
 */
package MultilingualAnalyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * IK分词器，Lucene Analyzer接口实现
 * 兼容Lucene 4.0版本
 */
public final class MLAnalyzer extends Analyzer{
	
	private boolean useSmart;
	
	public boolean isUseSmart() {
		return useSmart;
	}

	public void setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
	}

	/**
	 * IK分词器Lucene  Analyzer接口实现类
	 * 
	 * 默认细粒度切分算法
	 */
	public MLAnalyzer(){
		this(false);
	}

	/**
	 * IK分词器Lucene Analyzer接口实现类
	 * 
	 * @param useSmart 当为true时，分词器进行智能切分
	 */
	public MLAnalyzer(boolean useSmart){
		super();
		this.useSmart = useSmart;
	}

	/**
	 * 重载Analyzer接口，构造分词组件
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer _IKTokenizer = new MLTokenizer(this.isUseSmart());
		return new TokenStreamComponents(_IKTokenizer);
	}

}
