package app;

import app.model.SmartPhone;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Created by Khanhdb@eway.vn on 1/17/17.
 */
public class Repository {

    private BeanUtilsBean beanUtilsBean = new BeanUtilsBean() {
        @Override
        public void copyProperty(Object dest, String name, Object value)
                throws IllegalAccessException, InvocationTargetException {
            if (value == null) return;
            super.copyProperty(dest, name, value);
        }
    };

    public LinkedHashMap<String, SmartPhone> getSmartphones() {
        return smartphones;
    }

    private LinkedHashMap<String, SmartPhone> smartphones = new LinkedHashMap<>();


    public SmartPhone getModel(String databaseId) {
        SmartPhone model =smartphones.get(databaseId);
        if (model == null) {
            throw new RuntimeException("database_id " + databaseId + " not found");
        }
        return model;
    }

    public SmartPhone create(SmartPhone smartPhone){
        smartPhone.setDatabaseId(UUID.randomUUID().toString());
        smartPhone.setCreatedAt(new Date());
        smartphones.put(smartPhone.getDatabaseId(), smartPhone);
        return  smartPhone;
    }

    public SmartPhone update(String databaseId, SmartPhone newSmartPhone){
        SmartPhone oldSmartPhone = this.getModel(databaseId);
        if(oldSmartPhone == null){
            throw new RuntimeException("databaseId: " + databaseId + " Not found");
        }
        try {
            beanUtilsBean.copyProperties(oldSmartPhone, newSmartPhone);
            oldSmartPhone.setUpdatedAt(new Date());
        }catch (Exception e){
            throw new RuntimeException("faile to update", e);
        }
        return oldSmartPhone;
    }

    public SmartPhone delete(String databaseId){
        SmartPhone deletedSmartphone = smartphones.remove(databaseId);
        if (deletedSmartphone == null) {
            throw new RuntimeException("database_id " + databaseId + " not found");
        }
        return deletedSmartphone;
    }
}