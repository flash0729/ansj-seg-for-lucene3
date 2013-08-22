package org.ansj.analysis.lucene.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;

public class StopwordsUtil {

	/**
	 * Creates a CharArraySet from a file.
	 * 
	 * @param stopwords
	 *            the stopwords file to load
	 * 
	 * @param matchVersion
	 *            the Lucene version for cross version compatibility
	 * @return a CharArraySet containing the distinct stopwords from the given
	 *         file
	 * @throws IOException
	 *             if loading the stopwords throws an {@link IOException}
	 */
	public static CharArraySet loadStopwordSet(File stopwords,
			Version matchVersion) throws IOException {
		Reader reader = null;
		try {
			reader = IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8);
			return WordlistLoader.getWordSet(reader, matchVersion);
		} finally {
			IOUtils.close(reader);
		}
	}

	/**
	 * Creates a CharArraySet from a file.
	 * 
	 * @param stopwords
	 *            the stopwords reader to load
	 * 
	 * @param matchVersion
	 *            the Lucene version for cross version compatibility
	 * @return a CharArraySet containing the distinct stopwords from the given
	 *         reader
	 * @throws IOException
	 *             if loading the stopwords throws an {@link IOException}
	 */
	public static CharArraySet loadStopwordSet(Reader stopwords,
			Version matchVersion) throws IOException {
		try {
			return WordlistLoader.getWordSet(stopwords, matchVersion);
		} finally {
			IOUtils.close(stopwords);
		}
	}
}
