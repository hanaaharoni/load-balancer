# LOAD-BALANCER
Author: Hanaa HARONI

Load balancer implementing Random and Round Robin algorithms

## Basic Assumptions
1. I've used main as the a consumer interface.
2. You'll find Thread.sleep() across the app in order to simulate real world requests and responses.
3. Feel free to modify the numbers in Load Balancer / Provider for various scenarios
(I wanted to worry less about handling user input)
4. Currently it's set to use RandomLoadBalancer. in order to point it to Round Robin 
modify BasicModule.class.

## The application 
Once the application is started, it will initialize providers and start processing requests;

### Instructions to run the app
1. Clone git repository
2. Execute the command `mvn install` to build the application using Maven.
3. Execute the command `java -jar target/loadbalancer-1.0-SNAPSHOT.jar` to run the app.
4. ctrl+c to interrupt the process.
