package com.elmaghraby.app.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import com.elmaghraby.app.entities.Product;
import com.elmaghraby.app.entities.ProductCategory;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

	private EntityManager entityManager;

	@Autowired
	public DataRestConfig(EntityManager TheEntityManager) {
		this.entityManager = TheEntityManager;
	}

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		HttpMethod[] theUnSupportedActions = { HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT };

		// disable HTTp method for products : PUT , POST and DELETE
		config.getExposureConfiguration().forDomainType(Product.class)
				.withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions))
				.withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions));

		// disable HTTp method for productCategory : PUT , POST and DELETE
		config.getExposureConfiguration().forDomainType(ProductCategory.class)
				.withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions))
				.withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions));
		// call an Internal Helper Method ..
		exposeIds(config);

	}

	private void exposeIds(RepositoryRestConfiguration config) {
		// expose entity ids ..
		
		// get a list of all entity classes from the entity manager 
		Set<EntityType<?>>  entities = entityManager.getMetamodel().getEntities();
		//create an array of entity types
		List<Class> entityClasses= new ArrayList<>();
		
		// get the entity type For the entites
		for(EntityType tempEntityType : entities) {
			entityClasses.add(tempEntityType.getJavaType());
			
		// expose the entity ids for the array of entity domain types ..
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
		}
		
		
	}

}
