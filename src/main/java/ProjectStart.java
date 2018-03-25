import com.github.youyinnn.youdbutils.YouDbManager;
import com.github.youyinnn.youdbutils.druid.YouDruid;
import com.github.youyinnn.youdbutils.druid.filter.YouLog4j2FilterConfig;
import com.github.youyinnn.youdbutils.druid.filter.YouStatFilterConfig;
import com.github.youyinnn.youdbutils.exceptions.DataSourceInitException;
import com.github.youyinnn.youdbutils.exceptions.YouDbManagerException;
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
        YouLog4j2FilterConfig log4j2FilterConfig = new YouLog4j2FilterConfig();
        log4j2FilterConfig.enableStatementExecutableSqlLog();
        YouStatFilterConfig statFilterConfig = new YouStatFilterConfig();
        try {
            YouDbManager.signInYouDruid(YouDruid.initMySQLDataSource("a", true, log4j2FilterConfig, statFilterConfig));
            YouDbManager.scanPackageForModelAndService("model", "service", "a");
        } catch (DataSourceInitException | YouDbManagerException e) {
            e.printStackTrace();
        }
        JwtHelper.initJWTWithHMAC512("youyinnn","youyinnn000");
        try {
            JsonHelper.initJsonPool(PathKit.getWebRootPath() + "/WEB-INF/classes/conf/jsonTemplate.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void beforeJFinalStop() {
        System.err.println("JFinal Stop!");
    }
}
