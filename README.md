# HBA
Hotel Booking Application

# General description 

This application aims to help both clients and hotels by offering them a win-win circumstance. On one side, clients can schedule their trips using the application and on the other side, hotels can advertise their business.
The variety of hotels clients can choose from are grouped by price, services, filters and reviews; the photos with the hotel rooms and amenities are also displayed. The information helps the customers decide which trip destination to pick and the intuitive interface eases the process of reserving a room.

# Techonologies used
- Java 16
- JavaFX (as GUI)
- Maven (as build tool)
- Nitrite Database

# Prerequisites

To be able to install and run this project, please make sure you have installed Java 11 or higher. Otherwise, the setup will note work! To check your java version, please run java -version in the command line.

It would be good if you also installed Maven to your system. To check if you have Maven installed run mvn -version.

Make sure you install JavaFX SDK on your machine, using the instructions provided in the Official Documentation. Make sure to export the PATH_TO_FX environment variable, or to replace it in every command you will find in this documentation from now on, with the path/to/javafx-sdk-16/lib.

# Set Up & Run

To set up and run the project locally on your machine, please follow the next steps.

## Clone the repository
Clone the repository using:
```
git clone https://github.com/fis2021/HBA
```
## Verify that the project Builds locally
Open a command line session and cd SimpleRegistrationExample. If you have installed all the prerequisites, you should be able to run any of the following commands:

```
mvn clean install
```
## Open in IntelliJ IDEA
To open the project in IntelliJ idea, you have to import it as a Maven project. After you import it, in order to be able to run it, you need to set up your IDE according to the official documentation. Please read the section for Non-Modular Projects from IDE. If you managed to follow all the steps from the tutorial, you should also be able to start the application by pressing the run key to the left of the Main class.

## Run the project with Maven 

The project has already been setup for Maven and Gradle according to the above link. To start and run the project use one of the following commands:

```
mvn javafx:run or ./mvnw javafx:run (run the run goal of the javafx maven plugin)
```

You should see an application starting, that looks like this:

![image](https://user-images.githubusercontent.com/71551404/118410662-6b0dd780-b645-11eb-89e7-c197d883a74f.png)

Try to create your account with sign up button. It will get you to the sign up screen:

![image](https://user-images.githubusercontent.com/71551404/118410725-c17b1600-b645-11eb-9ed2-b15b4f4781e1.png)

Pay attention to email and password. You should introduce a real email and a complex password: 
- at least 10 characters length
- at least one upper case letter
- at least one digit 
- at least one special character

If your registration was successfull you will be able to see your account (as a hotel manager or a customer): 

(customer view)
![image](https://user-images.githubusercontent.com/71551404/118410823-4b2ae380-b646-11eb-885f-c83fc67662cc.png)

(hotel manager view)
![image](https://user-images.githubusercontent.com/71551404/118410858-76adce00-b646-11eb-95dd-526d44a80b13.png)

as a hotel manager you can update your hotel's info using "edit" button and, also, you can add new rooms using "add room" button.

as a customer you can view all the registered hotels, to make a reservation and to cancel a reservation.






