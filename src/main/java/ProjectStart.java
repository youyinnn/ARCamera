import com.github.youyinnn.youdbutils.YouDbManager;
import com.jfinal.config.*;
import com.jfinal.kit.PathKit;
import com.jfinal.template.Engine;
import controller.UserController;
import utils.JWTHelper;
import utils.JsonHelper;

import java.io.IOException;

/**
 * @author youyinnn
 */
public class ProjectStart extends JFinalConfig{

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setUrlParaSeparator("&");
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/arc/user", UserController.class,"/");
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void afterJFinalStart() {
        YouDbManager.youDruid.initMySQLDataSource();
        YouDbManager.youLog4j2Filter().setLog4j2FilterWithAllOff();
        YouDbManager.signInStatProxyFilter();
        YouDbManager.signInLog4j2ProxyFilter();
        YouDbManager.scanPackageForModel("model");
        YouDbManager.scanPackageForService("service");
        JWTHelper.initJWTWithHMAC512("youyinnn","youyinnn000");
        try {
            JsonHelper.initJsonPool(PathKit.getWebRootPath() + "/WEB-INF/classes/conf/jsonTemplate.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("JFinal Start!");
    }
}
