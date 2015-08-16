mvn clean install gpg:sign -DskipTests -T3C -f chrl-bom\pom.xml

mvn clean install gpg:sign -DskipTests -T3C -f chrl\pom.xml
mvn clean install gpg:sign -f chrl-archetypes\chrl-spring-sample\pom.xml