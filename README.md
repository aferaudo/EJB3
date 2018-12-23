# EJB3
Seconda esercitazione del corso di Sistemi Distribuiti M - Ingegneria Informatica Bologna

Sono stati usati **MySQL** come datasource e **WildFly** come application server.

## MOTIVAZIONI
Il Prof nelle sue soluzioni usa **JBoss 4.2.x**, versione abbastanza vecchia e ormai poco utilizzata. Quindi quello che vogliamo fare è usare **WildFly** anziché **JBoss 4.2.x**.

Lo scopo di questa guida è quello di capire come configurare **WildFly**, avendo noi riscontrato un sacco di problemi in questa fase.

**Ovviamente cercate di usare questa guida solo per la configurazione di WildFly e non per copiare direttamente la soluzione.**

### GUIDA
Ci sono un paio di differenze tra le due tecnolgie da tenere in mente prima di iniziare:

- in JBoss 4.2.x abbiamo un file di configurazione per sottosistema, in WildFly uno solo;

- il comando da lanciare non è più *./run.sh* ma *./standalone.sh*
	
Il file di configurazione, come già accennato, è unico e in particolare è situato nella directory **standalone/configuration/standalone.xml**.

**N.B.** I restanti file servono per la configurazione del cluster e verranno utilizzati solo se wildfly viene lanciato nella versione **full** (*./standalone.sh --server-config=standalone-full.xml*).


La prima cosa che dovete fare, se come noi avete usato **MySQL**, è aggiungere il suo modulo, in quanto in questa versione di WildFly non è presente. Noi per crearlo abbiamo seguito una guida che potete trovare su questo [link](https://synaptiklabs.com/posts/adding-the-mysql-jdbc-driver-into-wildfly/).

Ovviamente cambiate i parametri in base alla libreria che state utilizzando. Ad esempio noi abbiamo usato la libreria *mysql-connector-java-5.1.16-bin.jar*, quindi il file *module.xml* diventa:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<module name="com.mysql" xmlns="urn:jboss:module:1.5">
    <resources>
        <resource-root path="mysql-connector-java-5.1.16-bin.jar" />
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```


Le maggiori difficoltà che abbiamo riscontrato sono state nella configurazione di JNDI, in quanto in questa versione di JBoss cambia leggermente la sintassi.

Per prima cosa bisogna individuare cosa vogliamo registrare nel nostro sistema di nomi, nel nostro caso un **datasource**.
Aprendo il file **standalone.xml** troveremo un esempio di registrazione di datasource che riportiamo di seguito:

```xml
<datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
                    <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
                    <driver>h2</driver>
                    <security>
                        <user-name>sa</user-name>
                        <password>sa</password>
                    </security>
</datasource>
<drivers>
	<driver name="h2" module="com.h2database.h2">
	    <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
	</driver>
</drivers>
```

Usando questo file come linee guida possiamo inserire il nostro datasource all'interno del file standalone.xml oppure all'interno del file **sampleproject-ds.xml**, presente nel codice di partenza dato dal Prof.

Considerando il codice xml precedente il nostro diventa:

```xml
<datasource jndi-name="java:jboss/datasources/MySQLDS" pool-name="MySQLDS" enabled="true" use-java-context="true">
				  <!-- configurazione delle proprietà di jndi -->
                    <connection-url>jdbc:mysql://localhost/my_first_db</connection-url>
                    <driver>com.mysql</driver>
                    
                    <!-- da cambiare -->
                    <security>
                        <user-name>root</user-name> 
                        <password>my_password</password>
                    </security>
</datasource>
<drivers>
	<driver name="com.mysql" module="com.mysql">
	                        <driver-class>com.mysql.jdbc.Driver</driver-class>
	                        <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
	</driver>
</drivers>
```

A questo punto avremo registrato nel sistema di nomi il nostro datasource all'URL **java:jboss/datasources/MySQLDS**. L'unica cosa che ci rimane da fare è usare questo url per recuperare il datasource all'interno del file persistence.xml. Questo ci permetterà in un secondo momento di fare la **dependency injection**.


Per farvi vedere che ci sono delle differenze nella sintassi di JNDI vi riportiamo il file *sampleproject-ds.xml* presente nella soluzione del Prof.

```xml
<local-tx-datasource>
		<jndi-name>SampleProjectDS</jndi-name>
			<connection-url>jdbc:mysql:///sampleProject</connection-url>
	     	<driver-class>com.mysql.jdbc.Driver</driver-class>
	      	<user-name>sampleProject</user-name>
	      	<password>sampleProject</password>
</local-tx-datasource>
```

L'ultima parte dove potrete trovare difficoltà è sulla configurazione di **JMS** su WildFly. Potreste seguire un procedimento simile al precedente oppure usare questa [guida](https://gianlucacosta.info/wildfly-jms-tutorial), in modo tale da vedere anche come si può configurare WildFly attraverso l'interfaccia grafica.


**N.B.** La maggior parte dei problemi sono solo nella configurazione, una volta fatto questo dovrebbe essere abbastanza semplice.
