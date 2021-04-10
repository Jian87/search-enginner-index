# search engine
 this is a school project which used to understand how search engine index and its corresponding algorithms.
 
# details
 this is a basic index model, which doesn't include crawl part, you can use some data to do the test.

# how the index works
 each term has several corresponging postinglist based on different algorithms, you can check it from class PostingList.
 
 each postinglist of each term has a space limit: at most 100 docs, I throw the rest docs based on tf-idf/pagerank. 
 
 doc nodes in each postinglist always in decreasing order based on tf or pagerank.

 # how to test
 there are jsp pages and several controllers to help us test.
 
 At first, please prepare some test data and corresponding web graph data.
 
 step 1: modify the data input path in controller method: parse
 
 step 2: run this spring boot app
 
 enter localhost:8080/parse in your browser, wait for the page jumping happens. Once the page jumps to "/", you can input querys to do the test.
 
 
