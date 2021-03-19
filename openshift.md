## Deploying the app on Openshift

- Login into the cluster using `oc login`.

- Create a new project.

```
oc new-project sf-quarkus
```

- Clone the `inventory-ms-quarkus` repo.

```bash
git clone https://github.com/ibm-garage-ref-storefront/inventory-ms-quarkus.git
cd inventory-ms-quarkus
```

- Setup the database.

```bash
cd database_setup
./setup_database.sh sf-quarkus
cd ..
```

- Include the OpenShift extension like this:

```
./mvnw quarkus:add-extension -Dextensions="openshift"
```

This will add the below dependency to your pom.xml

```
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-openshift</artifactId>
</dependency>
```

- Now, navigate to `src/main/resources/application.properties` and add the below.

```
quarkus.openshift.env.vars.inventory-mysql-db-host=inventory-mysql
quarkus.openshift.env.vars.inventory-mysql-db-port=3306
quarkus.openshift.env.vars.inventory-mysql-db-username=dbuser
quarkus.openshift.env.vars.inventory-mysql-db-password=password
```

- To trigger a build and deployment in a single step, run the below command.

```
./mvnw clean package -Dquarkus.kubernetes.deploy=true
```

If it is run successfully, you will see something like this.

```
[INFO] [io.quarkus.container.image.openshift.deployment.OpenshiftProcessor] Successfully pushed image-registry.openshift-image-registry.svc:5000/sf-quarkus-openshift/inventory-ms-quarkus@sha256:f985cd157f5b83766dc57352b183003a373ff9de1d2f54baf8a33379ed432773
[INFO] [io.quarkus.kubernetes.deployment.KubernetesDeployer] Deploying to openshift server: https://c103-e.jp-tok.containers.cloud.ibm.com:31780/ in namespace: sf-quarkus-openshift.
[INFO] [io.quarkus.kubernetes.deployment.KubernetesDeployer] Applied: Service inventory-ms-quarkus.
[INFO] [io.quarkus.kubernetes.deployment.KubernetesDeployer] Applied: ImageStream inventory-ms-quarkus.
[INFO] [io.quarkus.kubernetes.deployment.KubernetesDeployer] Applied: ImageStream openjdk-11.
[INFO] [io.quarkus.kubernetes.deployment.KubernetesDeployer] Applied: BuildConfig inventory-ms-quarkus.
[INFO] [io.quarkus.kubernetes.deployment.KubernetesDeployer] Applied: DeploymentConfig inventory-ms-quarkus.
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 190698ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  03:25 min
[INFO] Finished at: 2021-03-19T16:42:32+05:30
[INFO] ------------------------------------------------------------------------
```

- Now create the route as follows.

```
oc expose svc inventory-ms-quarkus
```

- Grab the route.

```
oc get route inventory-ms-quarkus --template='{{.spec.host}}'
```

You will see something like below.

```
$ oc get route inventory-ms-quarkus --template='{{.spec.host}}'
inventory-ms-quarkus-sf-quarkus-openshift.storefront-cn-6ccd7f378ae819553d37d5f2ee142bd6-0000.che01.containers.appdomain.cloud
```

- Now access the endpoint using `http://<route_url>/micro/inventory`.

For instance if using the above route, it will be http://inventory-ms-quarkus-sf-quarkus-openshift.storefront-cn-6ccd7f378ae819553d37d5f2ee142bd6-0000.che01.containers.appdomain.cloud/micro/inventory.
