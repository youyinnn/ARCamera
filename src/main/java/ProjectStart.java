import com.github.youyinnn.youdbutils.YouDbManager;
import com.github.youyinnn.youquickjetty.YouJetty;
import com.jfinal.config.*;
import com.jfinal.template.Engine;
import controller.UserController;

/**
 * @author youyinnn
 */
public class ProjectStart extends JFinalConfig{

    public static void main(String[] args) {

        YouDbManager.youDruid.initMySQLDataSource();
        YouDbManager.youLog4j2Filter().setLog4j2FilterWithAllOff(true);
        YouDbManager.signInLog4j2ProxyFilter();
        YouDbManager.scanPackageForModel("model");
        YouDbManager.scanPackageForService("service");


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
        System.out.println("***");
    }
}
