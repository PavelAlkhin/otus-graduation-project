mvn clean package
heroku deploy:jar target/blgtest-1.jar --app blgtest
pause