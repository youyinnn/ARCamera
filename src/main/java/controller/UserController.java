package controller;

import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import model.User;
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

    static {
        try {
            service = (UserService) YouServiceIocContainer.getYouService(UserService.class);
        } catch (AutowiredException e) {
            e.printStackTrace();
        }
    }

    public void index(){
        renderText("xixi");
    }

    public void signup(){

        UploadFile file = getFile("portrait");

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

        String portraitRenamePath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
        file.getFile().renameTo(new File(portraitRenamePath));

        if (id == null) {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user", "fail"));
        } else {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success").put("id", id));
        }
    }

    public void login(){
        Long username = getParaToLong("username");
        String password = getPara("password");

        User user = service.login(username, password);

        if (user == null) {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user", "fail"));
        } else {
            JWTHelper jwtHelper = new JWTHelper();
            jwtHelper.setClaim("id",user.getId());
            jwtHelper.setExpiration(Date.from(Instant.now()));
            String token = jwtHelper.getToken();
            JSONObject json
                    = JsonHelper.getJSONObjectDeepInPool("user", "success");
            json.putAll(YouCollectionsUtils.getYouHashMap("userMsg", user, "token", token));
            renderJson(json);
        }

    }

    public void update(){
        Integer id = getParaToInt("id");
        String nickname = getPara("nickname");
        String info = getPara("info");
        String socialAccount = getPara("socialAccount");

        boolean update = service.updateUserInfo(id, nickname, info, socialAccount);

        if (update) {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success"));
        } else {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
        }
    }

    public void password(){
        Integer id = getParaToInt("id");
        String password = getPara("password");

        boolean updatePassword = service.updatePassword(id, password);

        if (updatePassword) {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success"));
        } else {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
        }
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

        if (delete) {
            newPortrait.getFile().renameTo(new File(portraitPath));
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","success"));
        } else {
            renderJson(JsonHelper.getJSONObjectDeepInPool("user","fail"));
        }

    }

    public void portrait(){
        Integer id = getParaToInt("id");
        String portraitPath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
        renderFile(new File(portraitPath));
    }
}
