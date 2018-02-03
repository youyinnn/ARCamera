package service;

import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youdbutils.ioc.annotations.Autowired;
import com.github.youyinnn.youdbutils.ioc.annotations.YouService;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;
import dao.UserDao;
import model.User;

/**
 * @author youyinnn
 */
@YouService
public class UserService {

    @Autowired
    private static UserDao dao;

    public Integer signUp(User user){
        Integer id = null;

        try {
            id = dao.addUser(user);
        } catch (NoneffectiveUpdateExecuteException e) {
            e.printStackTrace();
        }

        return id;
    }

    public User login(Integer username, String password) {
        return dao.getUser(YouCollectionsUtils.getYouHashMap("username",username,"password",password),
                YouCollectionsUtils.getYouArrayList("nickname","username","gender","info","socialAccount"));
    }

    public boolean updateUserInfo(Integer id, String nickname, String info, String socialAccount){

        int i = 0;

        try {
            i = dao.updateUser(id, YouCollectionsUtils.getYouHashMap(
                    "nickname", nickname,
                    "info", info,
                    "socialAccount", socialAccount));
        } catch (NoneffectiveUpdateExecuteException e) {
            e.printStackTrace();
        }

        return i == 1;
    }

    public boolean updatePassword(Integer id, String password) {
        int i = 0;
        try {
            i = dao.updateUser(id,YouCollectionsUtils.getYouHashMap("password",password));
        } catch (NoneffectiveUpdateExecuteException e) {
            e.printStackTrace();
        }
        return i == 1;
    }
}
