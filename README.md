#    Sbring Boot Zookeepr client.

### It uses
    Zookeeper 3.7
    Spring Boot 1.5.4.RELEASE
    Java 8
    Curator framework to register services with zookeeper


This application connects to Zookeeper server usring rest paths application can be registered/discovered in cloud environment.
Zookeeper should be up and running for this application to register.

# Test
Use rest client to test .


- GET method - http://localhost:8081/findAll - list all services registered
   
     [
         "Zookeeper_Service",
         "suman"
     ]
- PUT method - http://localhost:8081/create/localhost/82881/suman (add service named suman running on port 82881 with URI http://localhost:82881)
    {
        "serviceId": "suman",
        "host": "localhost",
        "port": 82882,
        "secure": false,
        "uri": "http://localhost:82882",
        "metadata": {}
    }
- GET method http://localhost:8081/find/suman find service information for service suman

   {
       "serviceId": "suman",
       "host": "localhost",
       "port": 82881,
       "secure": false,
       "uri": "http://localhost:82881",
       "metadata": {}
   }
