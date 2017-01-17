package app;

import app.model.SmartPhone;
import io.vertx.JsonUtils;
import io.vertx.WebServer;
import io.vertx.core.Vertx;
import io.vertx.data.JsonRequest;
import io.vertx.data.JsonRespone;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanhdb@eway.vn on 1/17/17.
 */
public class WebServerExample extends WebServer<AppConfig> {

    private Repository repository = new Repository();

    public WebServerExample(Vertx vertx, AppConfig appConfig) {
        super(vertx, appConfig);
    }

    @Override
    protected void setupRouter(Router router) throws Exception {
           router.get("/").handler(this::displayHomePage);
           router.get("/v1/smartphones.json").handler(this::getModels);
           router.get("/v1/smartphones/:id.json").handler(this::getModelById);
           router.put("/v1/smartphones.json").handler(this::createModel);
           router.put("/v1/smartphones/:id.json").handler(this::updateModel);
           router.delete("/v1/smartphones/:id.json").handler(this::deleteModelById);
    }

    private void displayHomePage(RoutingContext context){
        JsonRespone<String> jsonRespone = new JsonRespone<>(context, 200);
        jsonRespone.setData("everything is OK");
        jsonRespone.write();
    }

    private void getModels(RoutingContext context){
        JsonRespone<List<SmartPhone>> respone = new JsonRespone<>(context, 200);
        respone.setData(new ArrayList<>(repository.getSmartphones().values()));
        respone.write();
    }

    private void getModelById(RoutingContext context){
        JsonRespone<SmartPhone> respone = new JsonRespone<>(context, 200);
        String id = context.request().getParam("id");
        respone.setData(repository.getModel(id));
        respone.write();
    }

    private void createModel(RoutingContext context){
        JsonRespone<SmartPhone> respone = new JsonRespone<>(context, 200);
        JsonRequest<SmartPhone> request = JsonUtils.decode(context, SmartPhone.class);
        SmartPhone smartPhone = request.getData();
        SmartPhone createdModel = repository.create(smartPhone);        // luu vao trong repository
        respone.setData(createdModel);
        respone.write();
    }

    private void updateModel(RoutingContext context){
        //update trong repository
       JsonRequest<SmartPhone> request = JsonUtils.decode(context, SmartPhone.class);
       JsonRespone<SmartPhone> respone = new JsonRespone<>(context, 200);
       SmartPhone smartPhone = request.getData();
       String databaseId = smartPhone.getDatabaseId();
       SmartPhone updatedSmartphone = repository.update(databaseId, smartPhone);
       respone.setData(updatedSmartphone);
       respone.write();
    }

    private void deleteModelById(RoutingContext context){
         JsonRespone<SmartPhone> respone = new JsonRespone<>(context, 200);
         JsonRequest<SmartPhone> request = JsonUtils.decode(context, SmartPhone.class);
         SmartPhone deletedSmartphone = repository.delete(request.getData().getDatabaseId());
         respone.setData(deletedSmartphone);
         respone.write();
    }
}
