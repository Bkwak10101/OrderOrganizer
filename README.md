# Order Organizer

Java application for organizing order fulfillment schedule for ISF employees. The application allows for assigning a list of orders to employees in such a way that they can fulfill as many orders as possible.
## Requirements
To run the application, you need to have the following installed:

* Java 17
* Maven

## Installation

1) Clone the repository:

``git clone https://github.com/Bkwak10101/OrderOrganizer.git``
2) Navigate to the project directory:

``cd OrderOrganizer``

3) Build the project using Maven:

``mvn clean package``

4) Run the application, providing the path to the configuration files:

``java -jar target/order-organizer-1.0-SNAPSHOT.jar path/to/store.json path/to/orders.json
``
## Usage

The application reads configuration files in JSON format:

* store.json - store configuration
* orders.json - list of orders for a given day

The application assigns a list of orders to employees based on their availability and the order value (if specified in the configuration files). The goal is to maximize the number and value of orders fulfilled by employees. If an order cannot be fulfilled on time, it is skipped.
