package org.ansj.analysis.lucene;

import java.io.Reader;
import java.util.Set;

import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.ReusableAnalyzerBase;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;

public class AnsjIndexAnalyzer extends ReusableAnalyzerBase {

	private final Version matchVersion;

	/**
	 * An immutable stopword set
	 */
	private final CharArraySet stopwords;
	
	public AnsjIndexAnalyzer(final Version matchVersion, final Set<?> stopwords) {
		super();
		this.matchVersion = matchVersion;
		// analyzers should use char array set for stopwords!
	    this.stopwords = stopwords == null ? CharArraySet.EMPTY_SET : CharArraySet
	        .unmodifiableSet(CharArraySet.copy(matchVersion, stopwords));
	}

	public AnsjIndexAnalyzer(final Version matchVersion) {
		this(matchVersion, null);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		final Tokenizer source = new AnsjTokenizer(reader,new IndexAnalysis(reader));
		
		TokenStreamComponents result;
		if (stopwords.isEmpty()) {
			result = new TokenStreamComponents(source);
		} else {
			result = new TokenStreamComponents(source,new StopFilter(matchVersion, source, stopwords));
		}
		
		return result;
	}
}
