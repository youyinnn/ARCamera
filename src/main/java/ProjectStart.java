import com.github.youyinnn.youdbutils.YouDbManager;
import com.github.youyinnn.youdbutils.exceptions.Log4j2FilterException;
import com.github.youyinnn.youwebutils.second.JsonHelper;
import com.github.youyinnn.youwebutils.second.JwtHelper;
import com.jfinal.config.*;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.template.Engine;
import controller.UserController;

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
        me.add("/user", UserController.class,"/");
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
        me.add(new DruidStatViewHandler("/druid"));
    }

    @Override
    public void afterJFinalStart() {
        YouDbManager.youDruid.initMySQLDataSource();
        YouDbManager.signInStatProxyFilter();
        try {
            YouDbManager.youLog4j2Filter().setLog4j2FilterWithAllOff();
            YouDbManager.signInLog4j2ProxyFilter();
        } catch (Log4j2FilterException e) {
            e.printStackTrace();
        }
        YouDbManager.scanPackageForModel("model");
        YouDbManager.scanPackageForService("service");
        JwtHelper.initJWTWithHMAC512("youyinnn","youyinnn000");
        try {
            JsonHelper.initJsonPool(PathKit.getWebRootPath() + "/WEB-INF/classes/conf/jsonTemplate.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("JFinal Start!");
    }

    @Override
    public void beforeJFinalStop() {
        System.err.println("JFinal Stop!");
    }
}
