# Karajan : mapping, enrichissement et transformation de flux.

#* OUTDATED SINCE 2010 !!!*


## Introduction

Karajan est un service de mapping de données. Il permet (entre autres) de les agréger. Ce service traite aussi bien les flux que les fichiers et ce dans différents formats d’entrée et de sortie (JSON, XML, CSV, CRE, etc...). Il permet de lier deux modèles objets : un en façade et l'autre dépendant d'une architecture en couches basses. Couplé à Apache Camel, il permet de réaliser de la transcodification et de l'enrichissement de messages.

## Utilisation

### Utilisation

Création de classes d’action qui appelleront vos DAO (pouvant servir à de l’agrégation de données) 
et paramétrage du fichier XML de configuration. On peut placer plusieurs fichiers de configuration, 
seul le nom de l’unité d’œuvre doit être unique.

Le répertoire contenant les fichiers de configuration doit être passé en paramètre du processeur de Karajan :

    ConfProcessor.configure(new File("/home/xavier/workspace/fileTestCamel/conf/");

Puis l'appel au moteur de mapping se passe ainsi : 

    String request = "... some content ..."; 
    String result = ConfProcessor.unitsOfWork.get("Authentication").processFromString(request); 
    
`Authentication` correspond au nom de l'unité d'œuvre.

### Configuration

Voici quelques exemples : 

    <unitOfWork name="Authentication">
        <description>Authentification d'un user sur le système</description>
        <input action="authenticate" format="json" inputObject="userJson" outputObject="userIphone">
            <field sourceName="login" targetName="login" source="userJson" 
                   target="userIphone" sourceType="string" targetType="string" />
            <field sourceName="password" targetName="password" source="userJson" 
                   target="userIphone" sourceType="string" targetType="string" />
        </input>
        
        <output action="org.giwi.Authentication" inputObject="userIphone" outputObject="userJson" format="json">
            <field sourceName="login" targetName="login" source="userIphone" 
                   target="userJson" sourceType="string" targetType="string" />
            <field sourceName="name" targetName="name" source="userIphone" 
                   target="userJson" sourceType="string" targetType="string" />
            <field sourceName="forname" targetName="forname" source="userIphone" 
                   target="userJson" sourceType="string" targetType="string" /> 
            <field sourceName="gender" targetName="gender" source="userIphone" 
                   target="userJson" sourceType="string" targetType="string" /> 
            <field sourceName="customerId" targetName="customerId" source="userIphone" 
                   target="userJson" sourceType="string" targetType="string" />
        </output>
        <dataObjects> 
            <transObj name="userJson" className="net.sf.json.JSONObject" isCollection="false" />
            <transObj name="userIphone" className="org.giwi.iphone.model.User" isCollection="false" />
        </dataObjects>
    </unitOfWork>

Le nom (`name`) de cette unité d’œuvre doit être unique.

#### Balise `input` :

Cette unité d’œuvre a une action d’entrée « authenticate », le format du message d’entrée est `json`
(il pourrait être XML, CSV ou autre), l’objet d’entrée (nom du message d’entrée) est `inputObject`, 
il est décrit dans la section `dataObjects`. L’objet pivot connu du DAO est `outputObject`, il est décrit
dans la section `dataObjects`. Cette balise a des balises fille `field` servant au mapping de données 
entre le message d’entrée et l’objet pivot d’entrée.

#### Balise `field` dans le cas de `input`

+ `source` : nom du message d’entrée
+ `sourceName` : nom de l’attribut du message d’entrée
+ `sourcetype` : nature de l’attribut (cf DTD)
+ `target` :  nom de l’objet pivot d’entrée
+ `targetName` : nom de l’attribut de l’objet pivot d’entrée
+ `targetType` : nature de l’attribut (cf DTD)

Les types complexes peuvent être définis ainsi : 

    <field sourceName="produit.partenaireProduit.numeroPartenaire" targetName="noPtn" 
           source="listOfPolicies" target="consultResult" sourceType="int" targetType="int" />

Un objet `listOfPolicies` est ici une collection. Chaque item de cette collection a un attribut `produit` 
qui est un objet ayant un attribut `partenaireProduit`qui lui-même a un attribut `numeroPartenaire` qui est un entier.

#### Balise `output`

Cette unité d’œuvre a une action métier à réalisé définie dans la classe action définie dans l’attibut 
`action` de cette balise. L’objet pivot de sortie de l’action métier est défini par `inputObject`, il 
est décrit dans la section `dataObjects`. Le message de sortie est défini par `outputObject`, il est 
décrit dans la section `dataObjects`.

Cette balise a des balises fille `field` servant au mapping de données entre l’objet pivot de sortie 
de l’action métier et le message de sortie.

#### Balise `field` dans le cas de `output`

+ `source` nom de l’objet pivot de sortie de l’action métier
+ `sourceName` : nom de l’attribut du l’objet pivot de sortie de l’action métier
+ `sourcetype` : nature de l’attribut (cf DTD)
+ `target` : nom du message de sortie
+ `targetName` : nom de l’attribut du message de sortie
+ `targetType` : nature de l’attribut (cf DTD)

Les types complexes peuvent être définis ainsi : 

    <field sourceName="produit.partenaireProduit.numeroPartenaire" targetName="noPtn" 
           source="listOfPolicies" target="consultResult" sourceType="int" targetType="int"/>

Un objet `listOfPolicies` est ici une collection. Chaque item de cette collection a un attribut `produit`
qui est un objet ayant un attribut `partenaireProduit` qui lui-même a un attribut `numeroPartenaire` qui est un entier.

#### Balise `dataObjects`

`dataObjects` défini tous les objets nécessaires au mapping. Ces objets sont définis par la balise fille `transObj`.

#### Balise `transObj`

+ `name` : nom unique de l’objet dans l’unité d’œuvre, il correspond aux `source` et `target` des balises `field`, 
mais également au `inputObjetc` et `outputObject` des balise `input` et `output`.
+ `className` : défini la classe réelle de l’objet, la classe `net.sf.json.JsonObject` a une notion particulière 
dans Karajan.
+ `isCollection` :
  + `false` : défini s’il s’agit d’un objet du type `className`
  + `true`  : d’une liste d’objets de type `className`.
  
De la même manière, on peut définir un processus de mapping complexe : 

    <unitOfWork name="PolicyDetail">
        <description>Détail d'un contrat</description>
        <input outputObject="policyDetail" action="policeDetail" format="json" inputObject="jsonPolicy" >
            <field sourceName="policyNumber" sourceType="string" targetName="policyNumber" 
                   targetType="string" source="jsonPolicy" target="policy"/>
        </input>
        <output outputObject="jsonPolicy" action="org.giwi.iphone.actions.PolicyDetailAction" format="json" inputObject="policy" >
            <field sourceName="performanceContrat" sourceType="double" targetName="perf" 
                   targetType="double" source="policy" target="jsonPolicy"/>
            <field sourceName="dateEcheanceContrat" sourceType="date" targetName="policyDate" 
                   targetType="date" source="policy" target="jsonPolicy" /> 
            <field sourceName="listeSupportContrat" sourceType="list" targetName="listSpp" 
                   targetType="list" source="policy" target="jsonSpps"/>
            <field sourceName="nombrePart" sourceType="double" targetName="nbPart" 
                   targetType="double" source="support" target="jsonSpp"/>
            <field sourceName="performance" sourceType="double" targetName="perf" 
                   targetType="double" source="support" target="jsonSpp"/>
            <field sourceName="repartition" sourceType="double" targetName="repart" 
                   targetType="double" source="support" target="jsonSpp"/>
        </output>
        <dataObjects>
            <transObj name="jsonPolicy" className="net.sf.json.JSONObject" isCollection="false"/>
            <transObj name="jsonSpp" className="net.sf.json.JSONObject" isCollection="true"/>
            <transObj name="jsonSpps" className="net.sf.json.JSONObject" isCollection="false"/>
            <transObj name="policyDetail" className="org.giwi.iphone.model.Policy" isCollection="false"/>
            <transObj name="policy" className="org.giwi.contrat.ContratDetaille" isCollection="false"/>
            <transObj name="support" className="org.giwi.modele.support.SupportContrat" isCollection="true"/>
        </dataObjects>
    </unitOfWork>

