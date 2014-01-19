package com.gigaspaces.mongodemo;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.gsm.GridServiceManager;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitDeployment;
import org.openspaces.core.GigaSpace;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by kobi on 1/19/14.
 */
public class MongoExample {



    public static void main(String[] args) {

        Admin admin = new AdminFactory().addGroup("kobi").createAdmin();
        admin.getGridServiceAgents().waitForAtLeastOne(30, TimeUnit.SECONDS);
        admin.getGridServiceManagers().waitForAtLeastOne(30, TimeUnit.SECONDS);

        GridServiceManager gsm = admin.getGridServiceManagers().getManagers()[0];
        Map<String, String> contextProperties = new HashMap<String, String>();
        contextProperties.put("mongo.db", "kobidb");
        contextProperties.put("mongo.host", "127.0.0.1");
        contextProperties.put("mongo.port", "10001");

        ProcessingUnit space = gsm.deploy(processingUnitDeploymentDeployment("space", contextProperties)
                .clusterSchema("partitioned-sync2backup")
                .numberOfInstances(1)
                .numberOfBackups(1));
        space.waitFor(space.getPlannedNumberOfInstances());
        ProcessingUnit mirror = gsm.deploy(processingUnitDeploymentDeployment("mirror", contextProperties));
        mirror.waitFor(mirror.getPlannedNumberOfInstances());

        GigaSpace gigaSpace = space.getSpace().getGigaSpace();
        Car car = new Car();
        car.setId(0);
        car.setName("Toyota");
        car.setModel(2014);

        gigaSpace.write(car);

        Car readCar = gigaSpace.read(new Car());




    }

    protected static ProcessingUnitDeployment processingUnitDeploymentDeployment(String name, Map<String, String> contextProperties) {
        String s = System.getProperty("file.separator");
        String version = "1.0-SNAPSHOT";
        File puDeploymentFile =  new File(getLocalRepository() + "gs-mongo-demo" + s + name + s + version + s + name + "-" + version + ".jar");
        ProcessingUnitDeployment puDeployment = new ProcessingUnitDeployment(puDeploymentFile).name(name);
        for (String key : contextProperties.keySet()) {
            puDeployment.setContextProperty(key, contextProperties.get(key));
        }
        return puDeployment;
    }

    public static String getLocalRepository() {
        String s = System.getProperty("file.separator");
        return System.getProperty("user.home") + s + ".m2" + s + "/repository/";
    }

}
