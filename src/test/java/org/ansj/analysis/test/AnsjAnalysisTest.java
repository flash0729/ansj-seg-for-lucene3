package org.ansj.analysis.test;

import static org.ansj.analysis.lucene.util.AnalyzerUtils.displayTokensWithFullDetails;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ansj.analysis.lucene.AnsjIndexAnalyzer;
import org.ansj.analysis.lucene.AnsjSearchAnalyzer;
import org.ansj.analysis.lucene.util.StopwordsUtil;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class AnsjAnalysisTest {
	
	private static CharArraySet stopwords;
	
	static {
		InputStream in = AnsjAnalysisTest.class.getResourceAsStream("/" + "stopwords.zh_cn");
		InputStreamReader reader = new InputStreamReader(in);
		try {
			stopwords = StopwordsUtil.loadStopwordSet(reader, Version.LUCENE_36);
		} catch (IOException e) {
			stopwords = CharArraySet.EMPTY_SET;
			e.printStackTrace();
		}
		
		ToAnalysis.parse("初始化词库");
	}

	public static void main(String[] args) throws IOException {
		String input = "我在首都机场虹桥路滑旱冰！玩的很Happy，很Hi！";
		
		System.out.print("Ansj索引时采用面向索引的分词：" + input);
		displayTokensWithFullDetails(new AnsjIndexAnalyzer(Version.LUCENE_36),input);
		System.out.print("索引分词时过滤停用词");
		displayTokensWithFullDetails(new AnsjIndexAnalyzer(Version.LUCENE_36,stopwords),input);
		
		System.out.println("------------------------------------------------------------");
		
		System.out.print("Ansj查询时采用精准分词：" + input);
		displayTokensWithFullDetails(new AnsjSearchAnalyzer(Version.LUCENE_36),input);
		System.out.print("查询分词时过滤停用词");
		displayTokensWithFullDetails(new AnsjSearchAnalyzer(Version.LUCENE_36,stopwords),input);
	}
	
	@Test
	public void test() {
		
	}
}
