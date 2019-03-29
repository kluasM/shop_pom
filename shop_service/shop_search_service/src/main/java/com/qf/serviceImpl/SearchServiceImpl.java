package com.qf.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchServicr;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchServicr {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<Goods> queryByIndexId(String keyword) {


        SolrQuery solrQuery = new SolrQuery();
        if(keyword == null || keyword.equals("")){
            solrQuery.setQuery("*:*");
        }else{
            solrQuery.setQuery("gtitle:"+keyword+" || ginfo:"+keyword);
        }


        List<Goods> goodsList = new ArrayList<>();

        QueryResponse query = null;

        try {
            query = solrClient.query(solrQuery);
            SolrDocumentList results = query.getResults();
            for(SolrDocument documents : results){
                String id = (String) documents.get("id");
                String gtitle = (String) documents.get("gtitle");
                String ginfo = (String) documents.get("ginfo");
                float gprice = (float) documents.get("gprice");
                int gcount = (int) documents.get("gcount");
                String gimage = (String) documents.get("gimage");
                Goods goods = new Goods(
                        Integer.parseInt(id),
                        gtitle,
                        ginfo,
                        gcount,
                        0,
                        0,
                        gprice,
                        gimage
                );

                goodsList.add(goods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsList;
    }

    @Override
    public int insertIndexed(Goods goods) {
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", goods.getId());
        document.setField("gtitle", goods.getTitle());
        document.setField("ginfo", goods.getGinfo());
        document.setField("gimage", goods.getGimage());
        document.setField("gcount", goods.getGcount());
        document.setField("gprice", goods.getPrice());

        try {
            solrClient.add(document);
            solrClient.commit();
            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
