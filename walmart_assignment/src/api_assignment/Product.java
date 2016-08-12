/* NeethuSimon Walmart_api_assignment
 *Product has properties
*(instance variables) that matches fields on Search Form */
package api_assignment;

public class Product {
	/* field in the Search form to enter a product name for search*/	
	private String productName;
	
	/* field in the Search form to specify a filter*/
	private String filter;
	
 /* getter */
	public String getProductName() {
		return productName;
	}
	
	public String getFilter() {
		return filter;
	} 
	
/* setter */
	public void setProductName(String product) {
		this.productName = product;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
