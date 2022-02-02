# Gi'TSE
A desktop application for managing a local `Gitlab` instance thanks to its API through a graphical user interface built with Java Swing.  

### Notes : 

- An executable jar file is included so that you can run the project immediately.
- The application is a maven project, if you wish to change that, consider reintegrating the dependencies
      in the pom.xml file manually.
- The code is commented in French, and so the Javadoc is also generated in French. Will eventually try to translate it as well.
- The provided csv files serve only as examples to show the correct form, change them accordinly.
- The application is uses by default the Télécom Saint-Etienne Gitlab instance, thus the reason behind its logos. Feel free to change them as you change the instance url.
- The application securely stores passwords with an [AES](https://www.baeldung.com/java-aes-encryption-decryption) ecryption system.

### How to launch :

> Through the jar file, or clone the entire project locally and run it through the Main class.


### User guide :

> The application starts by introducting a loading screen and then a login screen, where the user is invited to enter 
his/her credentials for the indicated `Gitlab` instance.If the authentification process is successful, the user will 
then have access to the functionalities of the application beginning with a `Home page`.






