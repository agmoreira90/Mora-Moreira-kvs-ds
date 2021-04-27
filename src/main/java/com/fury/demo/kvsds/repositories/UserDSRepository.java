package com.fury.demo.kvsds.repositories;

import com.fury.demo.kvsds.dtos.UserDto;
import com.mercadolibre.ds.common.types.Aggregations;
import com.mercadolibre.ds.common.types.FieldType;
import com.mercadolibre.ds.common.types.buckets.TermsBucket;
import com.mercadolibre.dsclient.DsClient;
import com.mercadolibre.dsclient.config.DsClientConfiguration;
import com.mercadolibre.dsclient.exception.DsClientException;
import com.mercadolibre.dsclient.impl.EntityDsClient;
import com.mercadolibre.dsclient.response.Response;
import com.mercadolibre.dsclient.response.search.SearchResponse;
import com.mercadolibre.dsclient.search.AggregationBuilders;
import com.mercadolibre.dsclient.search.QueryBuilders;
import com.mercadolibre.dsclient.search.builders.query.QueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDSRepository {
    private DsClient dsClient;

    public UserDSRepository() throws DsClientException {
        DsClientConfiguration conf = DsClientConfiguration.builder()
                .withServiceName("my-container-ds")
                .build();
        this.dsClient = new EntityDsClient(conf); //ready to rock!
    }

    public Response saveUser(UserDto user) throws DsClientException {
        Response response = dsClient.saveDocument(user.getId().toString(), user);
        return response;
    }

    public Response deleteUser(UserDto user) throws DsClientException {
        Response response = dsClient.deleteDocument(user.getId().toString());
        return response;
    }

    public void getUserByCountry(String country) throws DsClientException {
        QueryBuilder queryBuilder = QueryBuilders.and(
                QueryBuilders.eq("pais", country)
        );

        SearchResponse<UserDto> response = this.dsClient.searchBuilder()
                .addAggregation(AggregationBuilders.terms("usuarios").field("id", FieldType.NUMBER))
                .withQuery(queryBuilder)
                .withSize(100)
                .execute(UserDto.class);

        // get total matched documents
        int total = (int) response.getTotal();

        // get returned documents
        List<UserDto> documents = response.getDocuments();

        // get returned aggregations
        Aggregations aggregations = response.getAggregations();
        List<TermsBucket> myAggregation = aggregations.terms("usuarios");
        for (TermsBucket bucket : myAggregation) {
            Object key = bucket.getKey();
            long count = bucket.getCount();
        }
    }


}
