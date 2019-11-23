package pl.sebue.magisterka.grs.model.algorithm;

import weka.core.Instances;
import weka.experiment.InstanceQuery;

public class WakaCrossValidation {

    public void sth(){
        try {
            InstanceQuery query = new InstanceQuery();
            query.setUsername("root");
            query.setPassword("insightadmin");
            query.setQuery("select * from gamestatistic");
            Instances data = query.retrieveInstances();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
