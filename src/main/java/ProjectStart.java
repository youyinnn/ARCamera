import com.github.youyinnn.youdbutils.YouDbManager;
import com.github.youyinnn.youquickjetty.YouJetty;
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

    public static void main(String[] args) throws IOException {

        YouDbManager.youDruid.initMySQLDataSource();
        YouDbManager.youLog4j2Filter().setLog4j2FilterWithAllOff(true);
        YouDbManager.signInLog4j2ProxyFilter();
        YouDbManager.scanPackageForModel("model");
        YouDbManager.scanPackageForService("service");
        JWTHelper.initJWTWithHMAC512("youyinnn","youyinnn000");
        JsonHelper.initJsonPool(PathKit.getWebRootPath() + "/src/main/resources/conf/jsonTemplate.json");

        YouJetty youJetty = YouJetty.initServer(args);
        youJetty.startAndJoin();

    }

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

    }

    @Override
    public void afterJFinalStart() {

    }
}
