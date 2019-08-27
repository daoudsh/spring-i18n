@echo on
call mvn clean install -DskipTests
call mvn spring-boot:run -DskipTests=true

pause