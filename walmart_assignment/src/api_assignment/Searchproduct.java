/*  NeethuSimon Walmart_api_assignment
 *  managed bean that contains a reference to Product model object, whose properties
 *  are mapped to fields on Search page.
 *  contains action controller method Search which get invoked on clicking the search button in the search page
 *  Recommend.xhtml is displayed if there is a product recommendation for the searched product
 *  NoRecommend.xhtml is displayed if there is no product recommendation for the searched product
 */

package api_assignment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.json.JSONException;


@ManagedBean
public class Searchproduct {
    
/* itemid of the the first item in the search response*/
     String itemId;
     

/*product object whose properties are mapped to fields on Search page.*/
	private Product product = new Product();
	
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
 
 /* stores itemname and averageoverallrating*/
	  Map<String,Double>itemIdrating = new HashMap<String,Double>();

/* stores itemname and averageoverallrating sorted by averageoverallrating desc*/
	  
	  Map<String,Double> productReview = new LinkedHashMap<String, Double>();
	  
	  
	  public Map<String, Double> getProductReview() {
			return productReview;
		}

		public void setProductReview(Map<String, Double> productReview) {
			this.productReview = productReview;
		}
	
	 private Searchwebservicemethods prodrecommend = new Searchwebservicemethods();
	
	
/* action controller method which get invoked on clicking search button in the Search form.
 * navigate to Recommend.xhtml which displays product recommendations based on review sentiments if the user 
 * searched for a valid product. Else navigate to NosuchProduct.xhtml .
 */
public String search() throws JSONException, IOException 
{    
	 Properties prop = new Properties();
	 InputStream input = null;
	
	
	try{
		
       //input = new FileInputStream("/WEB-INF/config.properties");
       //prop.load(input);
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		prop.load(ec.getResourceAsStream("/WEB-INF/config.properties"));
       String apikey= prop.getProperty("apiKey");
       String url = prop.getProperty("api_url");
       //System.out.println(apikey);
       
		
/* method return the itemid of the first product  in the  search for products based upon 
* a user-provided search string and search filter
*/
	   itemId = prodrecommend.getFirstItemId(product.getProductName(),product.getFilter(),apikey,url);
	   
/*Itemid of the first 10 product recommendations*/
      List<String>itemIds= new ArrayList<String>();
	 	
/*Uses the first item in the search response as input for a product recommendation search
* and returns the itemid of the first 10 product recommendations
*/
      itemIds = prodrecommend.getprodrecommend(itemId,apikey,url);
 
/*for each item in the product recommendation search , retrieve the averageoverallrating
*/
      itemIdrating = prodrecommend.getitemIdRating(itemIds,apikey,url);
	   
/*sort every item in product recommendation search based on averageoverallrating 
*/
       productReview = prodrecommend.sortItemIdRating(itemIdrating);
      
/*Recommend.xhtml is displayed with product Recommndations ranked by review 
*/
	return "recommend";
	}
	
	catch(Exception ex){
		ex.printStackTrace();
		
/*NoRecommend.xhtml is displayed */
		return "norecommend";
	}
	
}



}
