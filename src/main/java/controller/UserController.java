package controller;

import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;
import com.github.youyinnn.youquickjetty.utils.YouProUtils;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import service.UserService;
import utils.JWTHelper;
import utils.JsonHelper;

import java.io.File;
import java.time.Instant;
import java.util.Date;

/**
 * @author youyinnn
 */
public class UserController extends Controller {

    private static UserService service;

    private static final Logger USER_OP_LOG = LogManager.getLogger("userOperation");

    private static final Logger USER_LOGIN_LOG = LogManager.getLogger("userLogin");

    private static final Logger USER_SIGNUP_LOG = LogManager.getLogger("userSignUp");

    private static final Logger SYSTEM_LOG = LogManager.getLogger("sysLog");

    static {
        try {
            service = (UserService) YouServiceIocContainer.getYouService(UserService.class);
        } catch (AutowiredException e) {
            e.printStackTrace();
        }
    }

    public void index(){
        System.out.println(PathKit.getWebRootPath());
        System.out.println(PathKit.getRootClassPath());
        YouProUtils.getSystemProperties();

        SYSTEM_LOG.info("index");
        renderNull();
    }

    public void signup(){

        UploadFile file = getFile();

        Long username = getParaToLong("username");
        String password = getPara("password");
        String nickname = getPara("nickname");
        String gender = getPara("gender");
        String info = getPara("info");
        String socialAccount = getPara("socialAccount");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setInfo(info);
        user.setNickname(nickname);
        user.setSocialAccount(socialAccount);

        Integer id = service.signUp(user);

        if (id == null) {
            file.getFile().delete();
            USER_SIGNUP_LOG.error("Fail");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user", "fail"));
        } else {
            ThreadContext.put("id", String.valueOf(id));
            USER_SIGNUP_LOG.info("Success");
            ThreadContext.clearAll();

            String portraitRenamePath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
            System.out.println(portraitRenamePath);
            file.getFile().renameTo(new File(portraitRenamePath));
            JSONObject jsonObject = JsonHelper.getJSONObjectDeepInPool("user", "success");
            jsonObject.put("id", id);
            renderJson(jsonObject);
        }
    }

    public void login(){
        Long username = getParaToLong("username");
        String password = getPara("password");

        User user = service.login(username, password);

        String ip = getRequest().getRemoteAddr();
        ThreadContext.put("ip", ip);
        if (user == null) {
            USER_LOGIN_LOG.error("fail");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user", "fail"));
        } else {
            JWTHelper jwtHelper = new JWTHelper();
            jwtHelper.setClaim("id",user.getId());
            jwtHelper.setClaim("ip",ip);
            jwtHelper.setIssuedAt(Date.from(Instant.now()));
            String token = jwtHelper.getToken();
            JSONObject json
                    = JsonHelper.getJSONObjectDeepInPool("user", "success");
            json.putAll(YouCollectionsUtils.getYouHashMap("userMsg", user, "token", token));

            ThreadContext.put("id", String.valueOf(user.getId()));
            USER_LOGIN_LOG.info("success");
            ThreadContext.clearAll();

            renderJson(json);
        }

    }

    public void update(){
        Integer id = getParaToInt("id");
        String nickname = getPara("nickname");
        String info = getPara("info");
        String socialAccount = getPara("socialAccount");

        boolean update = service.updateUserInfo(id, nickname, info, socialAccount);

        String ip = getRequest().getRemoteAddr();
        ThreadContext.put("id", String.valueOf(id));
        ThreadContext.put("ip", ip);
        if (update) {
            USER_OP_LOG.info("Update Info");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success"));
        } else {
            USER_OP_LOG.error("Update Info");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
        }
        ThreadContext.clearAll();
    }

    public void password(){
        Integer id = getParaToInt("id");
        String password = getPara("password");

        boolean updatePassword = service.updatePassword(id, password);

        String ip = getRequest().getRemoteAddr();
        ThreadContext.put("id", String.valueOf(id));
        ThreadContext.put("ip", ip);
        if (updatePassword) {
            USER_OP_LOG.info("Update Password");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success"));
        } else {
            USER_OP_LOG.error("Update Password");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
        }
        ThreadContext.clearAll();
    }

    public void updateportrait(){

        UploadFile newPortrait = getFile("portrait");

        if (newPortrait == null || newPortrait.getFile().length() <= 100) {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
            return;
        }

        Integer id = getParaToInt("id");
        String portraitPath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
        File old = new File(portraitPath);
        boolean delete = old.delete();

        String ip = getRequest().getRemoteAddr();
        ThreadContext.put("id", String.valueOf(id));
        ThreadContext.put("ip", ip);
        if (delete) {
            USER_OP_LOG.info("Update Portrait");
            newPortrait.getFile().renameTo(new File(portraitPath));
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success"));
        } else {
            USER_OP_LOG.error("Update Portrait");
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
        }
        ThreadContext.clearAll();
    }

    public void portrait(){
        Integer id = getParaToInt("id");
        String ip = getRequest().getRemoteAddr();
        ThreadContext.put("id", String.valueOf(id));
        ThreadContext.put("ip", ip);
        String portraitPath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
        USER_OP_LOG.info("Get Portrait");
        ThreadContext.clearAll();
        renderFile(new File(portraitPath));
    }
}
