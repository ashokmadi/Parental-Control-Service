# Parental-Control-Service

Implementation of ParentalControlService API for video on Demand platform.

This project requires:
1. Java 8
2. Junit 4
3. Mockito All 1.10.19

Implementation Details:
This Parental Control Service requires movie service reference and list of control levels. It offers isAbleToWatch method with customer’s parental control level preference, a movie id and parental control error reference as input. If the customer is able to watch the movie the ParentalControlService indicate this to the calling client with boolean value true or false.
An error object is used to return the error message to the calling client if any error occurs.

Parental Control Service class is implemented at ashok.parental.control.service.ParentalControlService.

Test Implementation:
Unit Test class is provided at test/ashok.parental.control.service.ParentalControlServiceTest

Running the Test
The Parental Control Service implementation can be tested by running the ParentalControlServiceTest as JUnit.





