package com.nickbi.luceneread;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * @author nickBi
 * create on 2019-04-04.
 */
public class LuceneTest {
	public static void main(String[] args) throws IOException {
//		indexData();
		searchIndex();
	}

	/**
	 * index data to local store by lucene
	 *
	 * @throws IOException read /write dictionary exception
	 */
	public static void indexData() throws IOException {
		//配置索引写对象
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig();
		//数据追加模式，新增或者追加
		indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		FSDirectory open = FSDirectory.open(new File("/Users/bijialin/workspace/lingrit/lucene-read/src/main" +
				"/resources" +
				"/static/lucene/luceneData").toPath());
		//build indexWriter
		IndexWriter indexWriter = new IndexWriter(open, indexWriterConfig);
		//create Document
		for (int i = 0; i < 10; i++) {
			Document doc = new Document();
			doc.add(new StoredField("id", i));
			doc.add(new TextField("name", "this is a lucene doc text!, index id is:" + i, Field.Store.YES));
			indexWriter.addDocument(doc);
			indexWriter.flush();
			indexWriter.commit();
		}
		indexWriter.close();
		open.close();
	}

	/**
	 * search info by lucene
	 *
	 * @throws IOException read dictionary exception
	 */
	public static void searchIndex() throws IOException {

		FSDirectory directory = FSDirectory.open(new File("/Users/bijialin/workspace/lingrit/lucene-read/src/main" +
				"/resources" +
				"/static/lucene/luceneData").toPath());
		IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
		Term term = new Term("id", "1");
		TopDocs search = indexSearcher.search(new TermQuery(term), 10);
		BooleanQuery booleanClauses =
				new BooleanQuery.Builder().add(new TermQuery(term), BooleanClause.Occur.MUST).build();
		System.out.println(search);
	}

}
