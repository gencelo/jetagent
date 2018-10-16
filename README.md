# jetagent
This repository created for 'EmlakJet hiring challenge'

##for Elastic search

docker pull docker.elastic.co/elasticsearch/elasticsearch:6.4.2
docker-compose up (-d is optional for background starting)

##for JAVA project 

mvn clean package
cd target
java -jar [jar-name].jar

NOT: JAVA project need running elasticSearch cluster when starting.


##Endpoint examples

- http://localhost:8080/api/v1/posts --> for all posts
- http://localhost:8080/api/v1/posts?q=sat%C4%B1l%C4%B1k&max_price=2600000&min_price=289001&min_square=131&max_square=190&room_count=1%2B1&room_count=4%2B1 -> for all filter