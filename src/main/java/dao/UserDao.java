package dao;

import com.github.youyinnn.youdbutils.dao.YouDao;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author youyinnn
 */
public class UserDao extends YouDao<User> {

    public Integer addUser(User user) throws NoneffectiveUpdateExecuteException {

        modelHandler.saveModel(user);

        return (Integer) modelHandler.getModelFieldValue("id",
                YouCollectionsUtils.getYouHashMap("username",user.getUsername()),
                "AND");
    }

    public int updateUser(Integer id, HashMap<String, Object> newFieldValuesMap) throws NoneffectiveUpdateExecuteException {

        return modelHandler.updateModel(newFieldValuesMap,
                YouCollectionsUtils.getYouHashMap("id", id),
                "AND");

    }

    public User getUser(HashMap<String, Object> conditionMap, ArrayList<String> queryFieldList) {
        return modelHandler.getModel(conditionMap,
                queryFieldList,"AND");
    }

}
