package org.ansj.analysis.lucene;

import java.io.Reader;
import java.util.Set;

import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.ReusableAnalyzerBase;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;

public class AnsjAnalyzer extends ReusableAnalyzerBase {
	
	private Class<? extends Analysis> analysis;

	private final Version matchVersion;

	/**
	 * An immutable stopword set
	 */
	private final CharArraySet stopwords;
	
	public AnsjAnalyzer(final Version matchVersion, Class<? extends Analysis> analysis) {
		this(matchVersion, analysis, null);
	}

	public AnsjAnalyzer(final Version matchVersion) {
		this(matchVersion, null, null);
	}

	/**
     * @param analysis 
     *            搜索时用精准分词，索引时用面向索引的分词，默认值为面向索引的分词
     */
	public AnsjAnalyzer(final Version matchVersion, Class<? extends Analysis> analysis, final Set<?> stopwords) {
		this.matchVersion = matchVersion;
		this.analysis = analysis == null ? IndexAnalysis.class : analysis;
		// analyzers should use char array set for stopwords!
	    this.stopwords = stopwords == null ? CharArraySet.EMPTY_SET : CharArraySet
	        .unmodifiableSet(CharArraySet.copy(matchVersion, stopwords));
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Analysis in;
		try {
			in = analysis.getConstructor(Reader.class).newInstance(reader);
		} catch (Exception e) {
			throw new RuntimeException("Ansj analysis can't be instance!");
		}
		
		final Tokenizer source = new AnsjTokenizer(reader,in);
		
		TokenStreamComponents result;
		if (stopwords.isEmpty()) {
			result = new TokenStreamComponents(source);
		} else {
			result = new TokenStreamComponents(source,new StopFilter(matchVersion, source, stopwords));
		}
		
		return result;
	}
}

