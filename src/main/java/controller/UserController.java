package controller;

import com.alibaba.fastjson.JSON;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import model.User;
import service.UserService;

import java.io.File;

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
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
        } else {
            renderJson(YouCollectionsUtils.getYouHashMap("code",0,"id",id));
        }
    }

    public void login(){
        Long username = getParaToLong("username");
        String password = getPara("password");

        User user = service.login(username, password);
        System.out.println(user);

        if (user == null) {
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
        } else {
            renderJson(JSON.toJSONString(YouCollectionsUtils.getYouHashMap("code",0,"userMsg",user)));
        }

    }

    public void update(){
        Integer id = getParaToInt("id");
        String nickname = getPara("nickname");
        String info = getPara("info");
        String socialAccount = getPara("socialAccount");

        boolean update = service.updateUserInfo(id, nickname, info, socialAccount);

        if (update) {
            renderJson(YouCollectionsUtils.getYouHashMap("code",0));
        } else {
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
        }
    }

    public void password(){
        Integer id = getParaToInt("id");
        String password = getPara("password");

        boolean updatePassword = service.updatePassword(id, password);

        if (updatePassword) {
            renderJson(YouCollectionsUtils.getYouHashMap("code",0));
        } else {
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
        }
    }

    public void updateportrait(){

        UploadFile newPortrait = getFile("portrait");

        if (newPortrait == null) {
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
            return;
        }

        Integer id = getParaToInt("id");
        String portraitPath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
        File old = new File(portraitPath);
        boolean delete = old.delete();

        if (delete) {
            newPortrait.getFile().renameTo(new File(portraitPath));
            renderJson(YouCollectionsUtils.getYouHashMap("code",0));
        } else {
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
        }

    }

    public void portrait(){
        Integer id = getParaToInt("id");
        String portraitPath = PathKit.getWebRootPath() + File.separator + "img" + File.separator + id + ".png";
        renderFile(new File(portraitPath));
    }
}
