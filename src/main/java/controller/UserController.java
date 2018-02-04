package controller;

import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;
import com.jfinal.core.Controller;
import model.User;
import service.UserService;

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

        getFile("portrait", "userPortrait");

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
        user.setNickName(nickname);
        user.setSocialAccount(socialAccount);

        Integer id = service.signUp(user);

        if (id == null) {
            renderJson(YouCollectionsUtils.getYouHashMap("code",1));
        } else {
            renderJson(YouCollectionsUtils.getYouHashMap("code",0,"id",id));
        }
    }

}
