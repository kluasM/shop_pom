package com.qf.shop_search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchApplicationTests {

	@Autowired
	private SolrClient solrClient;

	@Test
	public void add() throws IOException, SolrServerException {

		SolrInputDocument document = new SolrInputDocument();

		document.setField("id",1);
		document.setField("gtitle","旺仔牛奶");
		document.setField("ginfo","真的好好喝噢");
		document.setField("gprice",25.0);
		document.setField("gimage","http://www.jd.com");
		document.setField("gcount",999);
		solrClient.add(document);
		solrClient.commit();
	}
	@Test
	public void delete() throws IOException, SolrServerException {
	  solrClient.deleteById("44");
	  solrClient.commit();
	}
	@Test
	public void query() throws IOException, SolrServerException {
		SolrQuery solrQuery=new SolrQuery();
		solrQuery.setQuery("*:*");
		QueryResponse queryResponse=solrClient.query(solrQuery);
		SolrDocumentList solrDocuments=queryResponse.getResults();
		for (SolrDocument solrDocument: solrDocuments) {
			String id = (String) solrDocument.get("id");
			System.out.println(id);
		}

	}

}

