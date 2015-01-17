This section explains how to install HeidelTime's jar file into the project's local repository, along with its dependencies:

1. Follow the steps in https://code.google.com/p/heideltime/wiki/Maven

2. From JvnTextPro's root folder:
mvn deploy:deploy-file -Durl=file:///path/to/glue-feed/repo/ -Dfile=target/jvntextpro-2.0.jar -DgroupId=jvntextpro -DartifactId=jvntextpro -Dpackaging=jar -Dversion=2.0 -DpomFile=pom.xml

3. From the HeidelTime's kit folder
mvn deploy:deploy-file -Durl=file:///path/to/glue-feed/repo/ -Dfile=target/de.unihd.dbs.heideltime.standalone.jar -DgroupId=de.unihd.dbs -DartifactId=heideltime -Dpackaging=jar -Dversion=1.8 -DpomFile=pom.xml