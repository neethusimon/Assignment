# Assignment

## Workflow

1.Search for products based upon a user-provided search string

2.Use the first item in the search response as input for a product recommendation search

3.Retrieve reviews of the first 10 product recommendations

4.Rank order the recommended products based upon the review sentiments



## Testing

Solution is deployed in AWS 

http://ec2-52-35-123-130.us-west-2.compute.amazonaws.com/walmart_assignment/faces/Search.xhtml

## Environment
JSF 2.2, Tomcatv7.0
specify apiKey and api url in  WebContent/WEB-INF/config.properties

```
apiKey=xxxx
api_url=http://api.walmartlabs.com/v1/
```
