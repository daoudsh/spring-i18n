# spring-i18n

This project contains a simple example to export SQL script for spring i18n messages source in database

* login to your account in https://poeditor.com/ 
* export the terms in JSON format.
* run the application by using run.cmd (windows) or run.sh (linux / MacOS)
* go to http://localhost:8080
* upload the exported JSON to the Spring application and it will create SQL script required to update the database

PLEASE be careful when running the script against the real database (make sure to choose the right database ub changing `use DATABASE_NAME ;`)