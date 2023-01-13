package com.vinhlam.tourScheduling.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vinhlam.tourScheduling.entity.TokenUser;

@Repository
public class TokenUserRepository {

	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoTemplate mongoTemplate;
	
	private MongoCollection<TokenUser> tokenUserTourCollection;
	
	@Autowired
	public void PriceTourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		tokenUserTourCollection = mongoDatabase.getCollection("tokenUser", TokenUser.class).withCodecRegistry(cRegistry);
	}
	
	public List<String> getAllListTokenUserDistinct() {
		List<String> listTokenUser = new ArrayList<>();
		
		DistinctIterable<String> tokens = tokenUserTourCollection.distinct("listToken", String.class);
		MongoCursor<String > results = tokens.iterator();
		
		while(results.hasNext()) {
			listTokenUser.add(results.next());
        }
		
		return listTokenUser;
		
	}
}
