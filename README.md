# Facile

Facile is a Graphical User Interface for MySQL server. Following functionalities are implemented in this project:
* User can login to the database server.
* User can create the database.
* User can see the contents of the database.
* User can create the table in the database.
* User can see the contents of the table in the database.
* User can insert the values/rows in the table of the database.
* User can update the values/rows in the table of the database.
* User can delete the values/rows in the table of the database.
* User can change the properties associated with column: its type,
index and nullable.
* User can delete the table in the database.
* User can delete the database.
* User can logout from the database server.


## Installation 

1. Downlaod the tomcat server and extract it to desired location. 

    [Apache Tomcat 8](https://tomcat.apache.org/download-80.cgi "Tomcat")
    

2. Download the Java connecter Platform Independant for your MySQL server version.

    [MySQL Java Connector](https://dev.mysql.com/downloads/connector/j/ "J connector")    

    After downloading paste extract it and copy the jar file to

    ```
    (Tomcat Home)/lib/
    ```

3. Downlaod the release package. 

    [Facile](https://github.com/talalwaseem50/AP/releases/download/v1.0/Facile.war "Facile")    

    Paste the downloaded `.war` file in

    ```
    (Tomcat Home)/webapps/
    ```

4. Run Tomcat

    ```
    (Tomcat Home)/bin/startup.sh
    ```

5. Go to

    [http://localhost:8080/Facile](http://localhost:8080/Facile "localhost")   


## Working 

* Login page

![Login](https://user-images.githubusercontent.com/14111173/122586054-9f761880-d075-11eb-84c8-6ce673e187b5.png)

* Main page

![Main](https://user-images.githubusercontent.com/14111173/122586198-cdf3f380-d075-11eb-9039-94d74c85e5ff.png)

* Table Structure page

![Table Structure](https://user-images.githubusercontent.com/14111173/122586342-fc71ce80-d075-11eb-9c1a-ddf83c0d7e5a.png)

* Table Rows page

![Table Rows](https://user-images.githubusercontent.com/14111173/122586245-dea46980-d075-11eb-99e4-0c7de119f80f.png)
