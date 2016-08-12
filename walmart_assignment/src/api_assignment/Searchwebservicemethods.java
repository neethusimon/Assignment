/*  NeethuSimon Walmart_api_assignment
 *  1. methods to return the itemid of the first product  in the  search for products based upon 
 *  a user-provided search string
 *  2. method that uses the first item in the search response as input for a product recommendation search
 *  and returns the itemid of  the first 10 product recommendations
 *  3. method that retrieves the averageoverallrating for each item in the product recommendation search 
 *  4. method that sort every item in product recommendation search based on averageoverallrating  
 */
package api_assignment;
 
import java.util.Map.Entry;
import java.util.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;


public class Searchwebservicemethods {
	
/*return the itemid of the first product  in the  search for products based upon 
* a user-provided search string
* input - user-provided search string,  search filter and apiKey
* returns - itemid of the first product  in the  search
*/	
	public String getFirstItemId(String Productname,String filter,String apikey,String url) 
			                     throws JSONException{
		
		Client searchClient= ClientBuilder.newClient();
		
       /* Search API allows text search on the Walmart.com catalogue and returns 
        * matching items available for sale online.
        */
		 String searchUrl;
		 
		 /* filter by increasing price*/
		  if(filter.equals("sortbyprice")){
			  searchUrl = url+"search?apiKey="+apikey+"&query="+Productname+"&sort=price&order=asc";
			  //searchUrl ="http://api.walmartlabs.com/v1/search?apiKey=akrfg97bzwv9625dnc8ygkxj&query="+ Productname +"&sort=price&order=asc";
		   }
		  
		  /* filter by best sellers*/
		  else if(filter.equals("bestseller")){
			  searchUrl = url+"search?apiKey="+apikey+"&query="+Productname+"&sort=bestseller&responseGroup=full";
		  }
		  
		  /*normal search */
		  else
		 searchUrl = url+"search?apiKey="+apikey+"&query="+Productname+"&numItems=10&format=json";
		System.out.println(searchUrl);
		String productSearch = searchClient.target(searchUrl)
	 		                     .request(MediaType.APPLICATION_JSON)
				                  .get(String.class);
	  
	    JSONObject jObject2 = new JSONObject(productSearch);
	      
	    JSONArray items = jObject2.getJSONArray("items");
	       
	    /*first Item */
	    JSONObject item = items.getJSONObject(0);
	    String itemId = item.getString("itemId");
	       
	    return itemId;
	}
	
/* method uses the first item in the search response as input for a product recommendation search
* and returns the itemid of the first 10 product recommendations
* input - itemid of the first item in the serach response,apikey
* returns - itemid of the first 10 product recommendations
*/
   public List<String> getprodrecommend(String itemId,String apikey,String url) 
		                                throws JSONException{
	   
	   /*itemid of the first 10 items*/
	   List<String> itemIds = new ArrayList<String>();
	   
	   Client prodRecommendClient= ClientBuilder.newClient();
	   
       /* Product Recommendation API returns  item responses, being ordered from most 
        * relevant to least relevant for the customer.
        */
	 
        String searchUrl = url+"nbp?apiKey="+apikey+"&itemId="+itemId ;
		System.out.println(searchUrl);
		String productRecommend = prodRecommendClient.target(searchUrl)
	 		                     .request(MediaType.APPLICATION_JSON)
				                  .get(String.class);
	  
	    JSONArray jsonArray = new JSONArray(productRecommend);
	    for (int i=0; i<10; i++) {
	        JSONObject items = jsonArray.getJSONObject(i);
	         String id  = items.getString("itemId");
	         itemIds.add(id);
	    }
       return itemIds;
	   
   }
   
/*for each item in the product recommendation search , retrieve the averageoverallrating and itemname
 * if a product has no reviews, averageoverallrating is assumed to be 0.0
 * input - itemids in the product recommendation search(10 products),apikey
 * returns- itemname and average overall rating for every 10 products
*/
   
   public  Map<String,Double>getitemIdRating(List<String>itemIds,String apikey,String url)  
		                                      throws JSONException{
	   
	     Double averageOverallRating;
	     
	     /* stores itemname and averageoverallrating*/
		 Map<String,Double>itemIdrating = new HashMap<String,Double>();
		  
		  for(String item:itemIds){
				Client reviewClient= ClientBuilder.newClient();
				String reviewUrl = url+"reviews/"+item+"?apiKey="+apikey+"&&format=json";
				System.out.println(reviewUrl);
				String reviewSearch = reviewClient.target(reviewUrl)
			 		                     .request(MediaType.APPLICATION_JSON)
						                  .get(String.class);
				
				JSONObject jObjectReview = new JSONObject(reviewSearch);
				
				 JSONArray reviews = jObjectReview.getJSONArray("reviews");
				 
				 /*products with no reviews , average overallrating is assumed to be 0.0*/
				 if (reviews.length()==0){
					 averageOverallRating = 0.0;
				 }
				 
				 /*products with reviews*/
				 else
				 {
				 JSONObject reviewStatistics = jObjectReview.getJSONObject("reviewStatistics");
				 averageOverallRating = reviewStatistics.getDouble("averageOverallRating");
				 }
				
				 /* saves the itemname instead of itemid for displaying the recommnedation in UI*/
				 
				 String name = jObjectReview.getString("name");
				 itemIdrating.put(name, averageOverallRating);
				 
		  }
		return itemIdrating;
		   
   }
/*sort every item in product recommendation search based on averageoverallrating desc
 * input- unsorted map containing item name as key and averageoverallrating as value
 * returns - sorted map with item name as key and averageoverallrating as value
 */
   
   public Map<String,Double>sortItemIdRating(Map<String,Double>itemIdrating){
	   
	   /* stores itemname and averageoverallrating sorted by averageoverallrating desc*/
	   Map<String,Double>product_Review = new LinkedHashMap<String,Double>();
       
	   Set<Entry<String,Double>> hash=itemIdrating.entrySet();
      
      
      // used linked list to sort
      
	  List<Entry<String,Double>> aList = new LinkedList<Entry<String,Double>>(hash);
      
	  // sorting the List
      Collections.sort(aList, new Comparator<Entry<String,Double>>() {

          @Override
          public int compare(Entry<String, Double> ele1,
                  Entry<String, Double> ele2) {
              
              return ele2.getValue().compareTo(ele1.getValue());
          }
      });
      
     // Storing the list into Linked HashMap to preserve the order of insertion. 
     
      for(Entry<String, Double> entry: aList) {
    	  product_Review.put(entry.getKey(), entry.getValue());
      }
 
	return product_Review;
    
    }  
    
	      
	  
}

