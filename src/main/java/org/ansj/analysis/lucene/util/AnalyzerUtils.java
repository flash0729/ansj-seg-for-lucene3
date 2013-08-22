package org.ansj.analysis.lucene.util;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;

public class AnalyzerUtils {
	public static void displayTokens(Analyzer analyzer, String text)
			throws IOException {
		displayTokens(analyzer.tokenStream("contents", new StringReader(text)));
	}

	public static void displayTokens(TokenStream stream) throws IOException {
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		while (stream.incrementToken()) {
			System.out.print("[" + getTerm(term) + "] ");
		}
	}

	public static int getPositionIncrement(AttributeSource source) {
		PositionIncrementAttribute attr = source.addAttribute(PositionIncrementAttribute.class);
		return attr.getPositionIncrement();
	}

	public static String getTerm(AttributeSource source) {
		CharTermAttribute term = source.addAttribute(CharTermAttribute.class);
		return getTerm(term);
	}
	
	public static String getTerm(CharTermAttribute term) {
		return new String(term.buffer(), 0, term.length());
	}

	public static String getType(AttributeSource source) {
		TypeAttribute attr = source.addAttribute(TypeAttribute.class);
		return attr.type();
	}

	public static void setPositionIncrement(AttributeSource source, int posIncr) {
		PositionIncrementAttribute attr = source.addAttribute(PositionIncrementAttribute.class);
		attr.setPositionIncrement(posIncr);
	}

	public static void setTerm(AttributeSource source, char[] term) {
		CharTermAttribute attr = source.addAttribute(CharTermAttribute.class);
		attr.copyBuffer(term, 0, term.length);
	}

	public static void setType(AttributeSource source, String type) {
		TypeAttribute attr = source.addAttribute(TypeAttribute.class);
		attr.setType(type);
	}

	public static void displayTokensWithPositions(Analyzer analyzer, String text)
			throws IOException {

		TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);

		int position = 0;
		while (stream.incrementToken()) {
			int increment = posIncr.getPositionIncrement();
			if (increment > 0) {
				position = position + increment;
				System.out.println();
				System.out.print(position + ": ");
			}

			System.out.print("[" + getTerm(term) + "] ");
		}
		System.out.println();
	}

	public static void displayTokensWithFullDetails(Analyzer analyzer,
			String text) throws IOException {

		TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));

		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);

		int position = 0;
		while (stream.incrementToken()) {

			int increment = posIncr.getPositionIncrement();
			if (increment > 0) {
				position = position + increment;
				System.out.println();
				System.out.print(position + ": ");
			}

			System.out.print("[" + getTerm(term) + ":"
					+ offset.startOffset() + "->" + offset.endOffset() + ":"
					+ type.type() + "] ");
		}
		System.out.println();
	}

	public static void displayPositionIncrements(Analyzer analyzer, String text)
			throws IOException {
		TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		while (stream.incrementToken()) {
			System.out.println("posIncr=" + posIncr.getPositionIncrement());
		}
	}
}
