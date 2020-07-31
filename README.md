# README #

### Multi-threaded Dictionary ###
* Version 1.0

### Summary ###
* This project was developed with Java Development Kit (JDK) 14
* Framework: IntelliJ IDEA Community Edition 2020.1 
* GUI: Swing

### Executing ###
* For executing Server JAR file (up to 5 dictionaries):
    * java –jar DictionaryServer.jar <port> <dictionary-file-1> <dictionary-file-2>  
* Dictionary files:
    * Must be txt files
    * One word-meaning pair by line separated with colon.
    * First line must have the language.
* For executing Client JAR file:
    * java –jar DictionaryClient.jar <server-address> <server-port>
### Dependencies ###
* These are the most uncommon libraries utilized:
    * JUnit Jupiter 5.4.2: for testing. 
    * Apache Common Text 1.8: for implementation of Levenshtein Distance (similar words). 


### Testing ###
* 15 tests are provided. It was run with the functionality of IntelliJ. 
